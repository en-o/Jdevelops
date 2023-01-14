package cn.jdevelops.jdbctemplate.util;

import cn.jdevelops.jdbctemplate.annotation.MySetter;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
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
 * 增加set方法的util
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-01-13 18:02
 */
public class SetterProcessorUtil {
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

    public SetterProcessorUtil(JavacTrees javacTrees, TreeMaker treeMaker, RoundEnvironment roundEnv, Names names) {
        this.javacTrees = javacTrees;
        this.treeMaker = treeMaker;
        this.roundEnv = roundEnv;
        this.names = names;
    }

    /**
     * 执行方法
     */
    public  void setterProcessHandle(){
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(MySetter.class);
        for (Element element : set) {
            JCTree tree = javacTrees.getTree(element);
            tree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    jcClassDecl.defs.stream()
                            .filter(it -> it.getKind().equals(Tree.Kind.VARIABLE))
                            .map(it -> (JCTree.JCVariableDecl) it)
                            .forEach(it -> {
                                jcClassDecl.defs = jcClassDecl.defs.prepend(genSetterMethod(it));
                            });
                    super.visitClassDef(jcClassDecl);
                }
            });
        }
    }

    private JCTree.JCMethodDecl genSetterMethod(JCTree.JCVariableDecl jcVariableDecl) {
        // this.xxx=xxx
        JCTree.JCExpressionStatement statement = treeMaker.Exec(
                treeMaker.Assign(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("this")),
                                jcVariableDecl.getName()
                        ),
                        treeMaker.Ident(jcVariableDecl.getName())
                )
        );
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>().append(statement);
        // set方法参数
        JCTree.JCVariableDecl param = treeMaker.VarDef(
                // 访问修饰符
                treeMaker.Modifiers(Flags.PARAMETER, List.nil()),
                // 变量名
                jcVariableDecl.name,
                //变量类型
                jcVariableDecl.vartype,
                // 变量初始值
                null
        );
        // 方法访问修饰符 public
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        // 方法名(setXxx)，根据字段名生成首选字母大写的set方法
        Name setMethodName = createSetMethodName(jcVariableDecl.getName());
        // 返回值类型void
        JCTree.JCExpression returnMethodType = treeMaker.Type(new Type.JCVoidType());
        // 生成方法体
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        // 泛型参数列表
        List<JCTree.JCTypeParameter> methodGenericParamList = List.nil();
        // 参数值列表
        List<JCTree.JCVariableDecl> parameterList = List.of(param);
        // 异常抛出列表
        List<JCTree.JCExpression> throwCauseList = List.nil();
        // 生成方法定义语法树节点
        return treeMaker.MethodDef(
                // 方法级别访问修饰符
                modifiers,
                // set 方法名
                setMethodName,
                // 返回值类型
                returnMethodType,
                // 泛型参数列表
                methodGenericParamList,
                // 参数值列表
                parameterList,
                // 异常抛出列表
                throwCauseList,
                // 方法体
                body,
                // 默认值
                null
        );
    }

    private Name createSetMethodName(Name variableName) {
        String fieldName = variableName.toString();
        return names.fromString("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
    }
}
