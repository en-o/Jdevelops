grammar ES;

query: expression EOF;

expression
    : expression ('AND'|'and') expression    # AndExpression
    | expression ('OR'|'or') expression     # OrExpression
    | '(' expression ')'             # ParenthesizedExpression
    | comparison                     # ComparisonExpression
    ;

comparison
    : IDENTIFIER op=operator value=valueType
    ;

// += like
operator
    : '==' | '!=' | '>=' | '<=' | '>' | '<' | '+='
    ;

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
STRING    : '"' ('\\' . | ~["\r\n])* '"';
INT : [0-9]+;

valueType
    : STRING
    | INT
    ;





// 忽略空白字符
WS        : [ \t\r\n]+ -> skip;
