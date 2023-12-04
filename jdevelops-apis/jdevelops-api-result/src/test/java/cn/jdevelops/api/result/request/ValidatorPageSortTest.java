package cn.jdevelops.api.result.request;

import cn.jdevelops.api.result.BeastValidatedTest;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * 测试validation
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-06-06 21:51
 */
public class ValidatorPageSortTest extends BeastValidatedTest {
    private SortDTO sortDTO;
    private PageDTO pageDTO;
    private SortPageDTO sortPageDTO;


    @Before
    public void initBean() {
        sortDTO = new SortDTO();
        pageDTO = new PageDTO();
        sortPageDTO = new SortPageDTO();

    }


    @Test
    public void testOrderBySize() {
        // 非0表示存在异常
        // 或者循环拿异常消息
        Set<ConstraintViolation<SortDTO>> violations  = validate(sortDTO);
        assertEquals(0,violations.size());

        sortDTO.setOrderBy("i1","i2","i3","i4","i5");
        violations  = validate(sortDTO);
        assertEquals(0,violations.size());

        sortDTO.setOrderBy("i1","i2","i3","i4","i5","i6");
        violations  = validate(sortDTO);
        violations.forEach(violation -> {
            assertEquals("排序字段超出了阈值",violation.getMessage());
        });
    }


    @Test
    public void testOrderDesc() {
        // 非0表示存在异常
        Set<ConstraintViolation<SortDTO>> violations  = validate(sortDTO);
        assertEquals(0,violations.size());

        sortDTO.setOrderDesc(1);
        violations  = validate(sortDTO);
        assertEquals(0,violations.size());


        sortDTO.setOrderDesc(0);
        violations  = validate(sortDTO);
        assertEquals(0,violations.size());


        sortDTO.setOrderDesc(-1);
        violations  = validate(sortDTO);
        violations.forEach(violation -> assertEquals("请正确选择排序方式",violation.getMessage()));

        sortDTO.setOrderDesc(2);
        violations  = validate(sortDTO);
        violations.forEach(violation -> assertEquals("请正确选择排序方式",violation.getMessage()));
    }



    @Test
    public void testPageIndex() {
        // 非0表示存在异常
        Set<ConstraintViolation<PageDTO>> violations  = validate(pageDTO);
        assertEquals(0,violations.size());

        pageDTO.setPageIndex(500);
        violations  = validate(pageDTO);
        assertEquals(0,violations.size());


        pageDTO.setPageIndex(1);
        violations  = validate(pageDTO);
        assertEquals(0,violations.size());


        pageDTO.setPageIndex(0);
        violations  = validate(pageDTO);
        violations.forEach(violation -> assertEquals("页码超出了阈值",violation.getMessage()));

        pageDTO.setPageIndex(10001);
        violations  = validate(pageDTO);
        violations.forEach(violation -> assertEquals("页码超出了阈值",violation.getMessage()));
    }




    @Test
    public void testPageSize() {
        // 非0表示存在异常
        Set<ConstraintViolation<PageDTO>> violations  = validate(pageDTO);
        assertEquals(0,violations.size());

        pageDTO.setPageSize(100);
        violations  = validate(pageDTO);
        assertEquals(0,violations.size());


        pageDTO.setPageSize(1);
        violations  = validate(pageDTO);
        assertEquals(0,violations.size());


        pageDTO.setPageSize(0);
        violations  = validate(pageDTO);
        violations.forEach(violation -> assertEquals("每页数量超出了阈值",violation.getMessage()));

        pageDTO.setPageSize(101);
        violations  = validate(pageDTO);
        violations.forEach(violation -> assertEquals("每页数量超出了阈值",violation.getMessage()));
    }


    @Test
    public void testSortPageDTO() {
        // 非0表示存在异常
        Set<ConstraintViolation<SortPageDTO>> violations  = validate(sortPageDTO);
        assertEquals(0,violations.size());

        sortPageDTO.setPageIndex(1);
        violations  = validate(sortPageDTO);
        assertEquals(0,violations.size());

        sortPageDTO.setSorts(Arrays.asList(new SortDTO("id")
                ,new SortDTO("id")
                ,new SortDTO("id")
                ,new SortDTO("id")));
        violations  = validate(sortPageDTO);
        assertEquals(0,violations.size());

        sortPageDTO.setPageIndex(10001);
        violations  = validate(sortPageDTO);
        violations.forEach(violation -> assertEquals("页码超出了阈值",violation.getMessage()));

        sortPageDTO.setSorts(Arrays.asList(new SortDTO("id")
                ,new SortDTO("id")
                ,new SortDTO("id")
                ,new SortDTO("id")
                ,new SortDTO("id")
                ,new SortDTO("id")));
        violations  = validate(sortPageDTO);
        // 上面有个错了所以这里是2
        assertEquals(2,violations.size());
        ArrayList<ConstraintViolation<SortPageDTO>> list = new ArrayList<>(violations);
        for (ConstraintViolation<SortPageDTO> sortPageDTOConstraintViolation : list) {
            System.out.println(sortPageDTOConstraintViolation.getMessage());
        }
    }
}
