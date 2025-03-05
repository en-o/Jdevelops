package cn.tannn.jdevelops.es.antlr;


import cn.tannn.jdevelops.es.antlr.demo.EsQueryVisitor;
import cn.tannn.jdevelops.es.antlr.demo.meta.ESLexer;
import cn.tannn.jdevelops.es.antlr.demo.meta.ESParser;
import cn.tannn.jdevelops.es.antlr.tools.FieldTransformer;
import cn.tannn.jdevelops.es.antlr.tools.ValueValidator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;


/**
 * 工具类
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024-08-14 09:40:44
 */
public class ElasticSearchQueryBuilder {


    private final FieldTransformer fieldTransformer;
    private final List<ValueValidator> valueValidators;

    private ElasticSearchQueryBuilder(Builder builder) {
        this.fieldTransformer = builder.fieldTransformer;
        this.valueValidators = List.copyOf(builder.valueValidators);
    }
    /**
     * 支持的表达式
     *  <p>基础运算符          :  '==' | '!=' | '>=' | '<=' | '>' | '<' | '+='（like）
     *  <p>正则表达式匹配/不匹配 :  | '=~' | '!~'
     *  <p>包含/不包含         :  'in' | 'not in'
     *  <p>存在/不存在          :  'exists' | 'not exists'
     * @param expression  像写sql一些构建es查询[e.g "title += \"论坚定理想信念（2023年）\" and years == 2021 "]
     * @return Query
     */
    public Query buildQuery(String expression) {
        // 处理 没有引号的值数据 e.g name = tan -> name = 'tan'
        var lexer = new ESLexer(CharStreams.fromString(expression));
        var tokens = new CommonTokenStream(lexer);
        var parser = new ESParser(tokens);
        ParseTree tree = parser.expression();
        return new EsQueryVisitor(fieldTransformer, valueValidators).visit(tree);
    }
    /**
     * 构建器
     */
    public static class Builder {
        private FieldTransformer fieldTransformer = field -> field; // 默认不转换
        private final List<ValueValidator> valueValidators = new ArrayList<>();

        public Builder withFieldTransformer(FieldTransformer transformer) {
            this.fieldTransformer = transformer;
            return this;
        }

        public Builder addValueValidator(ValueValidator validator) {
            this.valueValidators.add(validator);
            return this;
        }

        public ElasticSearchQueryBuilder build() {
            return new ElasticSearchQueryBuilder(this);
        }
    }



}
