package cn.jdevelops.jredis.entity;

import cn.jdevelops.jredis.entity.base.BasicsAccount;
import lombok.*;

/**
 * 用户信息 存redis的
 * @author tomsun28
 * @date 16:20 2019-05-19
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisAccount<T> extends BasicsAccount {

    /** 用户其他数据 **/
    private T userInfo;


}
