/**
 * 在基于 RESTful 架构的 Web 应用开发中，不同的业务场景对数据的字段需求不同。
 * 使用 @JsonView 可以通过定义多个视图类，并在实体类字段上标注该注解，
 * 决定该字段在哪些视图中可见。
 *
 * 要使 @JsonView 生效，必须在 Controller 的返回方法上显式指定 @JsonView，
 * 否则将忽略字段上的视图限制，返回所有字段。
 *
 * 注意：@JsonView 仅影响序列化（输出 JSON），不影响 toString()、日志打印或其他用途。
 *
 * <pre>
   public class User {
       &#64;JsonView(View.Detailed.class)
       private String password;
       private String name; // 没有视图注解，默认在所有视图中都可见
   }

   // ✅ 正确使用 JsonView
   &#64;GetMapping("/users/detailed")
   &#64;JsonView(View.Detailed.class)
   public List<User> getAllUsersDetailed() {
       // 返回的将仅包含字段：
       // - 被 @JsonView(View.Detailed.class) 标注的字段
       // - 没有标注 @JsonView 的字段（总是默认显示）
   }

   // ❌ 没有使用 JsonView（视图机制不会生效）
   &#64;GetMapping("/users")
   public List<User> getAllUsers() {
       // 将返回所有字段，无论是否标注了 @JsonView
   }
 * </pre>
 */
package cn.tannn.jdevelops.result.views;
