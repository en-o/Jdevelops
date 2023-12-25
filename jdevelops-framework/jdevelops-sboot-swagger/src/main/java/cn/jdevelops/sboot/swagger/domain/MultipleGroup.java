package cn.jdevelops.sboot.swagger.domain;

import java.util.List;

/**
 * 多组接口
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/12/25 11:44
 */
public class MultipleGroup {

    /**
     * controller接口所在的包(可以设置多个)，eg. cn.jdevelops.controller(默认)
     */
    private List<String> basePackage;

    /**
     *  分组 必须英文 e.g jdevelopsAPI
     */
    private String groupName;

    /**
     *  分组名
     */
    private String displayName;

    public MultipleGroup() {
    }

    public MultipleGroup(List<String> basePackage, String groupName, String displayName) {
        this.basePackage = basePackage;
        this.groupName = groupName;
        this.displayName = displayName;
    }

    public List<String> getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(List<String> basePackage) {
        this.basePackage = basePackage;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public String toString() {
        return "MultipleGroup{" +
                "basePackage=" + basePackage +
                ", groupName='" + groupName + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
