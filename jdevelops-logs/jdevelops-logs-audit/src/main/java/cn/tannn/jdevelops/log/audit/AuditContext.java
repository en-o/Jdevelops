package cn.tannn.jdevelops.log.audit;


import cn.tannn.jdevelops.jwt.standalone.util.UserUtil;
import cn.tannn.jdevelops.log.audit.constant.OperationalAuditIndex;
import cn.tannn.jdevelops.log.audit.constant.OperationalAuditType;
import cn.tannn.jdevelops.log.audit.constant.OperationalType;
import cn.tannn.jdevelops.log.audit.constant.UniqueIndexType;
import cn.tannn.jdevelops.utils.jwt.module.LoginJwtExtendInfo;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 数据审计日志记录
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/12/23 12:00
 */
@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AuditContext {

    /**
     * 审计类型 {@link OperationalAuditType}
     */
    private String auditType;


    /**
     * 操作类型 {@link OperationalType}
     */
    private String operationalType;

    /**
     * 被操作数据的唯一值 [id/code/no,从数据源决定][请绑定审计类型，以审计类型溯源，如跟用户相关这里就尽量写用户的唯一值]
     */
    private String uniqueCode;

    /**
     * 被操作数据的元数据存储索引[数据库=表名，es=索引名] {@link OperationalAuditIndex},[请绑定审计类型，以审计类型溯源，如跟用户相关这里就尽量写用户的唯一值]
     */
    private String uniqueIndex;


    /**
     * uniqueIndex的类型
     */
    private UniqueIndexType uniqueIndexType;


    /**
     * 被操作数据的名字
     */
    private String dataTitle;


    /**
     * 旧数据快照[json]
     */
    private JSONObject originalData;


    /**
     * 新数据快照[json]
     */
    private JSONObject targetData;

    /**
     * 备注
     */
    private String description;

    /**
     * 操作者登录名
     */
    private String operatorNo;

    /**
     * 操作者姓名
     */
    private String operatorName;




    /**
     * @param auditType       {@link OperationalAuditType}
     * @param operationalType {@link OperationalType}
     * @param uniqueCode      被操作数据的唯一值 [id/code/no,从数据源决定]
     * @param uniqueIndex     {@link OperationalAuditIndex}
     * @param uniqueIndexType uniqueIndex的类型
     * @param dataTitle       被操作数据的名字
     * @param originalData    旧数据快照
     * @param targetData      新数据快照
     */
    public AuditContext(String auditType
            , String operationalType
            , String uniqueCode
            , String uniqueIndex
            , UniqueIndexType uniqueIndexType
            , String dataTitle
            , JSONObject originalData
            , JSONObject targetData) {
        this.auditType = auditType;
        this.operationalType = operationalType;
        this.uniqueCode = uniqueCode;
        this.uniqueIndex = uniqueIndex;
        this.uniqueIndexType = uniqueIndexType;
        this.dataTitle = dataTitle;
        this.originalData = originalData;
        this.targetData = targetData;
    }



    public AuditContext appendOptUser(HttpServletRequest request) {
        LoginJwtExtendInfo userInfo = UserUtil.getUserInfo(request);
        this.operatorName = userInfo.getUserName();
        this.operatorNo = userInfo.getLoginName();
        return this;
    }

    public AuditContext appendOptUser(LoginJwtExtendInfo userInfo) {
        this.operatorName = userInfo.getUserName();
        this.operatorNo = userInfo.getLoginName();
        return this;
    }

    /**
     * 追加操作者
     */
    public AuditContext appendOptUser(String loginName, String userName) {
        this.operatorName = userName;
        this.operatorNo = loginName;
        return this;
    }


    /**
     * 设置： targetData
     */
    public AuditContext targetJson(Object target) {
        this.targetData = JSONObject.from(target);
        return this;
    }

    /**
     * 设置： targetData
     * @param target 数据bean
     * @param filterField 需要过滤的字段  - 比如 密码
     */
    public AuditContext targetJson(Object target, String... filterField) {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        if(filterField.length>0){
            filter.getExcludes().addAll(Arrays.asList(filterField));
        }
        this.targetData = JSONObject.parseObject(
                JSON.toJSONString(target, filter)
        );
        return this;
    }

    /**
     * 设置： originalData
     */
    public AuditContext originalJson(Object original) {
        this.originalData = JSONObject.from(original);
        return this;
    }

    /**
     * 设置： originalData
     * @param original 数据bean
     * @param filterField 需要过滤的字段  - 比如 密码
     */
    public AuditContext originalJson(Object original, String... filterField) {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        if(filterField.length>0){
            filter.getExcludes().addAll(Arrays.asList(filterField));
        }
        this.originalData = JSONObject.parseObject(
                JSON.toJSONString(original, filter)
        );
        return this;
    }
}
