# 检索模块

## 1. elasticsearch检索


### 1.1 依赖添加
```maven
    <dependency>
       <groupId>cn.jdevelops</groupId>
       <artifactId>search-es</artifactId>
       <version>2.0.0</version>
    </dependency>
```

### 1.2 配置文件

```yaml
# elasticsearch配置
spring:
  elasticsearch:
    rest:
      #es节点地址，集群则用逗号隔开
      uris: 192.168.0.97:9201
      connection-timeout: 100 #连接超时
      max-connection: 100  #最大连接数
#      usernme: elastic       #如果你设置了基于x-pack的验证就要填写账号和密码
#      password: 123456       #没有则不用配置
```

### 1.3 使用例子

```java
	private EsSearchUtil esSearchUtil;

	private EsIndexUtil esIndexUtil;

	public ElasticSearchController(EsSearchUtil esSearchUtil, EsIndexUtil esIndexUtil) {
		this.esSearchUtil = esSearchUtil;
		this.esIndexUtil = esIndexUtil;
	}

	@PostMapping("search")
	public EsPageVO search(String index, Integer startPage, Integer pageSize) throws IOException {

		return esSearchUtil.search(Arrays.asList(index.split(",")), startPage, pageSize);
	}
```
