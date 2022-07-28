package cn.jdevelops.jap.core.util;

import cn.jdevelops.result.response.SortVO;
import org.junit.Test;
import org.springframework.data.domain.Sort;

import static org.junit.Assert.*;

public class SortUtilTest {

    @Test
    public void getSort() {
        SortVO sortVO = new SortVO();
        assertEquals(SortUtil.getSort(sortVO).toString(),
                "SortVO(orderBy=id, orderDesc=0)");
        sortVO = new SortVO();
        sortVO.setOrderDesc(1);
        assertEquals(SortUtil.getSort(sortVO).toString(),
                "SortVO(orderBy=id, orderDesc=1)");
        sortVO = new SortVO();
        sortVO.setOrderDesc(1);
        sortVO.setOrderBy("sex");
        assertEquals(SortUtil.getSort(sortVO).toString(),
                "SortVO(orderBy=sex, orderDesc=1)");
    }

    @Test
    public void testGetSort() {
        SortVO sortVO = new SortVO();
        assertEquals(SortUtil.getSort(sortVO, UserTest::getName).toString(),
                "SortVO(orderBy=name, orderDesc=0)");
        sortVO = new SortVO();
        sortVO.setOrderDesc(1);
        assertEquals(SortUtil.getSort(sortVO, UserTest::getName).toString(),
                "SortVO(orderBy=name, orderDesc=1)");
        sortVO = new SortVO();
        sortVO.setOrderDesc(1);
        sortVO.setOrderBy("sex");
        assertEquals(SortUtil.getSort(sortVO, UserTest::getName).toString(),
                "SortVO(orderBy=sex, orderDesc=1)");
    }

    @Test
    public void sort() {
        assertEquals(SortUtil.sort(Sort.Direction.ASC, UserTest::getName).toString(),
                "name: ASC");
        assertEquals(SortUtil.sort(Sort.Direction.ASC, UserTest::getSex).toString(),
                "sex: ASC");
        assertEquals(SortUtil.sort(Sort.Direction.DESC, UserTest::getSex).toString(),
                "sex: DESC");
    }

    static class UserTest{
        private final String name;
        private final String sex;

        public String getName() {
            return name;
        }

        public String getSex() {
            return sex;
        }

        public UserTest(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }
        @Override
        public String toString() {
            return "UserTest{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }
    }
}
