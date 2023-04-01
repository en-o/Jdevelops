package cn.jdevelops.util.core.string.privates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前缀树过滤node
 *
 * @author tn
 */
public class Node {
    Node root;

    public Node(Node root) {
        this.root = root;
    }

    public Node() {
    }

    /**
     * 节点
     */
    private Map<Character, Node> nextNodes = new HashMap<>();

    public void addNextNode(Character key, Node node) {
        nextNodes.put(key, node);
    }

    public Node getNextNode(Character key) {
        return nextNodes.get(key);
    }

    public boolean isLastCharacter() {
        return nextNodes.isEmpty();
    }




    public void addWord(String word) {

        Node tempNode = root;
        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            Node node = tempNode.getNextNode(c);
            if (node == null) {
                node = new Node();
                tempNode.addNextNode(c, node);
            }
            // 移动到下一个字
            tempNode = node;


        }
    }

    public void addWord(List<String> words){
        words.forEach(this::addWord);
    }

    public String filter(String text,String sensitiveWords) {
        StringBuilder result = new StringBuilder(text.length());
        Node tempNode = root;
        int begin = 0;
        int position = 0;
        while (position < text.length()) {

            Character c = text.charAt(position);
            tempNode = tempNode.getNextNode(c);

            if (tempNode == null) {
                ////如果匹配失败，合法
                result.append(text.charAt(begin));
                begin = begin + 1;
                position = begin;
                //从新匹配
                tempNode = root;
                continue;
            } else if (tempNode.isLastCharacter()) {
                //匹配结束，替换敏感词
                result.append(sensitiveWords);
                position++;
                begin = position;
                tempNode = root;
            } else {
                position++;
            }

        }
        //添加剩下的内容
        result.append(text.substring(begin));
        return result.toString();
    }

}
