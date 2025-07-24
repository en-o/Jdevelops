package cn.tannn.jdevelops.log.audit;


import cn.tannn.jdevelops.log.audit.constant.OperationalAuditIndex;
import cn.tannn.jdevelops.log.audit.constant.OperationalAuditType;
import cn.tannn.jdevelops.log.audit.constant.OperationalType;
import cn.tannn.jdevelops.log.audit.constant.UniqueIndexType;
import cn.tannn.jdevelops.log.audit.util.JsonUtil;
import com.alibaba.fastjson2.JSONObject;
/**
 * 数据审计日志记录
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/12/23 12:00
 */
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
     * 自定义类型， 用于系统自主的一些判断条件
     */
    private Integer customType;


    /**
     * 操作平台/数据所属平台
     */
    private String platform;

    /**
     * 操作状态
     */
    private Boolean status;


    /**
     * 错误信息
     */
    private String failMessage;

    /**
     * 是否批量
     */
    private boolean batch;


    /**
     * 访问IP
     */
    private String accessIp;


    public AuditContext() {
        this.status = true; // 默认操作成功
        this.batch = false; // 默认单条操作
    }

    public AuditContext(String auditType, String operationalType, String uniqueCode, String uniqueIndex, UniqueIndexType uniqueIndexType, String dataTitle, JSONObject originalData, JSONObject targetData, String description, String operatorNo, String operatorName) {
        this.auditType = auditType;
        this.operationalType = operationalType;
        this.uniqueCode = uniqueCode;
        this.uniqueIndex = uniqueIndex;
        this.uniqueIndexType = uniqueIndexType;
        this.dataTitle = dataTitle;
        this.originalData = originalData;
        this.targetData = targetData;
        this.description = description;
        this.operatorNo = operatorNo;
        this.operatorName = operatorName;
    }


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
        this.targetData = JsonUtil.convertToJsonObject(target);
        return this;
    }

    /**
     * 设置： targetData
     *
     * @param target      数据bean
     * @param filterField 需要过滤的字段  - 比如 密码
     */
    public AuditContext targetJson(Object target, String... filterField) {
        this.targetData = JsonUtil.convertToJsonObjectWithFilter(target, filterField);
        return this;
    }

    /**
     * 设置： originalData
     */
    public AuditContext originalJson(Object original) {
        this.originalData = JsonUtil.convertToJsonObject(original);
        return this;
    }

    /**
     * 设置： originalData
     *
     * @param original    数据bean
     * @param filterField 需要过滤的字段  - 比如 密码
     */
    public AuditContext originalJson(Object original, String... filterField) {
        this.originalData = JsonUtil.convertToJsonObjectWithFilter(original, filterField);
        return this;
    }


    public String getAuditType() {
        return auditType;
    }

    /**
     * @param auditType 审计类型 {@link OperationalAuditType}
     */
    public AuditContext setAuditType(String auditType) {
        this.auditType = auditType;
        return this;
    }

    public String getOperationalType() {
        return operationalType;
    }

    /**
     * @param operationalType 操作类型 {@link OperationalType}
     */
    public AuditContext setOperationalType(String operationalType) {
        this.operationalType = operationalType;
        return this;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    /**
     * @param uniqueCode 被操作数据的唯一值 [id/code/no,从数据源决定][请绑定审计类型，以审计类型溯源，如跟用户相关这里就尽量写用户的唯一值]
     */
    public AuditContext setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
        return this;
    }

    public String getUniqueIndex() {
        return uniqueIndex;
    }

    /**
     * @param uniqueIndex 被操作数据的元数据存储索引[数据库=表名，es=索引名] {@link OperationalAuditIndex},[请绑定审计类型，以审计类型溯源，如跟用户相关这里就尽量写用户的唯一值]
     */
    public AuditContext setUniqueIndex(String uniqueIndex) {
        this.uniqueIndex = uniqueIndex;
        return this;
    }

    public UniqueIndexType getUniqueIndexType() {
        return uniqueIndexType;
    }

    /**
     * @param uniqueIndexType uniqueIndex的类型
     */
    public AuditContext setUniqueIndexType(UniqueIndexType uniqueIndexType) {
        this.uniqueIndexType = uniqueIndexType;
        return this;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    /**
     * @param dataTitle 被操作数据的名字
     */
    public AuditContext setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
        return this;
    }

    public JSONObject getOriginalData() {
        return originalData;
    }

    /**
     * @param originalData 旧数据快照[json]
     */
    public AuditContext setOriginalData(JSONObject originalData) {
        this.originalData = originalData;
        return this;
    }

    public JSONObject getTargetData() {
        return targetData;
    }

    /**
     * @param targetData 新数据快照[json]
     */
    public AuditContext setTargetData(JSONObject targetData) {
        this.targetData = targetData;
        return this;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @param description 备注
     */
    public AuditContext setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    /**
     * @param operatorNo 操作者登录名
     */
    public AuditContext setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
        return this;
    }

    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName 操作者姓名
     */
    public AuditContext setOperatorName(String operatorName) {
        this.operatorName = operatorName;
        return this;
    }


    /**
     * 自定义类型， 用于系统自主的一些判断条件
     */
    public String getPlatform() {
        return platform;
    }


    /**
     * 自定义类型， 用于系统自主的一些判断条件
     */
    public AuditContext setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    /**
     * 平台
     */
    public Integer getCustomType() {
        return customType;
    }

    /**
     * 平台
     */
    public AuditContext setCustomType(Integer customType) {
        this.customType = customType;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }

    /**
     * 截断过长的错误消息
     */
    public String truncateMessage() {
        if (this.failMessage == null) {
            return null;
        }
        return this.failMessage.length() > 200
                ? this.failMessage.substring(0, 200) + "..."
                : this.failMessage;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    public String getAccessIp() {
        return accessIp;
    }

    public void setAccessIp(String accessIp) {
        this.accessIp = accessIp;
    }

    @Override
    public String toString() {
        return "AuditContext{" +
                "auditType='" + auditType + '\'' +
                ", operationalType='" + operationalType + '\'' +
                ", uniqueCode='" + uniqueCode + '\'' +
                ", uniqueIndex='" + uniqueIndex + '\'' +
                ", uniqueIndexType=" + uniqueIndexType +
                ", dataTitle='" + dataTitle + '\'' +
                ", originalData=" + originalData +
                ", targetData=" + targetData +
                ", description='" + description + '\'' +
                ", operatorNo='" + operatorNo + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", customType=" + customType +
                ", platform='" + platform + '\'' +
                ", status=" + status +
                ", failMessage='" + failMessage + '\'' +
                ", batch=" + batch +
                ", accessIp='" + accessIp + '\'' +
                '}';
    }
}
