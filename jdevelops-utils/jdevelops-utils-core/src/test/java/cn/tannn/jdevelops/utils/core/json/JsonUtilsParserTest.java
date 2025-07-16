package cn.tannn.jdevelops.utils.core.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JSON解析器测试")
class JsonUtilsParserTest {

    private static final String OPENAI_RESPONSE = """
    {
        "id": "chatcmpl-123",
        "object": "chat.completion",
        "created": 1684340851,
        "model": "gpt-3.5-turbo",
        "choices": [
            {
                "index": 0,
                "delta": {
                    "content": "Hello, how can I help you?",
                    "role": "assistant"
                },
                "finish_reason": "stop"
            }
        ],
        "usage": {
            "prompt_tokens": 10,
            "completion_tokens": 20,
            "total_tokens": 30
        }
    }
    """;

    private static final String COMPLEX_NESTED_RESPONSE = """
    {
        "data": {
            "users": [
                {
                    "name": "John",
                    "details": {
                        "age": 30,
                        "contacts": {
                            "email": "john@example.com",
                            "phone": "123456789"
                        }
                    },
                    "scores": [85, 90, 95]
                }
            ]
        },
        "metadata": {
            "timestamp": "2025-07-16T01:21:10Z"
        }
    }
    """;

    @Test
    @DisplayName("解析简单路径")
    void testSimplePath() {
        String result = JsonUtils.parse(OPENAI_RESPONSE, "object", String.class);
        assertEquals("chat.completion", result);
    }

    @Test
    @DisplayName("解析嵌套路径和数组索引")
    void testNestedPathWithArrayIndex() {
        String content = JsonUtils.parse(OPENAI_RESPONSE, "choices[0].delta.content", String.class);
        assertEquals("Hello, how can I help you?", content);
    }

    @Test
    @DisplayName("解析数值类型")
    void testNumericValues() {
        Integer promptTokens = JsonUtils.parse(OPENAI_RESPONSE, "usage.prompt_tokens", Integer.class);
        assertEquals(10, promptTokens);
    }

    @Test
    @DisplayName("解析复杂嵌套结构")
    void testComplexNestedStructure() {
        String email = JsonUtils.parse(COMPLEX_NESTED_RESPONSE,
                "data.users[0].details.contacts.email", String.class);
        assertEquals("john@example.com", email);
    }

    @Test
    @DisplayName("解析数组值")
    void testArrayValues() {
        @SuppressWarnings("unchecked")
        List<Integer> scores = JsonUtils.parse(COMPLEX_NESTED_RESPONSE,
                "data.users[0].scores", List.class);
        assertEquals(List.of(85, 90, 95), scores);
    }

    @Test
    @DisplayName("自定义对象映射")
    void testCustomObjectMapping() {
        Usage usage = JsonUtils.parse(OPENAI_RESPONSE, "usage", Usage.class);
        assertNotNull(usage);
        assertEquals(10, usage.getPromptTokens());
        assertEquals(20, usage.getCompletionTokens());
        assertEquals(30, usage.getTotalTokens());
    }

    @ParameterizedTest
    @MethodSource("providePathTestCases")
    @DisplayName("多种路径格式测试")
    void testVariousPathFormats(String path, Class<?> type, Object expected) {
        Object result = JsonUtils.parse(COMPLEX_NESTED_RESPONSE, path, type);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> providePathTestCases() {
        return Stream.of(
                Arguments.of("data.users[0].name", String.class, "John"),
                Arguments.of("data.users[0].details.age", Integer.class, 30),
                Arguments.of("metadata.timestamp", String.class, "2025-07-16T01:21:10Z")
        );
    }

    @Nested
    @DisplayName("异常处理测试")
    class ExceptionHandlingTest {

        @Test
        @DisplayName("无效路径抛出异常")
        void testInvalidPath() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> JsonUtils.parse(OPENAI_RESPONSE, "invalid.path", String.class));
            assertTrue(exception.getMessage().contains("无效路径"));
        }

