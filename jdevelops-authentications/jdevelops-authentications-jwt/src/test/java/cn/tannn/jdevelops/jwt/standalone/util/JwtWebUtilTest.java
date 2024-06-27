package cn.tannn.jdevelops.jwt.standalone.util;

import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import cn.tannn.jdevelops.utils.jwt.module.SignEntity;
import com.alibaba.fastjson2.TypeReference;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JwtWebUtilTest {

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
            SignEntity<JwtWebUtilBean> signEntity = new SignEntity<>("tan",  new JwtWebUtilBean("tan",10));
            SignEntity<String> signEntityNull = new SignEntity<>("tan");
            SignEntity<String> signEntityStr = new SignEntity<>("tan",  "tan");
            SignEntity<Integer> signEntityInt = new SignEntity<>("tan",  1);
            SignEntity<String[]> signEntityArrStr = new SignEntity<>("tan",  new String[]{"tan","tan"});
            SignEntity<Integer[]> signEntityArrInt = new SignEntity<>("tan",  new Integer[]{1,2});
            SignEntity<List<String>> signEntityListStr = new SignEntity<>("tan",  Arrays.asList("tan","ning"));
            SignEntity<List<Integer>> signEntityListInt = new SignEntity<>("tan",  Arrays.asList(1,2));
            SignEntity<List<JwtWebUtilBean>> signEntityListBean = new SignEntity<>("tan",  Arrays.asList(
                    new JwtWebUtilBean("tan",10),
                    new JwtWebUtilBean("ning",11)
            ));
            SignEntity<List<JwtWebUtilBeanComplex>> signEntityListBeanComplex = new SignEntity<>("tan",  Arrays.asList(
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

    @Test
    public void testGetTokenByBeanMapBean() {
        // map bean
        SignEntity<JwtWebUtilBean> tokenByBean = JwtService.getTokenByBean(signBean, SignEntity.class, JwtWebUtilBean.class);
        assertEquals("SignEntity{subject='tan', platform=[COMMON], map={\"name\":\"tan\",\"sex\":10}}",
                tokenByBean.toString());
    }

    @Test
    public void testGetTokenByBeanMapNull() {
        // map null
        assertEquals("SignEntity{subject='tan', platform=[COMMON], map=tan}",
                JwtService.getTokenByBean(signNull, SignEntity.class, null).toString());
    }

    @Test
    public void testGetTokenByBeanMapStr() {
        // map str
        SignEntity<String> tokenByBeanStr = JwtService.getTokenByBean(signStr, SignEntity.class, String.class);
        assertEquals("SignEntity{subject='tan', platform=[\"COMMON\"], map=tan}",
                tokenByBeanStr.toString());
        assertEquals("tan",
                tokenByBeanStr.getMap());
    }
    @Test
    public void testGetTokenByBeanMapInt() {
        // map int
        SignEntity<Integer> tokenByBeanInt = JwtService.getTokenByBean(signInt, SignEntity.class, Integer.class);
        assertEquals("SignEntity{subject='tan', platform=[COMMON], map=1}",
                tokenByBeanInt.toString());
        Integer map = tokenByBeanInt.getMap();
        assertEquals(1, (int) map);
    }

    @Test
    public void testGetTokenByBeanMapArrStr() {
        // map str
        SignEntity<String[]> tokenByBeanArrStr = JwtService.getTokenByBean(signArrStr, SignEntity.class, String[].class);
        String[] map = tokenByBeanArrStr.getMap();
        assertEquals("[tan, tan]", Arrays.toString(map));
    }
    @Test
    public void testGetTokenByBeanMapArrInt() {
        // map int
        SignEntity<Integer[]> tokenByBeanArrInt = JwtService.getTokenByBean(signArrInt, SignEntity.class, Integer[].class);
        Integer[] map = tokenByBeanArrInt.getMap();
        assertEquals("[1, 2]", Arrays.toString(map));
    }
    @Test
    public void testGetTokenByBeanMapListStr() {
        // map int
        SignEntity<List<String>> tokenByBeanListStr = JwtService.getTokenByBean(signListStr, SignEntity.class, List.class);
        List<String> map = tokenByBeanListStr.getMap();
        assertEquals("[tan, ning]", map.toString());
    }

    @Test
    public void testGetTokenByBeanMapListInt() {
        // map int
        SignEntity<List<Integer>> tokenByBeanArrInt = JwtService.getTokenByBean(signListInt, SignEntity.class, List.class);
        List<Integer> map = tokenByBeanArrInt.getMap();
        assertEquals("[1, 2]", map.toString());
    }

    @Test
    public void testGetTokenByBeanMapListBean() {
        // map int
        SignEntity<List<JwtWebUtilBean>> tokenByBeanListBean = JwtService.getTokenByBean(signListBean, SignEntity.class, List.class);
        List<JwtWebUtilBean> map = tokenByBeanListBean.getMap();
        assertEquals("[{\"name\":\"tan\",\"sex\":10}, {\"name\":\"ning\",\"sex\":11}]", map.toString());
    }

    @Test
    public void testGetTokenByBeanMapListBeanComplex() {
        // map int
        SignEntity<List<JwtWebUtilBeanComplex>> tokenByBeanListBeanComplex = JwtService.getTokenByBean(signListBeanComplex, SignEntity.class, List.class);
        List<JwtWebUtilBeanComplex> map = tokenByBeanListBeanComplex.getMap();
        assertEquals("[{\"beans\":[{\"name\":\"tan1\",\"sex\":10},{\"name\":\"ning1\",\"sex\":11}],\"ints\":[1,2],\"name\":\"tan\",\"sex\":10,\"strs\":[\"tan\",\"ning\"]}, {\"beans\":[{\"name\":\"tan2\",\"sex\":11},{\"name\":\"ning2\",\"sex\":12}],\"ints\":[13,23],\"name\":\"ning\",\"sex\":10,\"strs\":[\"tan2\",\"ning2\"]}]",
                map.toString());
    }
    @Test
    public void testGetTokenBySignEntity() {
        // 创建HttpServletRequest的模拟对象
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        // 设置模拟请求的上下文和参数
        Mockito.when(request.getContextPath()).thenReturn("/test");
        Mockito.when(request.getMethod()).thenReturn("GET");
        Mockito.when(request.getParameter("token")).thenReturn(signBean);

        SignEntity<JwtWebUtilBean> tokenByBeanListBean =
                JwtWebUtil.getTokenBySignEntity(request,
                        JwtWebUtilBean.class);
        JwtWebUtilBean mapBean = tokenByBeanListBean.getMap();
        assertEquals("JwtWebUtilBean(name=tan, sex=10)",
                mapBean.toString());
    }

    @Test
    public void testGetTokenBySignEntity2() {
        // 创建HttpServletRequest的模拟对象
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        // 设置模拟请求的上下文和参数
        Mockito.when(request.getContextPath()).thenReturn("/test");
        Mockito.when(request.getMethod()).thenReturn("GET");
        Mockito.when(request.getParameter("token")).thenReturn(signListBeanComplex);

        SignEntity<List<JwtWebUtilBeanComplex>> tokenByBeanListBeanComplex =
                JwtWebUtil.getTokenBySignEntity(request,
                        List.class);
        List<JwtWebUtilBeanComplex> map = tokenByBeanListBeanComplex.getMap();
        assertEquals("[{\"beans\":[{\"name\":\"tan1\",\"sex\":10},{\"name\":\"ning1\",\"sex\":11}],\"ints\":[1,2],\"name\":\"tan\",\"sex\":10,\"strs\":[\"tan\",\"ning\"]}, {\"beans\":[{\"name\":\"tan2\",\"sex\":11},{\"name\":\"ning2\",\"sex\":12}],\"ints\":[13,23],\"name\":\"ning\",\"sex\":10,\"strs\":[\"tan2\",\"ning2\"]}]",
                map.toString());
    }
    @Test
    public void testGetTokenBySignEntity3() {
        // 创建HttpServletRequest的模拟对象
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        // 设置模拟请求的上下文和参数
        Mockito.when(request.getContextPath()).thenReturn("/test");
        Mockito.when(request.getMethod()).thenReturn("GET");
        Mockito.when(request.getParameter("token")).thenReturn(signListStr);

        SignEntity<List<String>> str =
                JwtWebUtil.getTokenBySignEntity(request,
                        List.class);
        List<String> map = str.getMap();
        assertEquals("[tan, ning]",
                map.toString());
    }
}

