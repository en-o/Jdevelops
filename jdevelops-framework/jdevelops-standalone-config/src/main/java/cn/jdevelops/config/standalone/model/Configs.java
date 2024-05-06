package cn.jdevelops.config.standalone.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 客户端注册上来的配置信息
 *
 * @author tan
 */
@Entity
@Table(name = "configs")
@Getter
@Setter
@ToString
@DynamicUpdate
@DynamicInsert
public class Configs {

    @Id
    @Column(columnDefinition = "bigint")
    @Comment("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 应用
     */
    @Comment("应用")
    @Column(columnDefinition = "varchar(64)")
    private String app;
    /**
     * 环境
     */
    @Comment("环境")
    @Column(columnDefinition = "varchar(64)")
    private String env;
    /**
     * namespace
     */
    @Comment("namespace")
    @Column(columnDefinition = "varchar(64)")
    private String ns;
    /**
     * 参数key
     */
    @Comment("参数key")
    @Column(columnDefinition = "varchar(64)")
    private String pkey;
    /**
     * 参数value
     */
    @Comment("参数value")
    @Column(columnDefinition = "varchar(128)")
    private String pval;


    /**
     * Configs
     * @param app 应用
     * @param env 环境
     * @param ns namespace
     * @param pkey 参数key
     * @param pval 参数value
     */
    public Configs  create(String app, String env, String ns, String pkey, String pval) {
        this.id = System.currentTimeMillis();
        this.app = app;
        this.env = env;
        this.ns = ns;
        this.pkey = pkey;
        this.pval = pval;
        return this;
    }
}
