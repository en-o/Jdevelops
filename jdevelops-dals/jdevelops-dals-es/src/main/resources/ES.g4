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

// 修改字符串规则以支持 email 地址等特殊格式
STRING
    : '"' ('\\' . | ~["\r\n])* '"'                   // 标准字符串
    | '\'' ('\\' . | ~['\r\\n])* '\''                 // 单引号字符串
    | [a-zA-Z0-9._%+-]+('@')[a-zA-Z0-9.-]+('.')[a-zA-Z]{2,}  // email 格式
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
