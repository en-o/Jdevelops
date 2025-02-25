grammar ES;

query: expression EOF;

expression
    : expression ('AND'|'and') expression    # AndExpression
    | expression ('OR'|'or') expression      # OrExpression
    | '(' expression ')'                     # ParenthesizedExpression
    | comparison                             # ComparisonExpression
    ;

comparison
    : IDENTIFIER operator valueType          # StandardComparison
    | IDENTIFIER existsOperator              # ExistenceComparison
    ;

// 显式定义 exists 操作符
existsOperator
    : EXISTS                                # ExistsOp
    | NOT EXISTS                            # NotExistsOp
    ;

// 扩展运算符支持
operator
    : '==' | '!=' | '>=' | '<=' | '>' | '<' | '+='  // 基础运算符
    | '=~' | '!~'                                    // 正则表达式匹配/不匹配
    | 'in' | 'not in'                               // 包含/不包含
    ;

EXISTS: 'exists';
NOT: 'not';

// 标识符（字段名）
IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;

// 值类型
valueType
    : DOUBLE_QUOTED_STRING                # QuotedStringValue
    | SINGLE_QUOTED_STRING                # SingleQuotedStringValue
    | BARE_STRING                         # BareStringValue
    | INT                                 # IntValue
    | DECIMAL                            # DecimalValue
    | arrayValue                         # ArrayValues
    | 'null'                             # NullValue
    ;

// 数组值
arrayValue
    : ARRAY_START value (COMMA value)* ARRAY_END
    ;

// 数组元素值
value
    : DOUBLE_QUOTED_STRING
    | SINGLE_QUOTED_STRING
    | BARE_STRING
    | INT
    | DECIMAL
    ;

// 词法规则
DOUBLE_QUOTED_STRING: '"' (~["\r\n])* '"';
SINGLE_QUOTED_STRING: '\'' (~['\r\n])* '\'';
BARE_STRING: [a-zA-Z0-9@.+\-/%_]+;
INT: [0-9]+;
DECIMAL: INT '.' INT;
ARRAY_START: '[';
ARRAY_END: ']';
COMMA: ',';

// 忽略空白字符
WS: [ \t\r\n]+ -> skip;
