antlr的g4文件示例:```

``` 
antlr的Visitor示例：```
``` 
工具类示例：```

``` 
单元测试示例：```

class ElasticSearchQueryBuilderTest {

}
````
开发环境：java17, elasticsearch7.16，org.elasticsearch.client:elasticsearch-rest-client:8.6.2，co.elastic.clients:elasticsearch-java:8.6.2，org.antlr:antlr4-runtime:4.13.1，org.junit.jupiter:junit.jupiter:5.9.2

根据上述示例和环境进行代码优化，优化项如下：
1. 根据java17的风格进行代码优化
2. 新增除了`'==' | '!=' | '>=' | '<=' | '>' | '<' | '+='` 之外的其他es相关功能
3. 通过设计模式进行field的替换丰富功能，如 `abc=10086 替换成 phone=10086`
4. 通过设计摸对 value 进行过滤，如 sex的值只能是男和女，当`sex=未知`是进行错误抛出
5. 实现es正则表达式查询


根据上述示例和环境进行代码优化，优化项如下：
1. 实现逻辑运算：AND，OR，NOT分别对应 es的 must，should，must_not查询
2. 实现比较/匹配运算：BETWEEN,>,>=,<=,<,=,%,%= , 其中每个符号的具体使用query我自己处理。
3. 同时判断某些比较/匹配运算的value是否为指定的集合，如果为指定的集合则用指定的query查询
