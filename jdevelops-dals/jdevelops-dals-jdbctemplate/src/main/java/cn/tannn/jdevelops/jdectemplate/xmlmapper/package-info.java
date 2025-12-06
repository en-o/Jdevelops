/**
 * xml sql ，模拟  mybatis  的 xml mapper 文件解析进行sql的操作
 * <p> 比 链式写法更加清晰，便于sql审查</p>
 * <p> 通过 @XmlMapperScan 进行 @XmlMapper类查找和注册 </p>
 * <p> 通过 @XmlInsert  @XmlDelete @XmlSelect @XmlUpdate 查找xml中的执行语句</p>
 * <p> 通过 XmlMapperRegistry 管理所有的 XML Mapper 和 SQL 执行 </p>
 * <p> 通过 XmlSqlExecutor 进行sql执行 </p>
 */
package cn.tannn.jdevelops.jdectemplate.xmlmapper;
