grammar ES_Upgrade;

query: expression EOF;

expression
    : expression ('AND'|'and') expression    # AndExpression
    | expression ('OR'|'or') expression      # OrExpression
    | NOT expression                         # NotExpression
    | '(' expression ')'                     # ParenthesizedExpression
    | comparison                             # ComparisonExpression
    ;

comparison
    : IDENTIFIER operator valueType          # StandardComparison
    | IDENTIFIER BETWEEN valueType AND valueType  # BetweenComparison
    | IDENTIFIER existsOperator              # ExistenceComparison
    ;

existsOperator
    : EXISTS                                # ExistsOp
    | NOT EXISTS                            # NotExistsOp
    ;

operator
    : '==' | '!=' | '>=' | '<=' | '>' | '<'
    | '%'  | '%='                           // 新增模糊匹配操作符
    | IN                                    # InOp
    | NOTIN                                 # NotInOp
    ;

// 关键字定义（支持大小写）
EXISTS: [Ee][Xx][Ii][Ss][Tt][Ss];
NOT: [Nn][Oo][Tt];
IN: [Ii][Nn];
NOTIN: [Nn][Oo][Tt][ ]+[Ii][Nn];
BETWEEN: [Bb][Ee][Tt][Ww][Ee][Ee][Nn];
AND: [Aa][Nn][Dd];

// 修改IDENTIFIER支持中文字符
IDENTIFIER: (CHINESE | [a-zA-Z_]) (CHINESE | [a-zA-Z0-9_])*;

// 添加中文字符片段规则
fragment CHINESE: [\u4e00-\u9fa5];

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

DOUBLE_QUOTED_STRING: '"' (~["\r\n])* '"';
SINGLE_QUOTED_STRING: '\'' (~['\r\n])* '\'';
UNQUOTED_STRING: (CHINESE | [a-zA-Z0-9@.+\-/%_])+;
INT: [0-9]+;
DECIMAL: INT '.' INT;

WS: [ \t\r\n]+ -> skip;
