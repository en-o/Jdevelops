package cn.jdevelops.sboot.authentication.jredis.util;

import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/7 18:03
 */
public final class UserRoleUtil {
    public UserRoleUtil() {
        throw new AssertionError("No cn.jdevelops.sboot.authentication.jredis.util.UserRoleUtil instances for you!");
    }


    /**
     * 判断是否拥有角色
     *
     * @param userRoles 用户拥有的角色
     * @param apiRoles  接口需要的角色
     * @return true  拥有
     */
    public static boolean verifyRoles(List<String> userRoles, String[] apiRoles) {
        // 如果接口角色为空，则表示不需要角色就能使用此接口
        if (apiRoles == null ) {
            return true;
        }
        // 如果用户的角色为空，但是接口需要 那就返回 false
        if(userRoles == null || userRoles.isEmpty()){
            return false;
        }
        for (String apiRole : apiRoles) {
            if (null == apiRole || apiRole.trim().isEmpty()) {
                continue;
            }
            if (userRoles.contains(apiRole)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否拥有权限(接口url)
     *
     * @param userPermissions 用户拥有的权限(接口url)
     * @param apiPermission  接口需要的权限(接口url)
     * @return true 拥有
     */
    public static boolean verifyPermissions(List<String> userPermissions, String apiPermission) {
        // 如果接口权限为空，则表示不需要权限
        if (apiPermission == null || apiPermission.isEmpty()) {
            return true;
        }
        // 如果用户的角色为空，但是接口需要 那就返回 false
        if(userPermissions == null || userPermissions.isEmpty()){
            return false;
        }
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return userPermissions.stream().anyMatch(e -> antPathMatcher.match(e, apiPermission));
    }
}
