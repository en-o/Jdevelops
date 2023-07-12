package cn.jdevelops.sboot.authentication.jwt.util;

import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.SignEntity;
import com.alibaba.fastjson2.JSON;
import junit.framework.TestCase;
import org.jose4j.lang.JoseException;

import java.util.Arrays;
import java.util.List;

public class JwtWebUtilTest extends TestCase {

    String signBean;
    String signNull;
    String signStr;
    String signInt;
    String signArrStr;
    String signArrInt;
    String signListStr;
    String signListInt;
    String signListBean;
    String signListBeanComplex;


    {
        try {
            SignEntity<JwtWebUtilBean> signEntity = new SignEntity<>("tan", "tan", "tan","tan", new JwtWebUtilBean("tan",10));
            SignEntity<String> signEntityNull = new SignEntity<>("tan", "tan", "tan", "tan");
            SignEntity<String> signEntityStr = new SignEntity<>("tan", "tan", "tan", "tan", "tan");
            SignEntity<Integer> signEntityInt = new SignEntity<>("tan", "tan", "tan", "tan", 1);
            SignEntity<String[]> signEntityArrStr = new SignEntity<>("tan", "tan", "tan", "tan", new String[]{"tan","tan"});
            SignEntity<Integer[]> signEntityArrInt = new SignEntity<>("tan", "tan", "tan", "tan", new Integer[]{1,2});
            SignEntity<List<String>> signEntityListStr = new SignEntity<>("tan", "tan", "tan", "tan", Arrays.asList("tan","ning"));
            SignEntity<List<Integer>> signEntityListInt = new SignEntity<>("tan", "tan", "tan", "tan", Arrays.asList(1,2));
            SignEntity<List<JwtWebUtilBean>> signEntityListBean = new SignEntity<>("tan", "tan", "tan", "tan", Arrays.asList(
                    new JwtWebUtilBean("tan",10),
                    new JwtWebUtilBean("ning",11)
            ));
            SignEntity<List<JwtWebUtilBeanComplex>> signEntityListBeanComplex = new SignEntity<>("tan", "tan", "tan", "tan", Arrays.asList(
                    new JwtWebUtilBeanComplex("tan",10, Arrays.asList("tan","ning"),
                            Arrays.asList(1,2),
                            Arrays.asList(
                                    new JwtWebUtilBean("tan1",10),
                                    new JwtWebUtilBean("ning1",11)
                            )),
                    new JwtWebUtilBeanComplex("ning",10, Arrays.asList("tan2","ning2"),
                            Arrays.asList(13,23),
                            Arrays.asList(
                                    new JwtWebUtilBean("tan2",11),
                                    new JwtWebUtilBean("ning2",12)
                            ))
            ));
            signBean = JwtService.generateToken(signEntity);
            signNull = JwtService.generateToken(signEntityNull);
            signStr = JwtService.generateToken(signEntityStr);
            signInt = JwtService.generateToken(signEntityInt);
            signArrStr = JwtService.generateToken(signEntityArrStr);
            signArrInt = JwtService.generateToken(signEntityArrInt);
            signListStr = JwtService.generateToken(signEntityListStr);
            signListInt = JwtService.generateToken(signEntityListInt);
            signListBean = JwtService.generateToken(signEntityListBean);
            signListBeanComplex = JwtService.generateToken(signEntityListBeanComplex);
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }

    public void testGetTokenByBeanMapBean() {
        // map bean
        SignEntity<JwtWebUtilBean> tokenByBean = JwtWebUtil.getTokenByBean(signBean, SignEntity.class, JwtWebUtilBean.class);
        assertEquals("SignEntity{subject='tan', loginName='tan', userId='tan', userName='tan', map=JwtWebUtilBean(name=tan, sex=10)}",
                tokenByBean.toString());
    }


    public void testGetTokenByBeanMapNull() {
        // map null
        assertEquals("SignEntity{subject='tan', loginName='tan', userId='tan', userName='tan', map=null}",
                JwtWebUtil.getTokenByBean(signNull, SignEntity.class, null).toString());
    }

    public void testGetTokenByBeanMapStr() {
        // map str
        SignEntity<String> tokenByBeanStr = JwtWebUtil.getTokenByBean(signStr, SignEntity.class, String.class);
        assertEquals("SignEntity{subject='tan', loginName='tan', userId='tan', userName='tan', map=tan}",
                tokenByBeanStr.toString());
        assertEquals("tan",
                tokenByBeanStr.getMap());
    }

    public void testGetTokenByBeanMapInt() {
        // map int
        SignEntity<Integer> tokenByBeanInt = JwtWebUtil.getTokenByBean(signInt, SignEntity.class, Integer.class);
        assertEquals("SignEntity{subject='tan', loginName='tan', userId='tan', userName='tan', map=1}",
                tokenByBeanInt.toString());
        Integer map = tokenByBeanInt.getMap();
        assertEquals(1, (int) map);
    }


    public void testGetTokenByBeanMapArrStr() {
        // map str
        SignEntity<String[]> tokenByBeanArrStr = JwtWebUtil.getTokenByBean(signArrStr, SignEntity.class, String[].class);
        String[] map = tokenByBeanArrStr.getMap();
        assertEquals("[tan, tan]", Arrays.toString(map));
    }

    public void testGetTokenByBeanMapArrInt() {
        // map int
        SignEntity<Integer[]> tokenByBeanArrInt = JwtWebUtil.getTokenByBean(signArrInt, SignEntity.class, Integer[].class);
        Integer[] map = tokenByBeanArrInt.getMap();
        assertEquals("[1, 2]", Arrays.toString(map));
    }

    public void testGetTokenByBeanMapListStr() {
        // map int
        SignEntity<List<String>> tokenByBeanListStr = JwtWebUtil.getTokenByBean(signListStr, SignEntity.class, List.class);
        List<String> map = tokenByBeanListStr.getMap();
        assertEquals("[tan, ning]", map.toString());
    }


    public void testGetTokenByBeanMapListInt() {
        // map int
        SignEntity<List<Integer>> tokenByBeanArrInt = JwtWebUtil.getTokenByBean(signListInt, SignEntity.class, List.class);
        List<Integer> map = tokenByBeanArrInt.getMap();
        assertEquals("[1, 2]", map.toString());
    }


    public void testGetTokenByBeanMapListBean() {
        // map int
        SignEntity<List<JwtWebUtilBean>> tokenByBeanListBean = JwtWebUtil.getTokenByBean(signListBean, SignEntity.class, List.class);
        List<JwtWebUtilBean> map = tokenByBeanListBean.getMap();
        assertEquals("[{\"name\":\"tan\",\"sex\":10}, {\"name\":\"ning\",\"sex\":11}]", map.toString());
    }


    public void testGetTokenByBeanMapListBeanComplex() {
        // map int
        SignEntity<List<JwtWebUtilBeanComplex>> tokenByBeanListBeanComplex = JwtWebUtil.getTokenByBean(signListBeanComplex, SignEntity.class, List.class);
        List<JwtWebUtilBeanComplex> map = tokenByBeanListBeanComplex.getMap();
        assertEquals("[{\"beans\":[{\"name\":\"tan1\",\"sex\":10},{\"name\":\"ning1\",\"sex\":11}],\"ints\":[1,2],\"name\":\"tan\",\"sex\":10,\"strs\":[\"tan\",\"ning\"]}, {\"beans\":[{\"name\":\"tan2\",\"sex\":11},{\"name\":\"ning2\",\"sex\":12}],\"ints\":[13,23],\"name\":\"ning\",\"sex\":10,\"strs\":[\"tan2\",\"ning2\"]}]",
                map.toString());
    }

    public void test() {
        // map int
        String x = "[\"tan\",\"tan\"]";
        Arrays.stream(JSON.to(String[].class,x)).forEach(System.out::println);
    }
}
