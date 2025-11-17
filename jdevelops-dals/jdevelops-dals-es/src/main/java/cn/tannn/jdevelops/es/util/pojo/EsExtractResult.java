package cn.tannn.jdevelops.es.util.pojo;


public class EsExtractResult {
    private String tableName;
    private String jsonBody;

    public EsExtractResult(String tableName, String jsonBody) {
        this.tableName = tableName;
        this.jsonBody = jsonBody;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getJsonBody() {
        return jsonBody;
    }

    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }

    @Override
    public String toString() {
        return "表名: " + tableName + "\nJSON: " + jsonBody;
    }
}
