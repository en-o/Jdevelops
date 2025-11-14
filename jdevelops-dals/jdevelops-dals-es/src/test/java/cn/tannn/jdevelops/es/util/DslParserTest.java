package cn.tannn.jdevelops.es.util;

import org.junit.jupiter.api.Test;

class DslParserTest {

    @Test
    void parse() {

        String dslQuery = """
                  POST /citation_comment/_search?typed_keys=true {"_source":{"excludes":["fullText"],"includes":["section","sentence","spoInfos","referenceAuthorNames","referenceDoi","referenceIssue","referenceJournal","referenceNumber","referencePages","referencePmcid","referencePmid","referencePubDate","referenceTitle","referenceUid","referenceVolume","referenceYear","paperAuthorNames","paperDoi","paperIssue","paperJournal","paperPages","paperPmcid","paperPmid","paperPubDate","paperTitle","paperVolume","paperYear"]},"from":0,"highlight":{"type":"unified","fragment_size":800000,"number_of_fragments":0,"post_tags":["</span>"],"pre_tags":["<span style='color:red'>"],"require_field_match":false,"fields":{"sentence":{}}},"query":{"bool":{"must":[{"bool":{"must":[{"match_phrase":{"paperAuthorNames":{"query":"Benjamin"}}},{"match_phrase":{"sentence":{"query":"we"}}}]}}]}},"size":10}
                """;

//        String dslQuery = """
//                 POST /citation_spo/_search?typed_keys=true {"_source":{"excludes":["fullText"],"includes":["articleType","subjectName","predicate","objectName","text","pmid","ctPmid","articlePubDate"]},"from":0,"highlight":{"type":"unified","fragment_size":800000,"number_of_fragments":0,"post_tags":["</span>"],"pre_tags":["<span style='color:red'>"],"require_field_match":false,"fields":{"text":{}}},"query":{"bool":{"must":[{"bool":{"must":[{"term":{"articleType":{"value":"journal"}}},{"term":{"source":{"value":"语句"}}}]}}]}},"size":10}
//                """;
        DslParser parser = new DslParser();
        DslParser.ParseResult result = parser.parse(dslQuery);
        System.out.println("SQL Query:");
        System.out.println(result.getSql());
        System.out.println("\nParameters:");
        System.out.println(result.getParameters());
        System.out.println("\nWhere SQL Query:");
        System.out.println(result.getWhereSql());
        System.out.println("\nWhere  Query Str:");
        System.out.println(result.getQueryStr());
    }
}
