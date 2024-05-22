package cn.tannn.jdevelops.jpa.generator;

import cn.tannn.jdevelops.result.utils.UUIDUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 *
 * 自定义主键生成策略 {@link UUIDUtils#generateShortUuid()}
 * <code>
 *     @Id
 *     @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuidStrCustomGenerator")
 *     @GenericGenerator(name = "uuidStrCustomGenerator", strategy = "cn.jdevelops.data.jap.generator.UuidStrCustomGenerator")
 *     @Column(columnDefinition="bigint")
 *     @Comment("uuid")
 *     @JsonSerialize(using = ToStringSerializer.class)
 *     private Long id;
 * </code>
 *
 * <p>https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators-GenericGenerator
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/4/15 9:42
 */
public class UuidStrCustomGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UUIDUtils.getInstance().generateShortUuid();
    }
}
