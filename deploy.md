# 发布版本
1. 只发布非SNAPSHOT版本


# 如果 module 中存在需要 exclude掉的Artifact流程如下
1. 将 SNAPSHOT 版本修改成发布版（只改发布的版本号）
2. 注释掉  pom.xml 中 modules 里暂时不需要 deploy 的 module
3. 执行发版 `mvn clean deploy -P release`
4. 将注释掉的 module 恢复