        @Test
        @DisplayName("类型不匹配抛出异常")
        void testTypeMismatch() {
            assertThrows(RuntimeException.class,
                    () -> JsonUtils.parse(OPENAI_RESPONSE, "choices[0].delta.content", Integer.class));
        }

        @Test
        @DisplayName("空JSON字符串抛出异常")
        void testEmptyJsonString() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> JsonUtils.parse("", "path", String.class));
            assertTrue(exception.getMessage().contains("JSON字符串不能为空"));
        }

        @Test
        @DisplayName("空路径抛出异常")
        void testEmptyPath() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> JsonUtils.parse(OPENAI_RESPONSE, "", String.class));
            assertTrue(exception.getMessage().contains("路径不能为空"));
        }

        @Test
        @DisplayName("空类型抛出异常")
        void testNullType() {
            assertThrows(IllegalArgumentException.class,
                    () -> JsonUtils.parse(OPENAI_RESPONSE, "object", null));
        }
    }

    @Nested
    @DisplayName("边界情况测试")
    class EdgeCasesTest {

        @Test
        @DisplayName("空对象处理")
        void testEmptyObjects() {
            String json = "{\"empty\":{}}";
            @SuppressWarnings("unchecked")
            Map<String, Object> result = JsonUtils.parse(json, "empty", Map.class);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("空数组处理")
        void testEmptyArrays() {
            String json = "{\"array\":[]}";
            List<?> result = JsonUtils.parse(json, "array", List.class);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("null值处理")
        void testNullValues() {
            String json = "{\"nullValue\":null}";
            Object result = JsonUtils.parse(json, "nullValue", Object.class);
            assertNull(result);
        }

        @Test
        @DisplayName("数组越界")
        void testArrayIndexOutOfBounds() {
            assertThrows(IllegalArgumentException.class,
                    () -> JsonUtils.parse(OPENAI_RESPONSE, "choices[10].delta.content", String.class));
        }

        @Test
        @DisplayName("非数组字段使用数组索引")
        void testNonArrayFieldWithArrayIndex() {
            assertThrows(IllegalArgumentException.class,
                    () -> JsonUtils.parse(OPENAI_RESPONSE, "object[0]", String.class));
        }
    }

    @Nested
    @DisplayName("性能测试")
    class PerformanceTest {

        @Test
        @DisplayName("大型嵌套对象处理效率")
        void testLargeNestedObjectsEfficiency() {
            StringBuilder jsonBuilder = new StringBuilder("{\"level1\":{");
            for (int i = 0; i < 1000; i++) {
                jsonBuilder.append("\"key").append(i).append("\":{\"value\":").append(i).append("},");
            }
            jsonBuilder.setLength(jsonBuilder.length() - 1);
            jsonBuilder.append("}}");

            String json = jsonBuilder.toString();
            long startTime = System.nanoTime();
            Integer result = JsonUtils.parse(json, "level1.key999.value", Integer.class);
            long endTime = System.nanoTime();

            assertEquals(999, result);
            assertTrue((endTime - startTime) < 1_000_000_000); // 1秒内完成
        }

        @Test
        @DisplayName("深层嵌套路径解析")
        void testDeepNestedPath() {
            String json = "{\"a\":{\"b\":{\"c\":{\"d\":{\"e\":{\"value\":\"深层值\"}}}}}}";
            String result = JsonUtils.parse(json, "a.b.c.d.e.value", String.class);
            assertEquals("深层值", result);
        }
    }
}

// 测试支持类
class Usage {
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;

    // 标准的getter和setter方法
    public int getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    @Override
    public String toString() {
        return "Usage{" +
                "promptTokens=" + promptTokens +
                ", completionTokens=" + completionTokens +
                ", totalTokens=" + totalTokens +
                '}';
    }
}
