package cn.tannn.jdevelops.frameworks.web.starter.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持 Ant 风格模式的高性能 DFA 路径匹配器
 * 支持模式：
 * - ? : 匹配单个字符
 * - * : 匹配零个或多个字符（不包括路径分隔符）
 * - ** : 匹配零个或多个路径段
 * - 精确匹配
 */
public class AntStyleDFAPathMatcher {

    // DFA状态类型枚举
    enum StateType {
        NORMAL,          // 普通状态
        SINGLE_WILDCARD, // ? 通配符状态
        STAR_WILDCARD,   // * 通配符状态
        DOUBLE_STAR,     // ** 通配符状态
        ACCEPTING        // 接受状态
    }

    // DFA状态节点
    static class State {
        int id;
        StateType type;
        Map<Character, Set<State>> transitions = new HashMap<>();
        Set<State> epsilonTransitions = new HashSet<>(); // ε转换
        boolean isAccepting = false;
        String matchedPattern = null;
        int priority = Integer.MAX_VALUE; // 优先级，数字越小优先级越高

        public State(int id, StateType type) {
            this.id = id;
            this.type = type;
        }

        @Override
        public String toString() {
            return "State{id=" + id + ", type=" + type + ", accepting=" + isAccepting + "}";
        }
    }

    private final State startState;
    private final Map<String, String> patternCache = new ConcurrentHashMap<>();
    private final List<String> originalPatterns;
    private int stateIdCounter = 0;

    public AntStyleDFAPathMatcher(List<String> patterns) {
        this.originalPatterns = new ArrayList<>(patterns);
        this.startState = buildNFA(patterns);
    }

    /**
     * 构建 NFA（然后用子集构造法转换为DFA的概念，这里简化为NFA处理）
     */
    private State buildNFA(List<String> patterns) {
        State start = new State(stateIdCounter++, StateType.NORMAL);

        // 按优先级排序模式（更具体的模式优先级更高）
        List<PatternWithPriority> sortedPatterns = new ArrayList<>();
        for (int i = 0; i < patterns.size(); i++) {
            int priority = calculatePriority(patterns.get(i));
            sortedPatterns.add(new PatternWithPriority(patterns.get(i), priority, i));
            System.out.println("模式: " + patterns.get(i) + " -> 优先级: " + priority);
        }
        sortedPatterns.sort(Comparator.comparingInt(p -> p.priority));

        System.out.println("排序后的模式:");
        for (PatternWithPriority p : sortedPatterns) {
            System.out.println("  " + p.pattern + " (优先级: " + p.priority + ")");
        }

        // 为每个模式构建状态机分支
        for (PatternWithPriority patternInfo : sortedPatterns) {
            addPatternToNFA(start, patternInfo.pattern, patternInfo.priority);
        }

        return start;
    }

    static class PatternWithPriority {
        String pattern;
        int priority;
        int originalIndex;

        PatternWithPriority(String pattern, int priority, int originalIndex) {
            this.pattern = pattern;
            this.priority = priority;
            this.originalIndex = originalIndex;
        }
    }

