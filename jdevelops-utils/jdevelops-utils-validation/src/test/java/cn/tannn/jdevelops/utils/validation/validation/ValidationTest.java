package cn.tannn.jdevelops.utils.validation.validation;


import cn.tannn.jdevelops.utils.validation.BeastValidatedTest;
import cn.tannn.jdevelops.utils.validation.model.ValidationUserBean;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试 参数检验
 */
public class ValidationTest extends BeastValidatedTest {



    @Test
    public void iphoneTest() {
        ValidationUserBean validationUserBean = new ValidationUserBean();
        validationUserBean.setIphone("13422381140");
        Set<ConstraintViolation<ValidationUserBean>> validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setIphone("13422381");
        validates.forEach(validate -> assertEquals("请输入正确的手机号码格式", validate.getMessage()));

        validationUserBean.setIphone("134223811411");
        validates.forEach(validate -> assertEquals("请输入正确的手机号码格式", validate.getMessage()));
    }


    @Test
    public void idCardTest() {
        ValidationUserBean validationUserBean = new ValidationUserBean();
        validationUserBean.setIdCard("200330196512016158");
        Set<ConstraintViolation<ValidationUserBean>> validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setIdCard("20033019651201615x");
        validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setIdCard("2003301965120161");
        validates.forEach(validate -> {
            assertEquals("请输入正确的身份证号", validate.getMessage());
        });

        validationUserBean.setIdCard("200330196512016158xa");
        validates.forEach(validate -> assertEquals("请输入正确的身份证号", validate.getMessage()));
    }


    @Test
    public void cnameTest() {
        ValidationUserBean validationUserBean = new ValidationUserBean();
        Set<ConstraintViolation<ValidationUserBean>> validates = validate(validationUserBean);

        validationUserBean.setCname("贪闲");
        validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setCname("贪闲贪闲贪闲贪闲贪闲贪闲贪闲贪闲");
        validates = validate(validationUserBean);
        assertEquals(0, validates.size());



        validationUserBean.setCname("贪");
        validates.forEach(validate -> assertEquals("请输入正确的中文姓名", validate.getMessage()));

        validationUserBean.setCname("贪闲贪闲贪闲贪闲贪闲贪闲贪闲贪闲贪");
        validates.forEach(validate -> assertEquals("请输入正确的中文姓名", validate.getMessage()));
    }
    @Test
    public void dateTimeTest() {
        ValidationUserBean validationUserBean = new ValidationUserBean();
        validationUserBean.setDateTime("2023-06-13 11:55:58");
        Set<ConstraintViolation<ValidationUserBean>> validates = validate(validationUserBean);
        assertEquals(0, validates.size());


        validationUserBean.setDateTime("2023-06-13");
        validates = validate(validationUserBean);
        validates.forEach(validate -> assertEquals("时间格式不合法", validate.getMessage()));

    }


    @Test
    public void passwordTest() {
        ValidationUserBean validationUserBean = new ValidationUserBean();
        validationUserBean.setPassword("Ax1$aa");
        Set<ConstraintViolation<ValidationUserBean>> validates = validate(validationUserBean);
        assertEquals(0, validates.size());


        validationUserBean.setPassword("Ax1$aa1111111111");
        validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setPassword("aaaaa1");
        validates = validate(validationUserBean);
        validates.forEach(validate -> assertEquals("密码不合法，密码必须有6-16个字符，必须包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符(!@#$%^&*?)", validate.getMessage()));

        validationUserBean.setPassword("1AAAAA");
        validates = validate(validationUserBean);
        validates.forEach(validate -> assertEquals("密码不合法，密码必须有6-16个字符，必须包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符(!@#$%^&*?)", validate.getMessage()));
        validationUserBean.setPassword("1Ad*12");
        validates = validate(validationUserBean);
        validates.forEach(validate -> assertEquals("密码不合法，密码必须有6-16个字符，必须包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符(!@#$%^&*?)", validate.getMessage()));

        validationUserBean.setPassword("!@#$%^");
        validates = validate(validationUserBean);
        validates.forEach(validate -> assertEquals("密码不合法，密码必须有6-16个字符，必须包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符(!@#$%^&*?)", validate.getMessage()));

        validationUserBean.setPassword("$%^&*?");
        validates = validate(validationUserBean);
        validates.forEach(validate -> assertEquals("密码不合法，密码必须有6-16个字符，必须包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符(!@#$%^&*?)", validate.getMessage()));

        validationUserBean.setPassword("Ax1$a");
        validates = validate(validationUserBean);
        validates.forEach(validate -> assertEquals("密码不合法，密码必须有6-16个字符，必须包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符(!@#$%^&*?)", validate.getMessage()));

        validationUserBean.setPassword("Ax1$aa11111111111");
        validates = validate(validationUserBean);
        validates.forEach(validate -> assertEquals("密码不合法，密码必须有6-16个字符，必须包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符(!@#$%^&*?)", validate.getMessage()));

    }

    @Test
    public void accountTest() {
        ValidationUserBean validationUserBean = new ValidationUserBean();
        validationUserBean.setAccount("TANNN");
        Set<ConstraintViolation<ValidationUserBean>> validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setAccount("TANNNN");
        validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setAccount("TATATATATATATATA");
        validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setAccount("TATATATA_ATATATA");
        validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setAccount("TATATATA_12ATATA");
        validates = validate(validationUserBean);
        assertEquals(0, validates.size());

        validationUserBean.setAccount("TATATA贪闲_12ATATA");
        validates = validate(validationUserBean);
        validates.forEach(validate -> assertEquals("账号不合法，请输入以字母开头，允许字母数字下划线组合，且不少于5个不大于16个", validate.getMessage()));


        validationUserBean.setAccount("TANN");
        validates.forEach(validate -> assertEquals("账号不合法，请输入以字母开头，允许字母数字下划线组合，且不少于5个不大于16个", validate.getMessage()));

        validationUserBean.setAccount("TATATATA_ATATATAA");
        validates.forEach(validate -> assertEquals("账号不合法，请输入以字母开头，允许字母数字下划线组合，且不少于5个不大于16个", validate.getMessage()));

        validationUserBean.setAccount("TATATATA_-2ATATA");
        validates.forEach(validate -> assertEquals("账号不合法，请输入以字母开头，允许字母数字下划线组合，且不少于5个不大于16个", validate.getMessage()));

        validationUserBean.setAccount("TATATATA&#2ATATA");
        validates.forEach(validate -> assertEquals("账号不合法，请输入以字母开头，允许字母数字下划线组合，且不少于5个不大于16个", validate.getMessage()));
    }
}
