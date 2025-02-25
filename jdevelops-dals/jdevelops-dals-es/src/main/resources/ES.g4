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
    | IDENTIFIER ('exists'|'not exists')     # ExistenceComparison
    ;

// 扩展运算符支持
operator
    : '==' | '!=' | '>=' | '<=' | '>' | '<' | '+='  // 基础运算符
    | '=~' | '!~'                                    // 正则表达式匹配/不匹配
    | 'in' | 'not in'                               // 包含/不包含
    ;

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
STRING: '"' ('\\' . | ~["\r\n])* '"';
INT: [0-9]+;

// 修改数组的定义
ARRAY_START: '[';
ARRAY_END: ']';
COMMA: ',';

valueType
    : STRING                                # StringValue
    | INT                                   # IntValue
    | arrayValue                           # ArrayValues
    | 'null'                               # NullValue
    ;

arrayValue
    : ARRAY_START (STRING|INT) (COMMA (STRING|INT))* ARRAY_END
    ;

// 忽略空白字符
WS: [ \t\r\n]+ -> skip;
