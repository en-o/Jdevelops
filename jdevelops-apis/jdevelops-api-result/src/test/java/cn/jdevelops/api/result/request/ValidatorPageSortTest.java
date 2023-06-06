package cn.jdevelops.api.result.request;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * 测试validation
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-06-06 21:51
 */
public class ValidatorPageSortTest {
    private Validator validator;
    private SortDTO sortDTO;
    private PageDTO pageDTO;


    @Before
    public void init() {
        sortDTO = new SortDTO();
        pageDTO = new PageDTO();
        // 获取验证器
        // 创建 Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();





    }


    @Test
    public void testOrderBySize() {
        // 非0表示存在异常
        Set<ConstraintViolation<SortDTO>> violations  = validator.validate(sortDTO);
        assertEquals(0,violations.size());

        sortDTO.setOrderBy("i1","i2","i3","i4","i5");
        violations  = validator.validate(sortDTO);
        assertEquals(0,violations.size());


        sortDTO.setOrderBy("i1","i2","i3","i4","i5","i6");
        violations  = validator.validate(sortDTO);
        assertEquals(1,violations.size());
    }


    @Test
    public void testOrderDesc() {
        // 非0表示存在异常
        Set<ConstraintViolation<SortDTO>> violations  = validator.validate(sortDTO);
        assertEquals(0,violations.size());

        sortDTO.setOrderDesc(1);
        violations  = validator.validate(sortDTO);
        assertEquals(0,violations.size());


        sortDTO.setOrderDesc(0);
        violations  = validator.validate(sortDTO);
        assertEquals(0,violations.size());


        sortDTO.setOrderDesc(-1);
        violations  = validator.validate(sortDTO);
        assertEquals(1,violations.size());

        sortDTO.setOrderDesc(2);
        violations  = validator.validate(sortDTO);
        assertEquals(1,violations.size());
    }



    @Test
    public void testPageIndex() {
        // 非0表示存在异常
        Set<ConstraintViolation<PageDTO>> violations  = validator.validate(pageDTO);
        assertEquals(0,violations.size());

        pageDTO.setPageIndex(500);
        violations  = validator.validate(pageDTO);
        assertEquals(0,violations.size());


        pageDTO.setPageIndex(1);
        violations  = validator.validate(pageDTO);
        assertEquals(0,violations.size());


        pageDTO.setPageIndex(0);
        violations  = validator.validate(pageDTO);
        assertEquals(1,violations.size());


        pageDTO.setPageIndex(501);
        violations  = validator.validate(pageDTO);
        assertEquals(1,violations.size());

    }




    @Test
    public void testPageSize() {
        // 非0表示存在异常
        Set<ConstraintViolation<PageDTO>> violations  = validator.validate(pageDTO);
        assertEquals(0,violations.size());

        pageDTO.setPageSize(100);
        violations  = validator.validate(pageDTO);
        assertEquals(0,violations.size());


        pageDTO.setPageSize(1);
        violations  = validator.validate(pageDTO);
        assertEquals(0,violations.size());


        pageDTO.setPageSize(0);
        violations  = validator.validate(pageDTO);
        assertEquals(1,violations.size());


        pageDTO.setPageSize(101);
        violations  = validator.validate(pageDTO);
        assertEquals(1,violations.size());

    }
}
