**You:**

RedisTemplate 所有api详细解析

* * *

**ChatGPT:**

`RedisTemplate` 是 Spring Data Redis 提供的一个类，它封装了与 Redis 交互的常用操作。`RedisTemplate` 提供了丰富的 API，用于对 Redis 进行各种类型的数据操作，包括字符串、哈希、列表、集合、有序集合等。以下是 `RedisTemplate` 的详细 API 解析：
## API基础说明

### 1. 基础操作
> https://redis.io/docs/latest/commands/ttl/

#### 设置值和获取值

* `void opsForValue().set(String key, String value)`：设置指定键的值。
* `String opsForValue().get(String key)`：获取指定键的值。

```java
redisTemplate.opsForValue().set("key1", "value1");
String value = redisTemplate.opsForValue().get("key1");
```

#### 删除值

* `Boolean delete(String key)`：删除指定键的值。
* `Long delete(Collection<String> keys)`：删除多个键的值。

```java
redisTemplate.delete("key1");
redisTemplate.delete(Arrays.asList("key1", "key2"));
```

#### 检查键是否存在

* `Boolean hasKey(String key)`：检查指定键是否存在。

```java
Boolean exists = redisTemplate.hasKey("key1");
```

### 2. 字符串操作

#### 追加值

* `Integer append(String key, String value)`：在指定键的值后追加值。

```java
redisTemplate.opsForValue().append("key1", "appendValue");
```

#### 获取部分字符串

* `String get(String key, long start, long end)`：获取指定键的值的子字符串。

```java
String substring = redisTemplate.opsForValue().get("key1", 0, 5);
```

#### 设置并返回旧值

* `String getAndSet(String key, String value)`：设置新值并返回旧值。

```java
String oldValue = redisTemplate.opsForValue().getAndSet("key1", "newValue");
```

### 3. 哈希操作

#### 设置哈希值

* `void put(String key, String hashKey, String value)`：在哈希中设置键值对。
* `void putAll(String key, Map<String, String> map)`：在哈希中设置多个键值对。

```java
redisTemplate.opsForHash().put("hash1", "field1", "value1");
Map<String, String> map = new HashMap<>();
map.put("field2", "value2");
map.put("field3", "value3");
redisTemplate.opsForHash().putAll("hash1", map);
```

#### 获取哈希值

* `String get(String key, String hashKey)`：获取哈希中的指定字段值。
* `Map<Object, Object> entries(String key)`：获取哈希中的所有键值对。

```java
String hashValue = redisTemplate.opsForHash().get("hash1", "field1");
Map<Object, Object> entries = redisTemplate.opsForHash().entries("hash1");
```

### 4. 列表操作

#### 添加列表值

* `void leftPush(String key, String value)`：在列表左边添加值。
* `void rightPush(String key, String value)`：在列表右边添加值。

```java
redisTemplate.opsForList().leftPush("list1", "leftValue");
redisTemplate.opsForList().rightPush("list1", "rightValue");
```

#### 获取列表值

* `String leftPop(String key)`：从列表左边弹出值。
* `String rightPop(String key)`：从列表右边弹出值。
* `List<String> range(String key, long start, long end)`：获取列表中的部分元素。

```java
String leftValue = redisTemplate.opsForList().leftPop("list1");
String rightValue = redisTemplate.opsForList().rightPop("list1");
List<String> range = redisTemplate.opsForList().range("list1", 0, 1);
```

### 5. 集合操作

#### 添加集合值

* `Long add(String key, String... values)`：向集合添加一个或多个值。

```java
redisTemplate.opsForSet().add("set1", "value1", "value2");
```

#### 获取集合值

* `Set<String> members(String key)`：获取集合中的所有值。

```java
Set<String> setValues = redisTemplate.opsForSet().members("set1");
```

### 6. 有序集合操作

#### 添加有序集合值

* `Boolean add(String key, String value, double score)`：向有序集合添加值及其分数。

```java
redisTemplate.opsForZSet().add("zset1", "value1", 1.0);
```

#### 获取有序集合值

* `Set<String> range(String key, long start, long end)`：获取有序集合中指定范围的值。

