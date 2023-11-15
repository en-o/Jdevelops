package cn.jdevelops.util.core.list;

import junit.framework.TestCase;

import java.util.*;


public class CollectionUtilTest extends TestCase {

    public void testIsEmpty() {
        assertFalse(CollectionUtil.isEmpty(new String[]{"1","2"}));
        assertTrue(CollectionUtil.isEmpty(new String[0]));
        assertFalse(CollectionUtil.isEmpty(new Integer[]{1,2}));
        assertTrue(CollectionUtil.isEmpty(new Integer[0]));
    }

    public void testTestIsEmpty() {
        assertFalse(CollectionUtil.isEmpty(Arrays.asList("1","2")));
        assertTrue(CollectionUtil.isEmpty(Arrays.asList()));

        ArrayList<String> arrayList = new ArrayList<>();
        assertTrue(CollectionUtil.isEmpty(arrayList));
        arrayList.add("12");
        assertFalse(CollectionUtil.isEmpty(arrayList));

        assertTrue(CollectionUtil.isEmpty(Collections.emptyList()));

        HashSet<String> hashSet = new HashSet<>();
        assertTrue(CollectionUtil.isEmpty(hashSet));
        hashSet.add("ad");
        assertFalse(CollectionUtil.isEmpty(hashSet));

        assertTrue(CollectionUtil.isEmpty(Collections.emptySet()));
    }

    public void testIsNotEmpty() {

        assertTrue(CollectionUtil.isNotEmpty(Arrays.asList("1","2")));
        assertFalse(CollectionUtil.isNotEmpty(Arrays.asList()));

        ArrayList<String> arrayList = new ArrayList<>();
        assertFalse(CollectionUtil.isNotEmpty(arrayList));
        arrayList.add("12");
        assertTrue(CollectionUtil.isNotEmpty(arrayList));

        assertFalse(CollectionUtil.isNotEmpty(Collections.emptyList()));

        HashSet<String> hashSet = new HashSet<>();
        assertFalse(CollectionUtil.isNotEmpty(hashSet));
        hashSet.add("ad");
        assertTrue(CollectionUtil.isNotEmpty(hashSet));

        assertFalse(CollectionUtil.isNotEmpty(Collections.emptySet()));
    }

    public void testInitialCapacity() {
        assertEquals(CollectionUtil.initialCapacity(Collections.emptySet()),10);
        assertEquals(CollectionUtil.initialCapacity(Collections.emptyList()),10);
        assertEquals(CollectionUtil.initialCapacity(Arrays.asList("1","2")),2);
    }

    public void testIntersection() {
        ArrayList<String> c1 = new ArrayList<>();
        ArrayList<String> c2 = new ArrayList<>();
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c1.add("12");
        c1.add("23");
        c1.add("4");
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c2.add("3");
        c2.add("5");
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c2.add("4");
        c2.add("12");
        //  c1 跟 c2 的交集为 4，12
        Collection<?> intersection = CollectionUtil.intersection(c1, c2);
        assertEquals(intersection.toString(),"[12, 4]");
        // 测试 前后数据量反转测试
        Collection<?> reIntersection = CollectionUtil.intersection(c2, c1);
        // 顺序
        assertEquals(reIntersection.toString(),"[4, 12]");
    }


    public void testIntersectionSet() {
        Set<String> c1 = new HashSet<>();
        Set<String> c2 = new HashSet<>();
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c1.add("12");
        c1.add("23");
        c1.add("4");
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c2.add("3");
        c2.add("5");
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c2.add("4");
        c2.add("12");
        //  c1 跟 c2 的交集为 4，12
        Collection<?> intersection = CollectionUtil.intersection(c1, c2);
        assertEquals(intersection.toString(),"[12, 4]");
        // 测试 前后数据量反转测试
        Collection<?> reIntersection = CollectionUtil.intersection(c2, c1);
        // set 是有序的
        assertEquals(reIntersection.toString(),"[12, 4]");
    }


    /**
     * bean的话必须重写 equals
     * @see TestBean#equals(Object)
     */
    public void testIntersectionBean() {
        ArrayList<TestBean> c1 = new ArrayList<>();
        ArrayList<TestBean> c2 = new ArrayList<>();
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c1.add(new TestBean("tan",1));
        c1.add(new TestBean("ning",2));
        c1.add(new TestBean("tanning",3));
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c2.add(new TestBean("ning",4));
        c2.add(new TestBean("admin",1));
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c2.add(new TestBean("ning",2));
        c2.add(new TestBean("tan",1));
        //  c1 跟 c2 的交集为 tan:1，ning:2
        Collection<?> intersection = CollectionUtil.intersection(c1, c2);
        assertEquals(intersection.toString(),"[TestBean{name='tan', sex=1}, TestBean{name='ning', sex=2}]");
        // 测试 前后数据量反转测试
        Collection<?> reIntersection = CollectionUtil.intersection(c2, c1);
        // 顺序
        assertEquals(reIntersection.toString(),"[TestBean{name='ning', sex=2}, TestBean{name='tan', sex=1}]");
    }

