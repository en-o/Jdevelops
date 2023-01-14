package cn.jdevelops.jdbctemplate.util;

import cn.jdevelops.jdbctemplate.annotation.Delete;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 创建文件
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-13 18:07
 */
public class CreateFileProcessorUtil {
    /**
     * 提供了待处理的抽象语法树
     */
    private JavacTrees javacTrees;
    /**
     * 封装了创建AST节点的一些方法
     */
    private TreeMaker treeMaker;

    private RoundEnvironment roundEnv;

    private ProcessingEnvironment processingEnv;

    /**
     * 提供了创建标识符的方法
     */
    private Names names;

    public CreateFileProcessorUtil(JavacTrees javacTrees, TreeMaker treeMaker,
                                   RoundEnvironment roundEnv,
                                   Names names, ProcessingEnvironment processingEnvironment) {
        this.javacTrees = javacTrees;
        this.treeMaker = treeMaker;
        this.roundEnv = roundEnv;
        processingEnv = processingEnvironment;
        this.names = names;
    }

    /**
     * 执行方法
     * @param annotation 指定注解干活
     */
    public  void processHandle( Class<? extends Annotation> annotation){
        //创建动态代码，实际上就是创建一个String, 写入到文件里
        //然后文件会被解释为.class文件

        StringBuilder builder = new StringBuilder()
                .append("package com.zhangjian.annotationprocessor.generated;\n\n")
                .append("public class GeneratedClass {\n\n")
                .append("\tpublic String getMessage() {\n")
                .append("\t\treturn \"");

        //获取所有被CustomAnnotation修饰的代码元素
        for (Element element : roundEnv.getElementsAnnotatedWith(Delete.class)) {
            String objectType = element.getSimpleName().toString();
            builder.append(objectType).append(" exists!\\n");
        }

        builder.append("\";\n")
                .append("\t}\n")
                .append("}\n");

        //将String写入并生成.class文件
        try {
            JavaFileObject source = processingEnv.getFiler().createSourceFile(
                    "com.zhangjian.annotationprocessor.generated.GeneratedClass");

            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            //
        }
    }

}
