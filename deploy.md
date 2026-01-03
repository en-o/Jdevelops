# 发布版本
> - 只发布非SNAPSHOT版本
> - maven配置记得同时勾选 ossrh 和 release
1. 请用 maven 3.9+
2. 将 SNAPSHOT 版本修改成发布版（只改发布的版本号）
3. 执行发版 `mvn clean deploy -P release`

# 单独发版 （fix发版）
> 1. 有模块需要单独发版的时候就根据下面的步骤操作
> 2. 自己模块的pom.xml新增 `<version>单独的版本号</version>`
> 3. parent的pom.xml 新增 profile 配置，参考下面的配置
> 4. 发布的时候注意 加/勾 上 `-P deploy-jwt-only`
> 5. 弄完记得删除
> ps: 因为 parent 被发过之后不允许发了，所以不能用父 pom.xml 发版 

自己模块的pom.xml
```xml
 <version>1.0.3.1</version>
```
parent的pom.xml (参考下面的配置)    
```xml
<profiles>
    <!-- 单独发布 jdevelops-apis-jwt 的 Profile -->
    <profile>
        <id>deploy-jwt-only</id>
        <build>
            <plugins>
                <!-- 覆盖父 POM 的 central-publishing-maven-plugin 配置 -->
                <plugin>
                    <groupId>org.sonatype.central</groupId>
                    <artifactId>central-publishing-maven-plugin</artifactId>
                    <version>0.4.0</version>
                    <extensions>true</extensions>
                    <configuration>
                        <publishingServerId>central</publishingServerId>
                        <tokenAuth>true</tokenAuth>
                        <autoPublish>true</autoPublish>
                        <!--此次发布的线程名-->
                        <deploymentName>jdevelops-apis-jwt-fix</deploymentName>
                        <!-- 排除其他模块，只发布 jdevelops-apis-jwt -->
                        <excludeArtifacts>
                            <excludeArtifact>cn.tannn.jdevelops:jdevelops-apis-result</excludeArtifact>
                            <excludeArtifact>cn.tannn.jdevelops:jdevelops-apis-exception</excludeArtifact>
                            <excludeArtifact>cn.tannn.jdevelops:jdevelops-apis-knife4j</excludeArtifact>
                            <excludeArtifact>cn.tannn.jdevelops:jdevelops-apis-version</excludeArtifact>
                            <excludeArtifact>cn.tannn.jdevelops:jdevelops-apis-log</excludeArtifact>
                            <excludeArtifact>cn.tannn.jdevelops:jdevelops-apis-idempotent</excludeArtifact>
                            <excludeArtifact>cn.tannn.jdevelops:jdevelops-apis-sign</excludeArtifact>
                            <!--包括自己-->
                            <excludeArtifact>cn.tannn.jdevelops:jdevelops-apis</excludeArtifact>
                            <!-- 不排除 jdevelops-apis-jwt，因此它会被发布 -->
                        </excludeArtifacts>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```
