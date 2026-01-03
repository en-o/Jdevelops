# 发布版本
> - 只发布非SNAPSHOT版本
> - maven配置记得同时勾选 ossrh 和 release
1. 请用 maven 3.9+
2. 将 SNAPSHOT 版本修改成发布版（只改发布的版本号）
3. 执行发版 `mvn clean deploy -P release`

# 单独发版 （fix发版）
> 1. 有模块需要单独发版的时候就根据下面的步骤操作
> 2. 自己模块的pom.xml 修改 parent的`<version>使用正式版本的版本号</version>`
> 3. 自己模块的pom.xml 新增 `<version>单独的版本号</version>`
> 4. 然后定位到自己模块目录下执行 `mvn clean deploy -P release` / idea maven 插件选择 自己模块 然后点击 deploy
> 5. 弄完记得删除
> ps: 因为 parent 被发过之后不允许发了，所以不能用父 pom.xml 发版 

自己模块的pom.xml
```xml
    <parent>
        <groupId>cn.tannn.jdevelops</groupId>
        <artifactId>jdevelops-xxx</artifactId>
        <version>使用正式版本的版本号</version>
    </parent>

    <version>你的fix版本号</version>

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
            <id>ossrh</id>
            <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
        </snapshotRepository>
        <repository>
            <id>ossrh</id>
            <url>https://repo1.maven.org/maven2</url>
        </repository>
    </distributionManagement>

    <!--Maven 会按照 pom.xml 中定义的顺序来查找依赖项-->
    <repositories>
        <repository>
            <id>snapshot</id>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <releases>
                <enabled>false</enabled>
            </releases>
            <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
        </repository>
    </repositories>
```

