package cn.jdevelops.list.core;

import junit.framework.TestCase;

import java.util.*;

public class ListGatherTest extends TestCase {


    public void testDifferenceOneway() {
        List<TestBean> lists1 = new ArrayList<>();
        List<TestBean> lists2 = new ArrayList<>();
        List<TestBean> result = new ArrayList<>();

        TestBean testBean = new TestBean();
        testBean.setAge(1);
        testBean.setName("a");
        lists1.add(testBean);
        testBean = new TestBean();
        testBean.setAge(2);
        testBean.setName("b");
        lists1.add(testBean);

        testBean = new TestBean();
        testBean.setAge(1);
        testBean.setName("a");
        lists2.add(testBean);
        testBean = new TestBean();
        testBean.setAge(2);
        testBean.setName("b");
        lists2.add(testBean);
        testBean = new TestBean();
        testBean.setAge(3);
        testBean.setName("c");
        lists2.add(testBean);
        List<TestBean> 删除list1中跟list2一样key值的数据饭后返回剩下的list1 = ListGather.differenceOneway(lists1, lists2, "age");
        assertEquals(Collections.emptyList().toString(),删除list1中跟list2一样key值的数据饭后返回剩下的list1.toString());

        List<TestBean> 删除list2中跟list1一样key值的数据饭后返回剩下的list2 = ListGather.differenceOneway(lists2, lists1, "age");
        testBean = new TestBean();
        testBean.setAge(3);
        testBean.setName("c");
        result.add(testBean);
        assertEquals(result.toString(),删除list2中跟list1一样key值的数据饭后返回剩下的list2.toString());
    }

    public void testGetListSIForMaxMin() {
        List<String> str = new ArrayList<>();
        str.add("a");
        str.add("b");
        str.add("c");
        assertEquals("a", ListGather.getListSIForMaxMin(str, 1));
        assertEquals("c", ListGather.getListSIForMaxMin(str, 0));
        List<Integer>  intt = new ArrayList<>();
        intt.add(1);
        intt.add(2);
        intt.add(3);
        assertEquals(Integer.valueOf(1), ListGather.getListSIForMaxMin(intt, 1));
        assertEquals(Integer.valueOf(3), ListGather.getListSIForMaxMin(intt, 0));
    }

    public void testDifferenceBothway() {

        List<TestBean> lists1 = new ArrayList<>();
        List<TestBean> lists2 = new ArrayList<>();
        List<TestBean> result = new ArrayList<>();

        TestBean testBean = new TestBean();
        testBean.setAge(1);
        testBean.setName("a");
        lists1.add(testBean);
        testBean = new TestBean();
        testBean.setAge(2);
        testBean.setName("b");
        lists1.add(testBean);

        testBean = new TestBean();
        testBean.setAge(1);
        testBean.setName("a");
        lists2.add(testBean);
        testBean = new TestBean();
        testBean.setAge(3);
        testBean.setName("c");
        lists2.add(testBean);

        List<TestBean> 差集 = ListGather.differenceBothway(lists1, lists2, "age");
        testBean = new TestBean();
        testBean.setAge(2);
        testBean.setName("b");
        result.add(testBean);
        testBean = new TestBean();
        testBean.setAge(3);
        testBean.setName("c");
        result.add(testBean);
        assertEquals(result.toString(), 差集.toString());
    }


    public class TestBean{
        String name;
        Integer age;

        @Override
        public String toString() {
            return "TestBean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Integer getAge() {
            return age;
        }
        public void setAge(Integer age) {
            this.age = age;
        }
    }
}