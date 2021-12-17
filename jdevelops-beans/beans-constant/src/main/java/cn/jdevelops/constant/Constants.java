package cn.jdevelops.constant;

/**
 * 公共常量
 *
 * @author tn
 * @version 1
 * @date 2021-12-17 11:25
 */
public interface Constants {

    /**
     * The constant COLONS.
     */
    String COLONS = ":";

    /**
     * The constant SUCCESS.
     */
    String SUCCESS = "success";

    /**
     * The constant DECODE.
     */
    String DECODE = "UTF-8";

    /**
     * The constant SIGN.
     */
    String SIGN = "sign";

    /**
     * The constant PATH.
     */
    String PATH = "path";

    /**
     * The constant VERSION.
     */
    String VERSION = "version";

    /**
     * The constant TIMESTAMP.
     */
    String TIMESTAMP = "timestamp";

    /**
     * The constant URI.
     */
    String URI = "uri";

    /**
     * The constant URL_SEPARATOR.
     */
    String PATH_SEPARATOR = "/";

    /**
     * The constant FILE_SEPARATOR.
     */
    String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * The constant TIME_OUT.
     */
    long TIME_OUT = 3000;

    /**
     * The constant secretKey.
     */
    String SECRET_KEY = "secretKey";

    /**
     * The constant secret
     */
    String SECRET = "secret";

    /**
     * is checked.
     */
    String IS_CHECKED = "checked";

    /**
     * default checked value.
     */
    String DEFAULT_CHECK_VALUE = "false";

    /**
     * 性别男
     */
    Integer SEX_MAN = 1;

    /**
     * 性别 女
     */
    Integer SEX_WOMAN = 2;

    /**
     * 性别 未知
     */
    Integer SEX_UNKNOWN = 0;

    /**
     * the empty json.
     */
    String EMPTY_JSON = "{}";

    /**
     * App：表示在 App 里启动刷脸
     */
    String FROM_APP = "App";

    /**
     * browser：表示在浏览器启动刷脸
     */
    String FROM_BROWSER = "browser";

    /**
     * SIGN 类型，必须缓存在磁盘，并定时刷新
     */
    String TICKET_TYPE_SIGN = "SIGN";

    /**
     * 其有效期为120秒，且一次性有效，即每次启动 SDK 刷脸都要重新请求 NONCE ticket。
     */
    String TICKET_TYPE_NONCE = "NONCE";

    /**
     * 默认超级用户登录登录名称
     */
    String LOGIN_ADMIN = "admin";

    /**
     * 默认角色：系统最高权限角色（能看到全校的数据）
     */
    String ROLE_ADMIN = "role_admin";

    /**
     * MediaType.parse("application/json; charset=utf-8");
     */
    String  APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

}
