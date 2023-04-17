package cn.jdevelops.sboot.jdbctemplate.core;

import cn.jdevelops.data.jdbctemplate.annotation.Query;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

/**
 * 根据注解给接口类生成默认实现
 * @author tnnn
 */
@AutoService(Processor.class)
public class QueryProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(Query.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        for (TypeElement annotation : annotations) {
//            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
//                if (element.getKind() == ElementKind.INTERFACE) {
//                    // 根据注解生成默认实现
//                    TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("QueryServiceImpl")
//                            .addSuperinterface(ClassName.get(element.asType()));
//                    TypeSpec typeSpec = typeSpecBuilder.build();
//                    JavaFile javaFile = JavaFile.builder("cn.jdevelops.sboot.jdbctemplate", typeSpec).build();
//
//                    // 输出生成的代码
//                    try {
//                        javaFile.writeTo(System.out);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
        return true;
    }
}
