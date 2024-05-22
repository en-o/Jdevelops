package cn.tannn.jdevelops.apis.log;

import cn.tannn.jdevelops.apis.log.module.ApiMonitoring;

/**
 * 保存接口日志，需要时重写里面的保存方法即可实现数据自定义存入
 * 接口日志保存
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/20 下午3:01
 */
public interface ApiLogSave {

    /**
     * 接口日志输出
     * @param apilog apilog
     */
    void saveLog(ApiMonitoring apilog);
}
