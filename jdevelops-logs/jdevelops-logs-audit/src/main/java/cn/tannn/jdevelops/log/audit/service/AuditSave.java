package cn.tannn.jdevelops.log.audit.service;

import cn.tannn.jdevelops.log.audit.AuditContext;

/**
 * 保存，需要时重写里面的保存方法即可实现数据自定义存入
 * 保存
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/5/20 下午3:01
 */
public interface AuditSave {

    /**
     * 接口日志输出
     * @param audit 审计
     */
    void saveLog(AuditContext audit);
}
