grammar ES;

query: expression EOF;

expression
    : expression ('AND'|'and') expression    # AndExpression
    | expression ('OR'|'or') expression      # OrExpression
    | '(' expression ')'                     # ParenthesizedExpression
    | comparison                             # ComparisonExpression
    ;

comparison
    : IDENTIFIER op=operator value=valueType
    ;

// 扩展运算符支持 += like
operator
    : '==' | '!=' | '>=' | '<=' | '>' | '<' | '+='  // 基础运算符
    | '=~' | '!~'                                    // 正则表达式匹配/不匹配
    | 'in' | 'not in'                               // 包含/不包含
    | 'exists' | 'not exists'                       // 存在/不存在
    ;

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
STRING    : '"' ('\\' . | ~["\r\n])* '"';
INT       : [0-9]+;
ARRAY     : '[' (STRING|INT) (',' (STRING|INT))* ']';

valueType
    : STRING
    | INT
    | ARRAY
    | 'null'
    ;

// 忽略空白字符
WS        : [ \t\r\n]+ -> skip;