    /**
     * 计算模式优先级（越具体优先级越高，数值越小）
     */
    private int calculatePriority(String pattern) {
        int wildcardWeight = 0;
        int exactCharCount = 0;

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '*') {
                if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '*') {
                    wildcardWeight += 1000; // ** 权重最高
                    i++; // 跳过下一个*
                } else {
                    wildcardWeight += 100; // * 权重中等
                }
            } else if (c == '?') {
                wildcardWeight += 10; // ? 权重最低
            } else {
                exactCharCount++; // 精确字符增加具体性
            }
        }

        // 优先级 = 通配符权重 - 精确字符数量（精确字符越多优先级越高）
        return wildcardWeight - exactCharCount;
    }

    private int countWildcards(String pattern) {
        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '*' || c == '?') {
                count++;
                if (c == '*' && i + 1 < pattern.length() && pattern.charAt(i + 1) == '*') {
                    i++; // 跳过 ** 中的第二个*
                }
            }
        }
        return count;
    }

    /**
     * 将模式添加到NFA中
     */
    private void addPatternToNFA(State start, String pattern, int priority) {
        State current = start;
        int i = 0;

        while (i < pattern.length()) {
            char c = pattern.charAt(i);

            if (c == '*' && i + 1 < pattern.length() && pattern.charAt(i + 1) == '*') {
                // 处理 ** 模式
                current = handleDoubleStar(current, pattern, i + 2, priority);
                return; // ** 处理完整个模式
            } else if (c == '*') {
                // 处理 * 模式
                current = handleSingleStar(current, pattern, i + 1, priority);
                return; // * 处理完整个模式
            } else if (c == '?') {
                // 处理 ? 模式
                current = handleQuestionMark(current);
                i++;
            } else {
                // 处理普通字符
                current = handleNormalChar(current, c);
                i++;
            }
        }

        // 设置接受状态
        current.isAccepting = true;
        current.matchedPattern = pattern;
        current.priority = Math.min(current.priority, priority);
    }

    /**
     * 处理双星号 ** 模式
     */
    private State handleDoubleStar(State current, String pattern, int nextIndex, int priority) {
        State doubleStarState = new State(stateIdCounter++, StateType.DOUBLE_STAR);

        // ** 可以匹配零个字符（直接跳过）
        current.epsilonTransitions.add(doubleStarState);

        // ** 可以匹配任何字符，包括路径分隔符（自循环）
        for (char c = 32; c <= 126; c++) {
            doubleStarState.transitions.computeIfAbsent(c, k -> new HashSet<>()).add(doubleStarState);
        }

        // 处理 ** 后面的部分
        if (nextIndex < pattern.length()) {
            String remaining = pattern.substring(nextIndex);
            State nextState = new State(stateIdCounter++, StateType.NORMAL);
            doubleStarState.epsilonTransitions.add(nextState);
            addPatternToNFA(nextState, remaining, priority);
            return doubleStarState;  // 返回doubleStarState以便正确处理
        } else {
            doubleStarState.isAccepting = true;
            doubleStarState.matchedPattern = pattern;
            doubleStarState.priority = priority;
            return doubleStarState;
        }
    }

    /**
     * 处理单星号 * 模式
     */
    private State handleSingleStar(State current, String pattern, int nextIndex, int priority) {
        State starState = new State(stateIdCounter++, StateType.STAR_WILDCARD);

        // * 可以匹配零个字符（ε转换到处理后续模式的状态）
        if (nextIndex < pattern.length()) {
            String remaining = pattern.substring(nextIndex);
            State nextState = new State(stateIdCounter++, StateType.NORMAL);
            // 零匹配：直接跳到后续模式
            current.epsilonTransitions.add(nextState);
            addPatternToNFA(nextState, remaining, priority);

            // 非零匹配：先到starState，然后可能继续匹配或跳到后续模式
            current.epsilonTransitions.add(starState);
            starState.epsilonTransitions.add(nextState);

            // * 可以匹配任何非路径分隔符的字符（自循环）
            for (char c = 32; c <= 126; c++) {
                if (c != '/') {
                    starState.transitions.computeIfAbsent(c, k -> new HashSet<>()).add(starState);
                }
            }

            return starState;
        } else {
            // * 在模式末尾
            current.epsilonTransitions.add(starState);
            starState.isAccepting = true;
            starState.matchedPattern = pattern;
            starState.priority = priority;

            // * 可以匹配任何非路径分隔符的字符
            for (char c = 32; c <= 126; c++) {
                if (c != '/') {
                    starState.transitions.computeIfAbsent(c, k -> new HashSet<>()).add(starState);
                }
            }

            return starState;
        }
    }

    /**
     * 处理问号 ? 模式
     */
    private State handleQuestionMark(State current) {
        State nextState = new State(stateIdCounter++, StateType.NORMAL);

        // ? 可以匹配任何单个字符，但不包括路径分隔符
        for (char c = 32; c <= 126; c++) {
            if (c != '/') {
                current.transitions.computeIfAbsent(c, k -> new HashSet<>()).add(nextState);
            }
        }

        return nextState;
    }

    /**
     * 处理普通字符
     */
    private State handleNormalChar(State current, char c) {
        // 查找是否已存在对应字符的转换状态
        Set<State> existingStates = current.transitions.get(c);
        if (existingStates != null && !existingStates.isEmpty()) {
            return existingStates.iterator().next();
        }

        State nextState = new State(stateIdCounter++, StateType.NORMAL);
        current.transitions.computeIfAbsent(c, k -> new HashSet<>()).add(nextState);
        return nextState;
    }

    /**
     * 主要匹配方法
     */
    public boolean matches(String path) {
        // 先检查缓存
        String cachedResult = patternCache.get(path);
        if (cachedResult != null) {
            return !cachedResult.isEmpty();
        }

        String matchedPattern = getMatchedPattern(path);
        boolean result = matchedPattern != null;

        // 缓存结果
        if (patternCache.size() < 10000) {
            patternCache.put(path, matchedPattern != null ? matchedPattern : "");
        }

        return result;
    }

    /**
     * 获取匹配的模式
     */
    public String getMatchedPattern(String path) {
        Set<State> currentStates = new HashSet<>();
        currentStates.add(startState);
        addEpsilonClosure(currentStates);

        for (char c : path.toCharArray()) {
            Set<State> nextStates = new HashSet<>();

            for (State state : currentStates) {
                Set<State> transitions = state.transitions.get(c);
                if (transitions != null) {
                    nextStates.addAll(transitions);
                }
            }

            if (nextStates.isEmpty()) {
                return null;
            }

            addEpsilonClosure(nextStates);
            currentStates = nextStates;
        }

        // 找到优先级最高的匹配模式
        String bestMatch = null;
        int bestPriority = Integer.MAX_VALUE;

        for (State state : currentStates) {
            if (state.isAccepting && state.priority < bestPriority) {
                bestMatch = state.matchedPattern;
                bestPriority = state.priority;
            }
        }

        return bestMatch;
    }

    /**
     * 添加ε转换闭包
     */
    private void addEpsilonClosure(Set<State> states) {
        Queue<State> queue = new LinkedList<>(states);
        Set<State> visited = new HashSet<>(states);

        while (!queue.isEmpty()) {
            State current = queue.poll();
            for (State epsilonState : current.epsilonTransitions) {
                if (!visited.contains(epsilonState)) {
                    visited.add(epsilonState);
                    states.add(epsilonState);
                    queue.offer(epsilonState);
                }
            }
        }
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        patternCache.clear();
    }

    /**
     * 获取统计信息
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStates", stateIdCounter);
        stats.put("cacheSize", patternCache.size());
        stats.put("patternCount", originalPatterns.size());
        return stats;
    }
}
