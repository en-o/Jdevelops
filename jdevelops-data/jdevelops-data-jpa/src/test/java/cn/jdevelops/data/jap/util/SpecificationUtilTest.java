package cn.jdevelops.data.jap.util;

import junit.framework.TestCase;
import org.springframework.data.jpa.domain.Specification;


public class SpecificationUtilTest extends TestCase {
    SpecificationUtil<Object> instance = SpecificationUtil.getInstance();

    public void testSpecification() {
        // 创建你的 Specification 对象
        Specification<Object> specification = instance
                .specification((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), "tan"));

    }


    public void testGt() {
        Specification<Object> gt = instance.gt("", "", true);
    }

    public void testTestGt() {
    }

    public void testGe() {
    }

    public void testTestGe() {
    }

    public void testLt() {
    }

    public void testTestLt() {
    }

    public void testLe() {
    }

    public void testTestLe() {
    }
}
