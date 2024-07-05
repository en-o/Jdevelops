package cn.tannn.jdevelops.frameworks.web.starter.entity.url;


/**
 * 接口集合
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-20 11:07
 */
public class Urls {

    /**
     * 分组
     */
    private String grouping;

    /**
     * 接口中文名
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口请求类型
     */
    private String requestMethod;

    public Urls() {
    }

    public Urls(String grouping, String description, String url, String requestMethod) {
        this.grouping = grouping;
        this.description = description;
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public String toString() {
        return "Urls{" +
                "grouping='" + grouping + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                '}';
    }
}
