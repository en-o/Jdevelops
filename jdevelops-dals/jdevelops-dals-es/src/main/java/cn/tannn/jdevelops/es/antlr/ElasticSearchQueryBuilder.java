package cn.tannn.jdevelops.es.antlr;


import cn.tannn.jdevelops.es.antlr.meta.ESLexer;
import cn.tannn.jdevelops.es.antlr.meta.ESParser;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


/**
 * 工具类
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024-08-14 09:40:44
 */
public class ElasticSearchQueryBuilder {

    public Query buildQuery(String expression) {
        ESLexer lexer = new ESLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ESParser parser = new ESParser(tokens);
        ParseTree tree = parser.expression();
        return new EsQueryVisitor().visit(tree);
    }

}
