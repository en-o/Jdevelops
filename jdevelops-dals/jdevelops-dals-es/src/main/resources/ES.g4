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
// 值类型处理器
valueType
    : quotedString                         # QuotedStringValue
    | unquotedString                       # UnquotedStringValue
    | INT                                  # IntValue
    | DECIMAL                              # DecimalValue
    | arrayValue                           # ArrayValues
    | 'null'                               # NullValue
    ;

quotedString
    : DOUBLE_QUOTED_STRING
    | SINGLE_QUOTED_STRING
    ;

unquotedString
    : UNQUOTED_STRING
    ;

arrayValue
    : '[' value (',' value)* ']'
    ;

value
    : quotedString
    | unquotedString
    | INT
    | DECIMAL
    ;

// 词法规则
DOUBLE_QUOTED_STRING: '"' (~["\r\n])* '"';
SINGLE_QUOTED_STRING: '\'' (~['\r\n])* '\'';
UNQUOTED_STRING: [a-zA-Z0-9@.+\-/%_]+;
INT: [0-9]+;
DECIMAL: INT '.' INT;

WS: [ \t\r\n]+ -> skip;