    /**
     * bean的话必须重写 equals
     * @see TestBean#equals(Object)
     * @see TestBean#hashCode()
     */
    public void testIntersectionBeanSet() {
        Set<TestBean> c1 = new HashSet<>();
        Set<TestBean> c2 = new HashSet<>();
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c1.add(new TestBean("tan",1));
        c1.add(new TestBean("ning",2));
        c1.add(new TestBean("tanning",3));
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c2.add(new TestBean("ning",4));
        c2.add(new TestBean("admin",1));
        assertEquals(CollectionUtil.intersection(c1,c2).size(),0);
        c2.add(new TestBean("ning",2));
        c2.add(new TestBean("tan",1));
        //  c1 跟 c2 的交集为 tan:1，ning:2
        Collection<?> intersection = CollectionUtil.intersection(c1, c2);
        assertEquals(intersection.toString(),"[TestBean{name='tan', sex=1}, TestBean{name='ning', sex=2}]");
        // 测试 前后数据量反转测试
        Collection<?> reIntersection = CollectionUtil.intersection(c2, c1);
        // 顺序
        assertEquals(reIntersection.toString(),"[TestBean{name='ning', sex=2}, TestBean{name='tan', sex=1}]");
    }

    public void testUnion() {

        ArrayList<String> c1 = new ArrayList<>();
        ArrayList<String> c2 = new ArrayList<>();
        assertEquals(CollectionUtil.union(c1,c2).size(),0);
        c1.add("12");
        c1.add("23");
        c1.add("4");

        c2.add("3");
        c2.add("5");
        c2.add("4");
        c2.add("12");
        //  c1 跟 c2 的并集为 4，12，23，3，5
        Collection<?> intersection = CollectionUtil.union(c1, c2);
        assertEquals(intersection.toString(),"[23, 3, 5, 4, 12]");
        // 测试 前后数据量反转测试
        Collection<?> reIntersection = CollectionUtil.union(c2, c1);
        // 顺序
        assertEquals(reIntersection.toString(),"[3, 5, 12, 23, 4]");
    }

    public void testUnionSet() {
        Set<String> c1 = new HashSet<>();
        Set<String> c2 = new HashSet<>();
        assertEquals(CollectionUtil.union(c1,c2).size(),0);
        c1.add("12");
        c1.add("23");
        c1.add("4");

        c2.add("3");
        c2.add("5");
        c2.add("4");
        c2.add("12");
        //  c1 跟 c2 的并集为 4，12，23，3，5
        Collection<?> intersection = CollectionUtil.union(c1, c2);
        assertEquals(intersection.toString(),"[23, 12, 3, 4, 5]");
        // 测试 前后数据量反转测试
        Collection<?> reIntersection = CollectionUtil.union(c2, c1);
        // 顺序
        assertEquals(reIntersection.toString(),"[3, 5, 12, 23, 4]");
    }



    /**
     * bean的话必须重写 equals
     * @see TestBean#equals(Object)
     */
    public void testUnionBean() {
        ArrayList<TestBean> c1 = new ArrayList<>();
        ArrayList<TestBean> c2 = new ArrayList<>();
        assertEquals(CollectionUtil.union(c1,c2).size(),0);
        c1.add(new TestBean("tan",1));
        c1.add(new TestBean("ning",2));
        c1.add(new TestBean("tanning",3));

        c2.add(new TestBean("ning",4));
        c2.add(new TestBean("admin",1));
        c2.add(new TestBean("ning",2));
        c2.add(new TestBean("tan",1));
        //  c1 跟 c2 的交集为 tan:1，ning:2，tanning:3，ning:4，admin:1
        Collection<?> intersection = CollectionUtil.union(c1, c2);
        assertEquals(intersection.toString(),"[TestBean{name='tanning', sex=3}, TestBean{name='ning', sex=4}, TestBean{name='admin', sex=1}, TestBean{name='ning', sex=2}, TestBean{name='tan', sex=1}]");
        // 测试 前后数据量反转测试
        Collection<?> reIntersection = CollectionUtil.union(c2, c1);
        // 顺序
        assertEquals(reIntersection.toString(),"[TestBean{name='ning', sex=4}, TestBean{name='admin', sex=1}, TestBean{name='tan', sex=1}, TestBean{name='ning', sex=2}, TestBean{name='tanning', sex=3}]");
    }

    /**
     * bean的话必须重写 equals
     * @see TestBean#equals(Object)
     * @see TestBean#hashCode()
     */
    public void testUnionBeanSet() {
        Set<TestBean> c1 = new HashSet<>();
        Set<TestBean> c2 = new HashSet<>();
        assertEquals(CollectionUtil.union(c1,c2).size(),0);
        c1.add(new TestBean("tan",1));
        c1.add(new TestBean("ning",2));
        c1.add(new TestBean("tanning",3));

        c2.add(new TestBean("ning",4));
        c2.add(new TestBean("admin",1));
        c2.add(new TestBean("ning",2));
        c2.add(new TestBean("tan",1));
        //  c1 跟 c2 的交集为 tan:1，ning:2，tanning:3，ning:4，admin:1
        Collection<?> intersection = CollectionUtil.union(c1, c2);
        assertEquals(intersection.toString(),"[TestBean{name='tanning', sex=3}, TestBean{name='admin', sex=1}, TestBean{name='ning', sex=2}, TestBean{name='tan', sex=1}, TestBean{name='ning', sex=4}]");
        // 测试 前后数据量反转测试
        Collection<?> reIntersection = CollectionUtil.union(c2, c1);
        // 顺序
        assertEquals(reIntersection.toString(),"[TestBean{name='admin', sex=1}, TestBean{name='ning', sex=4}, TestBean{name='tanning', sex=3}, TestBean{name='tan', sex=1}, TestBean{name='ning', sex=2}]");
    }
}
