package cn.jdevelops.jdbctemplate.util;

import cn.jdevelops.jdbctemplate.annotation.MyGetter;
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

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.Set;

/**
 * 增加get方法的util
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-13 18:02
 */
public class GetterProcessorUtil {
    /**
     * 提供了待处理的抽象语法树
     */
    private JavacTrees javacTrees;
    /**
     * 封装了创建AST节点的一些方法
     */
    private TreeMaker treeMaker;

    private RoundEnvironment roundEnv;

    /**
     * 提供了创建标识符的方法
     */
    private Names names;

    public GetterProcessorUtil(JavacTrees javacTrees, TreeMaker treeMaker, RoundEnvironment roundEnv, Names names) {
        this.javacTrees = javacTrees;
        this.treeMaker = treeMaker;
        this.roundEnv = roundEnv;
        this.names = names;
    }

    /**
     * 执行方法
     */
    public  void getterProcessHandle( ){
        // 返回使用给定注释类型注释的元素的集合。
        Set<? extends Element> get = roundEnv.getElementsAnnotatedWith(MyGetter.class);
        for (Element element : get) {
            // 获取当前类的抽象语法树
            JCTree tree = javacTrees.getTree(element);
            // 获取抽象语法树的所有节点
            // Visitor 抽象内部类，内部定义了访问各种语法节点的方法
            tree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    // 在抽象树中找出所有的变量
                    // 过滤，只处理变量类型
                    jcClassDecl.defs.stream()
                            .filter(it -> it.getKind().equals(Tree.Kind.VARIABLE))
                            // 类型强转
                            .map(it -> (JCTree.JCVariableDecl) it)
                            .forEach(it -> {
                                // 对于变量进行生成方法的操作
                                jcClassDecl.defs = jcClassDecl.defs.prepend(genGetterMethod(it));
                            });
                    super.visitClassDef(jcClassDecl);
                }
            });
        }
    }

    private JCTree.JCMethodDecl genGetterMethod(JCTree.JCVariableDecl jcVariableDecl) {
        // 生成return语句，return this.xxx
        JCTree.JCReturn returnStatement = treeMaker.Return(
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("this")),
                        jcVariableDecl.getName()
                )
        );
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>().append(returnStatement);
        // public 方法访问级别修饰
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        // 方法名 getXXX ，根据字段名生成首字母大写的get方法
        Name getMethodName = createGetMethodName(jcVariableDecl.getName());
        // 返回值类型，get类型的返回值类型与字段类型一致
        JCTree.JCExpression returnMethodType = jcVariableDecl.vartype;
        // 生成方法体
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        // 泛型参数列表
        List<JCTree.JCTypeParameter> methodGenericParamList = List.nil();
        // 参数值列表
        List<JCTree.JCVariableDecl> parameterList = List.nil();
        // 异常抛出列表
        List<JCTree.JCExpression> throwCauseList = List.nil();
        // 生成方法定义树节点
        return treeMaker.MethodDef(
                // 方法访问级别修饰符
                modifiers,
                // get 方法名
                getMethodName,
                // 返回值类型
                returnMethodType,
                // 泛型参数列表
                methodGenericParamList,
                //参数值列表
                parameterList,
                // 异常抛出列表
                throwCauseList,
                // 方法默认体
                body,
                // 默认值
                null
        );
    }

    private Name createGetMethodName(Name variableName) {
        String fieldName = variableName.toString();
        return names.fromString("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
    }
}
