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
// 第一行基础运算符 ps :  += = like(text) , =+ -> like(keyword)
// 第二行正则表达式匹配/不匹配
// 第三行包含/不包含
operator
    : '==' | '!=' | '>=' | '<=' | '>' | '<' | '+=' | '=+'
    | '=~' | '!~'
    | IN                                    # InOp
    | NOTIN                                 # NotInOp
    ;

// 关键字定义（支持大小写）
EXISTS: [Ee][Xx][Ii][Ss][Tt][Ss];
NOT: [Nn][Oo][Tt];
IN: [Ii][Nn];
NOTIN: [Nn][Oo][Tt][ ]+[Ii][Nn];

// 修改IDENTIFIER支持中文字符
IDENTIFIER: (CHINESE | [a-zA-Z_]) (CHINESE | [a-zA-Z0-9_])*;

// 添加中文字符片段规则
fragment CHINESE: [\u4e00-\u9fa5];

// 值类型处理器
valueType
    : quotedString                         # QuotedStringValue
    | unquotedString                       # UnquotedStringValue
    | INT                                  # IntValue
    | DECIMAL                             # DecimalValue
    | arrayValue                          # ArrayValues
    | 'null'                              # NullValue
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
// 修改UNQUOTED_STRING支持中文字符
UNQUOTED_STRING: (CHINESE | [a-zA-Z0-9@.+\-/%_])+;
INT: [0-9]+;
DECIMAL: INT '.' INT;

// 忽略空白字符
WS: [ \t\r\n]+ -> skip;
