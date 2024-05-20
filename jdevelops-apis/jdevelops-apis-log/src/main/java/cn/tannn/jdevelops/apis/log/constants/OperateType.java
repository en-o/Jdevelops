package cn.tannn.jdevelops.apis.log.constants;


import cn.tannn.jdevelops.apis.log.annotation.ApiLog;

/**
 * 操作日志的操作类型
 * {@link ApiLog}
 * @author tan
 */

public interface OperateType {

    /**
     * 查询
     *
     */
    int GET = 1;
    /**
     * 新增
     */
    int CREATE = 2;
    /**
     * 修改
     */
    int UPDATE = 3;
    /**
     * 删除
     */
    int DELETE = 4;
    /**
     * 导出
     */
    int EXPORT = 5;
    /**
     * 导入
     */
    int IMPORT = 6;
    /**
     * 其它
     *
     * 在无法归类时，可以选择使用其它。因为还有操作名可以进一步标识
     */
   int OTHER = 0;


}
