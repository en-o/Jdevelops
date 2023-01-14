package cn.jdevelops.jdbctemplate.aspect;

import cn.jdevelops.jdbctemplate.annotation.Delete;
import cn.jdevelops.jdbctemplate.util.GetterProcessorUtil;
import cn.jdevelops.jdbctemplate.util.SetterProcessorUtil;
import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;
/**
 * @author nzc
 */
@SupportedAnnotationTypes({
        "cn.jdevelops.jdbctemplate.annotation.Delete",
        "cn.jdevelops.jdbctemplate.annotation.MyGetter",
        "cn.jdevelops.jdbctemplate.annotation.MySetter",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class JdbcAnnotationProcessor extends AbstractProcessor {

    /**
     * 提供了待处理的抽象语法树
     */
    private JavacTrees javacTrees;
    /**
     * 封装了创建AST节点的一些方法
     */
    private TreeMaker treeMaker;
    /**
     * 提供了创建标识符的方法
     */
    private Names names;


    /**
     * 从Context中初始化JavacTrees，TreeMaker，Names
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        javacTrees = JavacTrees.instance(processingEnv);
        treeMaker = TreeMaker.instance(context);
        names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        SetterProcessorUtil setterProcessor = new SetterProcessorUtil(javacTrees, treeMaker, roundEnv, names);
        setterProcessor.setterProcessHandle();
        GetterProcessorUtil getterProcessor = new GetterProcessorUtil(javacTrees, treeMaker, roundEnv, names);
        getterProcessor.getterProcessHandle();
        return true;
    }
}
