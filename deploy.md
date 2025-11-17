# 发布版本
> - 只发布非SNAPSHOT版本
> - maven配置记得同时勾选 ossrh 和 release
1. 请用 maven 3.9+
2. 将 SNAPSHOT 版本修改成发布版（只改发布的版本号）
3. 执行发版 `mvn clean deploy -P release`

# 单独发版 （特殊发版）
> 1. 有时候需要单独发版的时候就把下面的代码复制到自己的pom.xml里去
> 2. 弄完记得删除
> ps: 因为 parent 被发过之后不允许发了，所以不能用父 pom.xml 发版 
```xml
<url>https://github.com/en-o/Jdevelops</url>

    <organization>
        <name>tan</name>
        <url>https://t.tannn.cn/</url>
    </organization>

    <!-- 开源许可证 -->
    <licenses>
        <license>
            <name>The Apache License, Version 2.0</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
    </licenses>
    <!-- 开源地址 -->
    <scm>
        <!-- 发布分支 -->
        <tag>master</tag>
        <url>https://github.com/en-o/Jdevelops.git</url>
        <connection>https://github.com/en-o/Jdevelops.git</connection>
        <developerConnection>https://github.com/en-o/Jdevelops.git</developerConnection>
    </scm>

    <!-- 开发者信息 -->
    <developers>
        <developer>
            <id>tanning</id>
            <name>tanning</name>
            <email>1445763190@qq.com</email>
            <organization>cn.tannn.jdevelops</organization>
            <url>https://t.tannn.cn/</url>
            <roles>
                <role>author</role>
            </roles>
        </developer>
    </developers>
        <!--定义项目的分发管理，会在执行 deploy 命令时使用-->
    <distributionManagement>
    <snapshotRepository>
        <id>ali-snapshots</id>
        <url>https://packages.aliyun.com/619f9ba2cb55679b040c4b21/maven/2159763-snapshot-hps61l</url>
    </snapshotRepository>
    <repository>
        <id>ossrh</id>
        <url>https://repo1.maven.org/maven2</url>
    </repository>
    </distributionManagement>
    
            <!--Maven 会按照 pom.xml 中定义的顺序来查找依赖项-->
    <repositories>
    <!-- 快照仓库：阿里云私有仓库 -->
    <repository>
        <id>ali-snapshots</id>
        <url>https://packages.aliyun.com/619f9ba2cb55679b040c4b21/maven/2159763-snapshot-hps61l</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
    
    <!-- 发布仓库：Maven Central -->
    <repository>
        <id>central</id>
        <url>https://repo1.maven.org/maven2</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    </repositories>
```
