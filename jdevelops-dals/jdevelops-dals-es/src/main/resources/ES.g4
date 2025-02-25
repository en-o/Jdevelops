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
    : STRING                              # StringValue
    | NUMBER                             # NumberValue
    | arrayValue                         # ArrayValues
    | 'null'                             # NullValue
    ;

arrayValue
    : '[' value (',' value)* ']'
    ;

value
    : STRING
    | NUMBER
    ;

fragment DIGIT: [0-9];
NUMBER: DIGIT+ ('.' DIGIT+)?;

// 修改STRING词法规则，统一处理带引号和不带引号的字符串
STRING
    : QUOTED_STRING     // 带引号的字符串
    | UNQUOTED_STRING   // 不带引号的字符串
    ;

fragment QUOTED_STRING
    : '"' (~["\r\n])* '"'
    | '\'' (~['\r\n])* '\''
    ;

fragment UNQUOTED_STRING
    : ~[ \t\r\n"'()[\],<>=!+~]+
    ;

// 忽略空白字符
WS: [ \t\r\n]+ -> skip;
