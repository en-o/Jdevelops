package cn.tannn.jdevelops.jpa.constant;


/**
 * 自定义扩展 root
 * <p> 使用示例 @Query("select o from User o where o.gender = :#{woman} ")
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/11/16 14:36
 */
public class CommonStateExpressionRoot {

    /**
     * 性别等于 未知
     */
    public final Integer gender = 0;
    /**
     * 男性
     */
    public final Integer man = 1;
    /**
     * 女性
     */
    public final Integer woman = 2;

    /**
     * true
     */
    public final Integer truee = 1;

    /**
     * false
     */
    public final Integer falsee = 0;


    ////  用户状态

    /**
     * 正常
     */
    public final Integer normal = 1;

    /**
     * 锁定[违规]
     */
    public final Integer banned = 2;

    /**
     * 删除[禁用]
     */
    public final Integer del = 3;

}
