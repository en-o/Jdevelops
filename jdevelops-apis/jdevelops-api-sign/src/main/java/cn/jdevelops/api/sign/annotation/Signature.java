package cn.jdevelops.api.sign.annotation;


import cn.jdevelops.api.sign.enums.SginEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *  MD5加密： 参数类型:  Map bean String   (json 跟 普通传参都可以)
 *          禁止list 禁止jsonArray 禁止套娃
 *          如果参数中非要有list，jsonArray 和套娃 那就加密的时候不要他
 * 推荐加密网站： https://www.sojson.com/encrypt_md5.html 因为他的参数组合跟我的是一样的 AopMapUtil.map2Str
 * 工具使用
 *          参数转型: AopMapUtil.map2Str (规则为: key=value&key=value)
 *          接口加密: SignMD5Util.encrypt(str,true)
 *
 *
 *  签名方式MD5     加密时注意顺序，验证时会判断顺序的一致性(参数为bean时可能会例外)
 *      sign(签名) = md5小写32位加密(
 *              md5小写32位加密(
 *                  params(key+value)
 *              )
 *              + MD5database(内置盐)
 *          )
 *      eg: MD5(age=1&name=谭宁) = 6c9adabf334133a3176dd689992bdef6
 *          MD5(6c9adabf334133a3176dd689992bdef6MD5database) = 5f6a191b3f07f7602ef15bc1b464d799
 *          sign = 5f6a191b3f07f7602ef15bc1b464d799
 *  注意两层加密
 *
 *
 *  MD5HEADER加密：参数 list map string bean jsonAarray jsonObject ,（注： get拼接方式跟list参数会强制验证数据顺序）
 *      签名方式MD5
 *              sign(签名) = md5小写32位加密(
 *                  md5小写32位加密(
 *                      params( fastJson 格式化)
 *                  )
 *                  + MD5database(内置盐)
 *              )
 *         eg:  sign(签名) 放到header中
 *              list （jsonArray）:
 *                  sign = md5小写32位加密(
 *                              md5小写32位加密(
 *                                    [{"age":"1","name":"谭宁"},{"age":"11","name":"谭宁1"},{"age":"11","name":"谭宁1"}]
 *                              )
 *                              + MD5database(内置盐)
 *                          )
 *                list （jsonObect map bean）:
 *                    sign = md5小写32位加密(
 *                                md5小写32位加密(
 *                                      {"age":"1","name":"谭宁"}
 *                                )
 *                                + MD5database(内置盐)
 *                           )
 *
 *
 *
 *  接口测试参考: jdevelops-demo -> controller-sign-demo -> test -> cn.jdevelops.sign.controller
 *
 * </pre>
 *
 *  接口签名
 * @author tn
 * @date  2020/6/9 14:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Signature {
    /*默认不需要签名*/
    SginEnum type() default SginEnum.ANY;
}
