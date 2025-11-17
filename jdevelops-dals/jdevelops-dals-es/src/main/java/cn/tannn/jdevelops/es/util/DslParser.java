package cn.tannn.jdevelops.es.util;

import cn.tannn.jdevelops.es.util.pojo.EsExtractResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DslParser {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private StringBuilder sqlBuilder = new StringBuilder();
    private List<String> parameters = new ArrayList<>();


    /**
     * 将 SearchRequest.toString() 解析为 SQL
     * <p> 注意 tostring的格式 [SearchRequest: POST /citation_comment xx] 所有需要 SearchRequest.toString().indexOf(":") + 1 </p>
     * @param dslQueryReqStr POST /citation_paper/_search?typed_keys=true {}
     * @return ParseResult
     */
    public ParseResult parse(String dslQueryReqStr) {
        try {
            EsExtractResult extract = extract(dslQueryReqStr);
            JsonNode dslJson = objectMapper.readTree(extract.getJsonBody());
            parseSelect(dslJson.get("_source"));
            parseFrom(extract.getTableName());
            JsonNode query = dslJson.get("query");
            parseWhere(query);
            parseSort(dslJson.get("sort"));
            parseLimit(dslJson.get("size"), dslJson.get("from"));
            return new ParseResult(sqlBuilder.toString(), parameters,query.toString());
        } catch (Exception e) {
            throw new RuntimeException("DSL解析失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将 SearchRequest.toString() 解析为 SQL
     * <p> 注意 tostring的格式 [SearchRequest: POST /citation_comment xx] 所有需要 SearchRequest.toString().indexOf(":") + 1 </p>
     *
     * @param dslQuery  {}
     * @param tableName tableName
     * @return ParseResult
     */
    public ParseResult parse(String dslQuery, String tableName) {
        try {
            JsonNode dslJson = objectMapper.readTree(dslQuery);
            parseSelect(dslJson.get("_source"));
            parseFrom(tableName);
            JsonNode query = dslJson.get("query");
            parseWhere(query);
            parseSort(dslJson.get("sort"));
            parseLimit(dslJson.get("size"), dslJson.get("from"));

            return new ParseResult(sqlBuilder.toString(), parameters,query.toString());
        } catch (Exception e) {
            throw new RuntimeException("DSL解析失败: " + e.getMessage(), e);
        }
    }

    private void parseSelect(JsonNode sourceNode) {
        sqlBuilder.append("SELECT ");
        Set<String> fields = new LinkedHashSet<>();

        if (sourceNode != null) {
            JsonNode includes = sourceNode.get("includes");
            if (includes != null && includes.isArray()) {
                includes.forEach(field -> fields.add(convertFieldName(field.asText())));
            }

            // 处理排除字段
            JsonNode excludes = sourceNode.get("excludes");
            if (excludes != null && excludes.isArray()) {
                excludes.forEach(field -> fields.remove(convertFieldName(field.asText())));
            }
        }

        if (fields.isEmpty()) {
            sqlBuilder.append("*");
        } else {
            sqlBuilder.append(String.join(", ", fields));
        }
    }

    private void parseFrom(String tableName) {
        // 从索引名称获取表名
        sqlBuilder.append("\nFROM ").append(tableName);
    }


    private void parseWhere(JsonNode queryNode) {
        if (queryNode == null) return;

        sqlBuilder.append("\nWHERE ");
        parseQueryNode(queryNode);
    }

    private void parseQueryNode(JsonNode queryNode) {
        if (queryNode.has("bool")) {
            parseBoolQuery(queryNode.get("bool"));
        } else if (queryNode.has("match")) {
            parseMatchQuery(queryNode.get("match"));
        } else if (queryNode.has("term")) {
            parseTermQuery(queryNode.get("term"));
        } else if (queryNode.has("terms")) {
            parseTermsQuery(queryNode.get("terms"));
        } else if (queryNode.has("range")) {
            parseRangeQuery(queryNode.get("range"));
        } else if (queryNode.has("exists")) {
            parseExistsQuery(queryNode.get("exists"));
        } else if (queryNode.has("multi_match")) {
            parseMultiMatchQuery(queryNode.get("multi_match"));
        } else if (queryNode.has("query_string")) {
            parseQueryStringQuery(queryNode.get("query_string"));
        } else if (queryNode.has("match_phrase")) {
            parseMatchPhraseQuery(queryNode.get("match_phrase"));
        } else if (queryNode.has("match_phrase_prefix")) {
            parseMatchPhrasePrefixQuery(queryNode.get("match_phrase_prefix"));
        } else if (queryNode.has("wildcard")) {
            parseWildcardQuery(queryNode.get("wildcard"));
        }
    }

    private void parseBoolQuery(JsonNode boolNode) {
        List<String> conditions = new ArrayList<>();

        // 处理 must 条件（AND）
        if (boolNode.has("must")) {
            JsonNode mustNode = boolNode.get("must");
            List<String> mustConditions = new ArrayList<>();
            if (mustNode.isArray()) {
                mustNode.forEach(node -> {
                    parseQueryNode(node);
                    String condition = sqlBuilder.toString();
                    // 清除之前的SQL构建结果
                    sqlBuilder.setLength(0);
                    mustConditions.add(condition);
                });
                if (!mustConditions.isEmpty()) {
                    conditions.add(String.join(" AND ", mustConditions));
                }
            } else {
                parseQueryNode(mustNode);
                conditions.add("(" + sqlBuilder.toString() + ")");
                sqlBuilder.setLength(0);
            }
        }

        // 处理 should 条件（OR）
        if (boolNode.has("should")) {
            JsonNode shouldNode = boolNode.get("should");
            List<String> shouldConditions = new ArrayList<>();
            if (shouldNode.isArray()) {
                shouldNode.forEach(node -> {
                    parseQueryNode(node);
                    String condition = sqlBuilder.toString();
                    // 清除之前的SQL构建结果
                    sqlBuilder.setLength(0);
                    shouldConditions.add(condition);
                });
                if (!shouldConditions.isEmpty()) {
                    conditions.add(String.join(" OR ", shouldConditions));
                }
            }
        }

        // 处理 must_not 条件（NOT）
        if (boolNode.has("must_not")) {
            JsonNode mustNotNode = boolNode.get("must_not");
            List<String> notConditions = new ArrayList<>();
            if (mustNotNode.isArray()) {
                mustNotNode.forEach(node -> {
                    parseQueryNode(node);
                    String condition = sqlBuilder.toString();
                    // 清除之前的SQL构建结果
                    sqlBuilder.setLength(0);
                    notConditions.add("NOT (" + condition + ")");
                });
                conditions.addAll(notConditions);
            }
        }

        if (!conditions.isEmpty()) {
            sqlBuilder.append(String.join(" AND ", conditions));
        }
    }


    private void parseMatchQuery(JsonNode matchNode) {
        matchNode.fields().forEachRemaining(field -> {
            String fieldName = field.getKey();
            JsonNode valueNode = field.getValue();
            String value;
            if (valueNode.isObject()) {
                value = valueNode.get("query").asText();
            } else {
                value = valueNode.asText();
            }
            // 直接使用值而不是参数占位符
            sqlBuilder.append(convertFieldName(fieldName))
                    .append(" LIKE '%")
                    .append(value)
                    .append("%'");
        });
    }

    private void parseTermQuery(JsonNode termNode) {
        termNode.fields().forEachRemaining(field -> {
            String fieldName = field.getKey();
            JsonNode valueNode = field.getValue();
            // Check if the value is an object with a "value" field
            Object value = extractTermValue(valueNode);
            if (value != null) {
                appendTermCondition(fieldName, value);
            }
        });
    }

    private void parseRangeQuery(JsonNode rangeNode) {
        rangeNode.fields().forEachRemaining(field -> {
            String fieldName = field.getKey();
            JsonNode conditions = field.getValue();
            List<String> rangeConditions = new ArrayList<>();

            if (conditions.has("gt")) {
                String value = conditions.get("gt").asText();
                rangeConditions.add(convertFieldName(fieldName) + " > '" + value + "'");
            }
            if (conditions.has("gte")) {
                String value = conditions.get("gte").asText();
                rangeConditions.add(convertFieldName(fieldName) + " >= '" + value + "'");
            }
            if (conditions.has("lt")) {
                String value = conditions.get("lt").asText();
                rangeConditions.add(convertFieldName(fieldName) + " < '" + value + "'");
            }
            if (conditions.has("lte")) {
                String value = conditions.get("lte").asText();
                rangeConditions.add(convertFieldName(fieldName) + " <= '" + value + "'");
            }

            sqlBuilder.append("(")
                    .append(String.join(" AND ", rangeConditions))
                    .append(")");
        });
    }

    private void parseExistsQuery(JsonNode existsNode) {
        String fieldName = existsNode.get("field").asText();
        sqlBuilder.append(convertFieldName(fieldName))
                .append(" IS NOT NULL");
    }

    private void parseMultiMatchQuery(JsonNode multiMatchNode) {
        List<String> conditions = new ArrayList<>();
        JsonNode fieldsNode = multiMatchNode.get("fields");
        String query = multiMatchNode.get("query").asText();

        if (fieldsNode.isArray()) {
            fieldsNode.forEach(field -> {
                parameters.add("%" + query + "%");
                conditions.add(convertFieldName(field.asText()) + " LIKE ?");
            });
        }

        sqlBuilder.append("(")
                .append(String.join(" OR ", conditions))
                .append(")");
    }

    private void parseQueryStringQuery(JsonNode queryStringNode) {
        String query = queryStringNode.get("query").asText();
        // 简单处理，实际可能需要更复杂的解析
        parameters.add("%" + query + "%");
        sqlBuilder.append("text_column LIKE ?");
    }

    // 添加 terms 查询解析方法
    private void parseTermsQuery(JsonNode termsNode) {
        termsNode.fields().forEachRemaining(field -> {
            String fieldName = field.getKey();
            JsonNode valuesNode = field.getValue();

            if (valuesNode.isArray() && valuesNode.size() > 0) {
                List<String> formattedValues = new ArrayList<>();

                // Process each value in the array
                valuesNode.forEach(valueNode -> {
                    Object value = extractTermValue(valueNode);
                    if (value != null) {
                        formattedValues.add(formatValueForSql(value));
                    }
                });

                if (!formattedValues.isEmpty()) {
                    sqlBuilder.append(convertFieldName(fieldName))
                            .append(" IN (")
                            .append(String.join(", ", formattedValues))
                            .append(")");
                }
            } else if (valuesNode.isObject() && valuesNode.has("value")) {
                // Handle case where terms query has a value object
                JsonNode valueArray = valuesNode.get("value");
                if (valueArray.isArray() && valueArray.size() > 0) {
                    List<String> formattedValues = new ArrayList<>();
                    valueArray.forEach(v -> {
                        Object value = extractTermValue(v);
                        if (value != null) {
                            formattedValues.add(formatValueForSql(value));
                        }
                    });

                    if (!formattedValues.isEmpty()) {
                        sqlBuilder.append(convertFieldName(fieldName))
                                .append(" IN (")
                                .append(String.join(", ", formattedValues))
                                .append(")");
                    }
                }
            }
        });
    }



    // Match Phrase Query - Matches exact phrases in the field
    private void parseMatchPhraseQuery(JsonNode matchPhraseNode) {
        matchPhraseNode.fields().forEachRemaining(field -> {
            String fieldName = field.getKey();
            JsonNode valueNode = field.getValue();
            String value;
            if (valueNode.isObject()) {
                value = valueNode.get("query").asText();
            } else {
                value = valueNode.asText();
            }
            sqlBuilder.append(convertFieldName(fieldName))
                    .append(" LIKE '%")
                    .append(value.replace("'", "''"))  // Escape single quotes
                    .append("%'");  // Use LIKE for phrase matching
        });
    }

    // Match Phrase Prefix Query - Matches phrases with a prefix
    private void parseMatchPhrasePrefixQuery(JsonNode matchPhrasePrefixNode) {
        matchPhrasePrefixNode.fields().forEachRemaining(field -> {
            String fieldName = field.getKey();
            JsonNode valueNode = field.getValue();
            String value;
            if (valueNode.isObject()) {
                value = valueNode.get("query").asText();
            } else {
                value = valueNode.asText();
            }
            sqlBuilder.append(convertFieldName(fieldName))
                    .append(" LIKE '")
                    .append(value.replace("'", "''"))  // Escape single quotes
                    .append("%'");  // Add only trailing wildcard
        });
    }

    // Wildcard Query - Supports * and ? wildcards
    private void parseWildcardQuery(JsonNode wildcardNode) {
        wildcardNode.fields().forEachRemaining(field -> {
            String fieldName = field.getKey();
            String value = field.getValue().asText();
            // Convert Elasticsearch wildcards to SQL wildcards
            value = value.replace('*', '%').replace('?', '_');
            sqlBuilder.append(convertFieldName(fieldName))
                    .append(" LIKE '")
                    .append(value.replace("'", "''"))  // Escape single quotes
                    .append("'");
        });
    }

    private void parseSort(JsonNode sortNode) {
        if (sortNode == null || !sortNode.isArray() || sortNode.isEmpty()) return;

        List<String> sortClauses = new ArrayList<>();
        sortNode.forEach(sort -> {
            if (sort.isObject()) {
                sort.fields().forEachRemaining(field -> {
                    String fieldName = field.getKey();
                    String direction = field.getValue().asText().equalsIgnoreCase("desc") ? "DESC" : "ASC";
                    sortClauses.add(convertFieldName(fieldName) + " " + direction);
                });
            }
        });

        if (!sortClauses.isEmpty()) {
            sqlBuilder.append("\nORDER BY ")
                    .append(String.join(", ", sortClauses));
        }
    }

    private void parseLimit(JsonNode sizeNode, JsonNode fromNode) {
        int size = sizeNode != null ? sizeNode.asInt() : 10;
        int from = fromNode != null ? fromNode.asInt() : 0;

        sqlBuilder.append("\nLIMIT ").append(size);
        if (from > 0) {
            sqlBuilder.append(" OFFSET ").append(from);
        }
    }

    private String convertFieldName(String elasticsearchField) {
        return elasticsearchField.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static class ParseResult {
        private final String sql;
        private final String whereSql;
        private final List<String> parameters;
        private final String queryStr;

        public ParseResult(String sql, List<String> parameters, String queryStr) {
            this.sql = sql;
            this.whereSql = extractWhereClause(sql);
            this.parameters = parameters;
            this.queryStr = queryStr;
        }

        public String getSql() {
            return sql;
        }

        public String getWhereSql() {
            return whereSql;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public String getQueryStr() {
            return queryStr;
        }
    }


    public static String extractWhereClause(String sql) {
        // 将SQL转换为大写以便处理，但保留原始的值
        String upperCaseSql = sql.toUpperCase();
        String originalSql = sql;

        // 查找 WHERE 关键字的位置
        int whereIndex = upperCaseSql.indexOf("WHERE");
        if (whereIndex == -1) {
            return "No WHERE clause found";
        }

        // WHERE 后面的起始位置（跳过"WHERE"关键字及其后的空格）
        int startIndex = whereIndex + "WHERE".length();
        while (startIndex < originalSql.length() &&
                Character.isWhitespace(originalSql.charAt(startIndex))) {
            startIndex++;
        }

        // 找出限制性子句的位置（GROUP BY, ORDER BY, LIMIT等）
        int limitIndex = upperCaseSql.indexOf("LIMIT", whereIndex);
        int groupByIndex = upperCaseSql.indexOf("GROUP BY", whereIndex);
        int orderByIndex = upperCaseSql.indexOf("ORDER BY", whereIndex);

        // 找出最早出现的限制性子句
        int endIndex = originalSql.length();
        if (limitIndex != -1) endIndex = Math.min(endIndex, limitIndex);
        if (groupByIndex != -1) endIndex = Math.min(endIndex, groupByIndex);
        if (orderByIndex != -1) endIndex = Math.min(endIndex, orderByIndex);

        // 提取WHERE子句（不包含WHERE关键字）
        return originalSql.substring(startIndex, endIndex).trim();
    }


    /**
     * 解析 es dsl请求
     *
     * @param input
     * @return
     */
    public static EsExtractResult extract(String input) {
        // 更简单的正则表达式模式
        String tablePattern = "/([^/]+)/";  // 匹配两个斜杠之间的内容
        String jsonPattern = "\\{.*\\}";    // 匹配从第一个{到最后一个}的所有内容

        // 提取表名
        Pattern tableRegex = Pattern.compile(tablePattern);
        Matcher tableMatcher = tableRegex.matcher(input);
        String tableName = null;
        if (tableMatcher.find()) {
            tableName = tableMatcher.group(1).split("_")[0]; // 分割并获取第一部分
        }

        // 提取JSON
        Pattern jsonRegex = Pattern.compile(jsonPattern, Pattern.DOTALL); // DOTALL模式允许.匹配换行
        Matcher jsonMatcher = jsonRegex.matcher(input);
        String jsonBody = null;
        if (jsonMatcher.find()) {
            jsonBody = jsonMatcher.group();
            System.out.println("JSON体: " + jsonBody);
        }

        return new EsExtractResult(tableName, jsonBody);
    }



    /**
     * Helper method to extract value from term query node
     * Handles different value formats and types
     */
    private Object extractTermValue(JsonNode valueNode) {
        if (valueNode == null || valueNode.isNull()) {
            return null;
        }

        // Handle object with "value" field
        if (valueNode.isObject() && valueNode.has("value")) {
            valueNode = valueNode.get("value");
        }

        // Extract value based on type
        if (valueNode.isTextual()) {
            String text = valueNode.asText();
            return text.isEmpty() ? null : text;
        }
        else if (valueNode.isNumber()) {
            return valueNode.numberValue();
        }
        else if (valueNode.isBoolean()) {
            return valueNode.booleanValue();
        }
        else if (valueNode.isNull()) {
            return null;
        }

        // Default to string representation
        String text = valueNode.asText();
        return text.isEmpty() ? null : text;
    }

    /**
     * Helper method to format value for SQL inclusion
     * Handles different value types appropriately
     */
    private String formatValueForSql(Object value) {
        if (value == null) {
            return "NULL";
        }

        if (value instanceof Number) {
            return value.toString();
        }
        else if (value instanceof Boolean) {
            return value.toString();
        }
        else {
            return "'" + value.toString().replace("'", "''") + "'";
        }
    }

    /**
     * Helper method to append term condition to SQL builder
     */
    private void appendTermCondition(String fieldName, Object value) {
        sqlBuilder.append(convertFieldName(fieldName));

        if (value == null) {
            sqlBuilder.append(" IS NULL");
        } else {
            sqlBuilder.append(" = ").append(formatValueForSql(value));
        }
    }
}
