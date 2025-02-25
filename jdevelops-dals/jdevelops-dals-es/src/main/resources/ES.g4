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
// 第一行基础运算符 ps :  += = like
// 第二行正则表达式匹配/不匹配
// 第三行包含/不包含
operator
    : '==' | '!=' | '>=' | '<=' | '>' | '<' | '+='
    | '=~' | '!~'
    | 'in' | 'not in'
    ;

EXISTS: 'exists';
NOT: 'not';

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;

valueType
    : DOUBLE_QUOTED_STRING                # QuotedStringValue
    | SINGLE_QUOTED_STRING                # SingleQuotedStringValue
    | BARE_STRING                         # BareStringValue
    | INT                                 # IntValue
    | DECIMAL                            # DecimalValue
    | arrayValue                         # ArrayValues
    | 'null'                             # NullValue
    ;

arrayValue
    : ARRAY_START value (COMMA value)* ARRAY_END
    ;

value
    : DOUBLE_QUOTED_STRING
    | SINGLE_QUOTED_STRING
    | BARE_STRING
    | INT
    | DECIMAL
    ;

//词法规则，让BARE_STRING支持更多字符
DOUBLE_QUOTED_STRING: '"' (~["\r\n])* '"';
SINGLE_QUOTED_STRING: '\'' (~['\r\n])* '\'';
// BARE_STRING的定义，允许更多字符，但排除空格和特殊操作符
BARE_STRING: ~[ \t\r\n"'()[\],<>=!+~]+;
INT: [0-9]+;
DECIMAL: INT '.' INT;
ARRAY_START: '[';
ARRAY_END: ']';
COMMA: ',';

// 忽略空白字符
WS: [ \t\r\n]+ -> skip;
