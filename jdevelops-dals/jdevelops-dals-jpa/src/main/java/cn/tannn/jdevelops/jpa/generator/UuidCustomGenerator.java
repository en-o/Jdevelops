package cn.tannn.jdevelops.jpa.generator;

import cn.tannn.jdevelops.result.utils.UUIDUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 *
 * 自定义主键生成策略 {@link UUIDUtils#generateShortUuidLong()}
 * <code>
 *     @Id
 *     @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuidCustomGenerator")
 *     @GenericGenerator(name = "uuidCustomGenerator", strategy = "cn.jdevelops.data.jap.generator.UuidCustomGenerator")
 *     @Column(columnDefinition="bigint")
 *     @Comment("uuid")
 *     private Long id;
 * </code>
 *
 * <p>https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators-GenericGenerator
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/15 9:42
 */
public class UuidCustomGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UUIDUtils.getInstance().generateShortUuidLong();
    }
}
