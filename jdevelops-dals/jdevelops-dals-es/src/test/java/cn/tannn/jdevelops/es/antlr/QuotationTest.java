//package cn.tannn.jdevelops.es.antlr;
//
//import co.elastic.clients.elasticsearch._types.query_dsl.Query;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class QuotationTest {
//    private final ElasticSearchQueryBuilder queryBuilder = new ElasticSearchQueryBuilder.Builder().build();
//
//    @Test
//    @DisplayName("测试基本相等比较")
//    void testBasicEquality() {
//        assertQueryProcessing(
//            "name==tan1",
//            "name=='tan1'"
//        );
//    }
//
//    @Test
//    @DisplayName("测试数值比较")
//    void testNumericComparisons() {
//        var tests = Map.of(
//            "age==25", "age==25",
//            "price>=10.5", "price>=10.5",
//            "count<100", "count<100"
//        );
//        assertQueriesProcessing(tests);
//    }
//
//    @Test
//    @DisplayName("测试字符串比较和模式匹配")
//    void testStringPatternMatching() {
//        var tests = Map.of(
//            "name+=pattern", "name+='pattern'",
//            "email=~gmail", "email=~'gmail'",
//            "title!~temp", "title!~'temp'"
//        );
//        assertQueriesProcessing(tests);
//    }
//
//    @Test
//    @DisplayName("测试数组操作")
//    void testArrayOperations() {
//        var tests = Map.of(
//            "tags in [tag1, tag2]", "tags in ['tag1', 'tag2']",
//            "status not in [active, pending]", "status not in ['active', 'pending']"
//        );
//        assertQueriesProcessing(tests);
//    }
//
//    @Test
//    @DisplayName("测试复合表达式")
//    void testCompoundExpressions() {
//        var tests = Map.of(
//            "name==john and age>20", "name=='john' and age>20",
//            "(status==active or status==pending) and priority==high",
//            "(status=='active' or status=='pending') and priority=='high'"
//        );
//        assertQueriesProcessing(tests);
//    }
//
//    @Test
//    @DisplayName("测试已有引号的值")
//    void testQuotedValues() {
//        var tests = Map.of(
//            "name==\"quoted\"", "name==\"quoted\"",
//            "title=='single'", "title=='single'"
//        );
//        assertQueriesProcessing(tests);
//    }
//
//    @Test
//    @DisplayName("测试空值和特殊值")
//    void testNullAndSpecialValues() {
//        var tests = Map.of(
//            "name==null", "name==null",
//            "status!=null", "status!=null"
//        );
//        assertQueriesProcessing(tests);
//    }
//
//    @Test
//    @DisplayName("测试复杂嵌套表达式")
//    void testComplexNestedExpressions() {
//        var tests = Map.of(
//            "((name==john or name==jane) and age>20) or status==vip",
//            "((name=='john' or name=='jane') and age>20) or status=='vip'",
//
//            "(tags in [new, hot] and price<100) or featured==true",
//            "(tags in ['new', 'hot'] and price<100) or featured=='true'"
//        );
//        assertQueriesProcessing(tests);
//    }
//
//    @Test
//    @DisplayName("测试边界情况")
//    void testEdgeCases() {
//        var tests = Map.of(
//            "name==", "name==",  // 空值
//            "==value", "=='value'",  // 缺少字段名
//            "  name  ==  value  ", "  name  ==  'value'  "  // 多余空格
//        );
//        assertQueriesProcessing(tests);
//    }
//
//    private void assertQueryProcessing(String input, String expected) {
//        Query result = queryBuilder.buildQuery(input);
//        assertNotNull(result, "Query should not be null for: " + input);
//        System.out.println("Input: " + input);
//        System.out.println("Expected: " + expected);
//        System.out.println("Result: " + result);
//        System.out.println("---");
//    }
//
//    private void assertQueriesProcessing(Map<String, String> tests) {
//        tests.forEach((input, expected) -> assertQueryProcessing(input, expected));
//    }
//}
