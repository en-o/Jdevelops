package cn.tannn.jdevelops.util.excel.listener;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.FastExcel;
import cn.tannn.jdevelops.util.excel.listener.ExcelDataFunListener;
import cn.tannn.jdevelops.util.excel.model.CommonExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ExcelDataFunListener 使用示例
 */
public class ExcelDataFunListenerExample {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelDataFunListenerExample.class);

    // 示例1：基本使用 - 简单数据处理
    public static void basicUsage() {
        String fileName = "user_data.xlsx";

        // 创建监听器，使用默认批处理大小（1000）
        ExcelDataFunListener<UserData> listener = new ExcelDataFunListener<>(dataList -> {
            LOG.info("处理 {} 条用户数据", dataList.size());

            // 处理每条数据
            dataList.forEach(user -> {
                LOG.debug("处理用户: {} (行号: {})", user.getName(), user.getRowIndex());
                // 这里可以进行数据验证、转换、保存等操作
            });
        });
        //   .headRowNumber(1) 设置头行数
        // 读取Excel文件
        EasyExcel.read(fileName, UserData.class, listener).sheet().doRead();
//        FastExcel.read(file.getInputStream())
//                .head(UserData.class)
//                .registerReadListener(listener)
//                .headRowNumber(1)
//                .sheet().doReadSync();
        LOG.info("Excel读取完成，总计处理: {} 行", listener.getProcessedCount());
    }

    // 示例2：自定义批处理大小
    public static void customBatchSize() {
        String fileName = "large_data.xlsx";

        // 创建监听器，自定义批处理大小为500
        ExcelDataFunListener<UserData> listener = new ExcelDataFunListener<>(
            dataList -> {
                LOG.info("批量处理 {} 条数据", dataList.size());
                // 批量保存到数据库
                batchSaveToDatabase(dataList);
            },
            500 // 自定义批处理大小
        );
        //   .headRowNumber(1) 设置头行数
        EasyExcel.read(fileName, UserData.class, listener).sheet().doRead();
    }

    // 示例3：异步处理
    public static void asyncProcessing() {
        String fileName = "async_data.xlsx";
        ExecutorService executor = Executors.newFixedThreadPool(4);

        ExcelDataFunListener<UserData> listener = new ExcelDataFunListener<>(dataList -> {
            // 异步处理数据
            CompletableFuture.runAsync(() -> {
                try {
                    LOG.info("异步处理 {} 条数据", dataList.size());
                    // 模拟耗时操作
                    Thread.sleep(100);
                    processUserData(dataList);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOG.error("异步处理被中断", e);
                }
            }, executor);
        }, 200);

        try {
            //   .headRowNumber(1) 设置头行数
            EasyExcel.read(fileName, UserData.class, listener).sheet().doRead();
        } finally {
            executor.shutdown();
        }
    }

    // 示例4：数据验证和错误处理
    public static void dataValidationExample() {
        String fileName = "validation_data.xlsx";
        List<UserData> errorData = new ArrayList<>();

        ExcelDataFunListener<UserData> listener = new ExcelDataFunListener<>(dataList -> {
            List<UserData> validData = new ArrayList<>();

            for (UserData user : dataList) {
                if (isValidUser(user)) {
                    validData.add(user);
                } else {
                    errorData.add(user);
                    LOG.warn("无效数据，行号: {}, 姓名: {}", user.getRowIndex(), user.getName());
                }
            }

            // 只处理有效数据
            if (!validData.isEmpty()) {
                LOG.info("处理 {} 条有效数据", validData.size());
                batchSaveToDatabase(validData);
            }
        });
        //   .headRowNumber(1) 设置头行数
        EasyExcel.read(fileName, UserData.class, listener).sheet().doRead();

        LOG.info("处理完成，错误数据: {} 条", errorData.size());
    }

    // 示例5：带进度监控的处理
    public static void progressMonitoring() {
        String fileName = "progress_data.xlsx";

        ExcelDataFunListener<UserData> listener = new ExcelDataFunListener<>(dataList -> {
            LOG.info("当前批次处理: {} 条，总计已处理: {} 条",
                dataList.size(), getCurrentProcessedCount());

            // 处理数据
            processUserData(dataList);

            // 更新进度
            updateProgress(dataList.size());
        }, 1000);
        //   .headRowNumber(1) 设置头行数
        EasyExcel.read(fileName, UserData.class, listener).sheet().doRead();
    }

    // 示例6：多Sheet处理
    public static void multiSheetProcessing() {
        String fileName = "multi_sheet_data.xlsx";

        // 处理第一个Sheet
        ExcelDataFunListener<UserData> userListener = new ExcelDataFunListener<>(dataList -> {
            LOG.info("处理用户数据: {} 条", dataList.size());
            processUserData(dataList);
        });

        // 处理第二个Sheet
        ExcelDataFunListener<OrderData> orderListener = new ExcelDataFunListener<>(dataList -> {
            LOG.info("处理订单数据: {} 条", dataList.size());
            processOrderData(dataList);
        });
        //   .headRowNumber(1) 设置头行数
        // 读取不同Sheet
        EasyExcel.read(fileName, UserData.class, userListener).sheet("用户数据").doRead();
        EasyExcel.read(fileName, OrderData.class, orderListener).sheet("订单数据").doRead();
    }

    // 工具方法
    private static void batchSaveToDatabase(List<UserData> dataList) {
        // 模拟批量保存到数据库
        LOG.info("保存 {} 条数据到数据库", dataList.size());
    }

    private static void processUserData(List<UserData> dataList) {
        // 模拟处理用户数据
        dataList.forEach(user -> {
            // 数据处理逻辑
        });
    }

    private static void processOrderData(List<OrderData> dataList) {
        // 模拟处理订单数据
        dataList.forEach(order -> {
            // 数据处理逻辑
        });
    }

    private static boolean isValidUser(UserData user) {
        // 数据验证逻辑
        return user.getName() != null && !user.getName().trim().isEmpty();
    }

    private static long getCurrentProcessedCount() {
        // 获取当前处理进度
        return 0; // 示例值
    }

    private static void updateProgress(int processedCount) {
        // 更新处理进度
        LOG.info("更新进度: +{}", processedCount);
    }

    // 示例数据模型
    public static class UserData extends CommonExcel {
        private String name;
        private Integer age;
        private String email;

        // getter/setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class OrderData extends CommonExcel {
        private String orderNo;
        private String productName;
        private Integer quantity;

        // getter/setter
        public String getOrderNo() { return orderNo; }
        public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    // 主方法演示
    public static void main(String[] args) {
        LOG.info("=== 基本使用示例 ===");
        basicUsage();

        LOG.info("=== 自定义批处理大小示例 ===");
        customBatchSize();

        LOG.info("=== 数据验证示例 ===");
        dataValidationExample();

        LOG.info("=== 多Sheet处理示例 ===");
        multiSheetProcessing();
    }
}
