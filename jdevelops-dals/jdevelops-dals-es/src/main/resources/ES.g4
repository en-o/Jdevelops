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
    | IDENTIFIER existsOperator             # ExistenceComparison
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

// 修改标识符规则以支持更多字符
IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;

// 简化的字符串规则
STRING
    : '"' (~["\r\n])* '"'     // 双引号字符串
    | '\'' (~['\r\n])* '\''   // 单引号字符串
    | UNQUOTED_STRING         // 无引号字符串（包括email等格式）
    ;

// 定义无引号字符串（可以包含email等格式）
fragment UNQUOTED_STRING
    : (~[ \t\r\n"'()[\],] | '@' | '.' | '-' | '+' | '%')+
    ;

INT: [0-9]+;
DECIMAL: INT '.' INT;

// 数组相关
ARRAY_START: '[';
ARRAY_END: ']';
COMMA: ',';

valueType
    : STRING                                # StringValue
    | INT                                   # IntValue
    | DECIMAL                              # DecimalValue
    | arrayValue                           # ArrayValues
    | 'null'                               # NullValue
    ;

arrayValue
    : ARRAY_START (STRING|INT|DECIMAL) (COMMA (STRING|INT|DECIMAL))* ARRAY_END
    ;

// 忽略空白字符
WS: [ \t\r\n]+ -> skip;