```java
Set<String> zsetValues = redisTemplate.opsForZSet().range("zset1", 0, -1);
```

### 7. 其他操作

#### 获取过期时间

* `Long getExpire(String key)`：获取键的剩余过期时间。

```java
Long expire = redisTemplate.getExpire("key1");
```

#### 设置过期时间

* `Boolean expire(String key, long timeout, TimeUnit unit)`：设置键的过期时间。

```java
redisTemplate.expire("key1", 10, TimeUnit.MINUTES);
```

#### 获取随机值

* `String randomKey()`：获取一个随机键。
* `Set<String> keys(String pattern)`：获取匹配指定模式的所有键。

```java
String randomKey = redisTemplate.randomKey();
Set<String> keys = redisTemplate.keys("key*");
```

以上只是 `RedisTemplate` 提供的 API 的一部分，实际上它支持的操作非常多，可以满足大部分 Redis 使用场景。你可以参考 Spring Data Redis 的官方文档获取更详细的信息。


## RedisTemplate 中的 bound 和 ops 区别
> `RedisTemplate` 中的 `bound` 和 `ops` 是两种操作方式，分别用于简化和增强对 Redis 键值的操作管理。它们的区别主要体现在使用的灵活性和便捷性上。

### `ops` 操作

`ops` 提供了直接对 Redis 各种数据类型进行操作的 API，每一种数据类型（如字符串、哈希、列表、集合、有序集合）都有对应的操作方法。`ops` 是通用的，不绑定特定的键。

#### 示例代码

```java
// 对字符串操作
redisTemplate.opsForValue().set("key1", "value1");
String value = redisTemplate.opsForValue().get("key1");

// 对哈希操作
redisTemplate.opsForHash().put("hashKey", "field1", "value1");
String hashValue = redisTemplate.opsForHash().get("hashKey", "field1");

// 对列表操作
redisTemplate.opsForList().leftPush("listKey", "value1");
String listValue = redisTemplate.opsForList().leftPop("listKey");
```

### `bound` 操作

`bound` 提供了绑定到特定键的操作，简化了对某个特定键的多次操作。当你需要对同一个键进行多次操作时，`bound` 提供了一种更简洁的方式。这些方法返回特定类型的 `BoundKeyOperations`，如 `BoundValueOperations`、`BoundHashOperations`、`BoundListOperations`、`BoundSetOperations`、`BoundZSetOperations`。

#### 示例代码

```java
// 绑定到特定字符串键
BoundValueOperations<String, String> boundValueOps = redisTemplate.boundValueOps("key1");
boundValueOps.set("value1");
String value = boundValueOps.get();

// 绑定到特定哈希键
BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps("hashKey");
boundHashOps.put("field1", "value1");
String hashValue = (String) boundHashOps.get("field1");

// 绑定到特定列表键
BoundListOperations<String, String> boundListOps = redisTemplate.boundListOps("listKey");
boundListOps.leftPush("value1");
String listValue = boundListOps.leftPop();
```

### 区别总结

1. **使用方式**：

    * `ops`：需要在每次操作时指定键。
    * `bound`：绑定到特定键后，可以直接进行操作，无需每次指定键。
2. **适用场景**：

    * `ops`：适用于对多个不同键进行操作的场景。
    * `bound`：适用于对同一个键进行多次操作的场景，简化代码。
3. **简洁性**：

    * `bound` 操作更加简洁，当对一个键进行多次操作时，代码更清晰。

### 示例对比

#### 使用 `ops` 对同一个键进行多次操作

```java
redisTemplate.opsForValue().set("key1", "value1");
String value1 = redisTemplate.opsForValue().get("key1");
redisTemplate.opsForValue().append("key1", "value2");
String value2 = redisTemplate.opsForValue().get("key1");
```

#### 使用 `bound` 对同一个键进行多次操作

```java
BoundValueOperations<String, String> boundValueOps = redisTemplate.boundValueOps("key1");
boundValueOps.set("value1");
String value1 = boundValueOps.get();
boundValueOps.append("value2");
String value2 = boundValueOps.get();
```

在这种情况下，`bound` 操作明显更加简洁和直观。
