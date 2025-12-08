package cn.tannn.jdevelops.jdectemplate.xmlmapper.node;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlContext;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Choose/When/Otherwise 节点测试
 *
 * @author tnnn
 */
class ChooseSqlNodeTest {

    /**
     * 测试 choose 节点 - when 条件匹配
     */
    @Test
    void testChooseWithMatchingWhen() {
        // 准备参数
        Map<String, Object> param = new HashMap<>();
        param.put("orgNo", "ORG001");

        // 创建 when 节点
        List<SqlNode> whenContents = new ArrayList<>();
        whenContents.add(new TextSqlNode("AND a.org_no = 'ORG001'"));
        WhenSqlNode whenNode = new WhenSqlNode("orgNo != null && orgNo != '-'", whenContents);

        List<SqlNode> whenNodes = new ArrayList<>();
        whenNodes.add(whenNode);

        // 创建 otherwise 节点
        List<SqlNode> otherwiseContents = new ArrayList<>();
        otherwiseContents.add(new TextSqlNode("AND a.org_no IS NULL"));
        OtherwiseSqlNode otherwiseNode = new OtherwiseSqlNode(otherwiseContents);

        // 创建 choose 节点
        ChooseSqlNode chooseNode = new ChooseSqlNode(whenNodes, otherwiseNode);

        // 执行
        SqlContext context = new SqlContext();
        boolean applied = chooseNode.apply(context, param);

        // 验证
        assertTrue(applied);
        String sql = context.getSql();
        assertTrue(sql.contains("AND a.org_no = 'ORG001'"));
        assertFalse(sql.contains("IS NULL"));
    }

    /**
     * 测试 choose 节点 - when 条件不匹配，执行 otherwise
     */
    @Test
    void testChooseWithOtherwise() {
        // 准备参数
        Map<String, Object> param = new HashMap<>();
        param.put("orgNo", "-");

        // 创建 when 节点
        List<SqlNode> whenContents = new ArrayList<>();
        whenContents.add(new TextSqlNode("AND a.org_no = 'ORG001'"));
        WhenSqlNode whenNode = new WhenSqlNode("orgNo != null && orgNo != '-'", whenContents);

        List<SqlNode> whenNodes = new ArrayList<>();
        whenNodes.add(whenNode);

        // 创建 otherwise 节点
        List<SqlNode> otherwiseContents = new ArrayList<>();
        otherwiseContents.add(new TextSqlNode("AND a.org_no IS NULL"));
        OtherwiseSqlNode otherwiseNode = new OtherwiseSqlNode(otherwiseContents);

        // 创建 choose 节点
        ChooseSqlNode chooseNode = new ChooseSqlNode(whenNodes, otherwiseNode);

        // 执行
        SqlContext context = new SqlContext();
        boolean applied = chooseNode.apply(context, param);

        // 验证
        assertTrue(applied);
        String sql = context.getSql();
        assertFalse(sql.contains("AND a.org_no = 'ORG001'"));
        assertTrue(sql.contains("AND a.org_no IS NULL"));
    }

    /**
     * 测试 choose 节点 - 多个 when 条件，匹配第一个
     */
    @Test
    void testChooseWithMultipleWhen() {
        // 准备参数
        Map<String, Object> param = new HashMap<>();
        param.put("status", 1);

        List<SqlNode> whenNodes = new ArrayList<>();

        // 第一个 when - 应该匹配
        List<SqlNode> when1Contents = new ArrayList<>();
        when1Contents.add(new TextSqlNode("AND status = 1"));
        whenNodes.add(new WhenSqlNode("status == 1", when1Contents));

        // 第二个 when - 不会被执行
        List<SqlNode> when2Contents = new ArrayList<>();
        when2Contents.add(new TextSqlNode("AND status = 2"));
        whenNodes.add(new WhenSqlNode("status == 2", when2Contents));

        // 创建 choose 节点（没有 otherwise）
        ChooseSqlNode chooseNode = new ChooseSqlNode(whenNodes, null);

        // 执行
        SqlContext context = new SqlContext();
        boolean applied = chooseNode.apply(context, param);

        // 验证 - 只有第一个 when 被执行
        assertTrue(applied);
        String sql = context.getSql();
        assertTrue(sql.contains("AND status = 1"));
        assertFalse(sql.contains("AND status = 2"));
    }

    /**
     * 测试 choose 节点 - 没有 otherwise，所有 when 都不匹配
     */
    @Test
    void testChooseWithNoMatchAndNoOtherwise() {
        // 准备参数
        Map<String, Object> param = new HashMap<>();
        param.put("status", 3);

        List<SqlNode> whenNodes = new ArrayList<>();

        // when 条件都不匹配
        List<SqlNode> when1Contents = new ArrayList<>();
        when1Contents.add(new TextSqlNode("AND status = 1"));
        whenNodes.add(new WhenSqlNode("status == 1", when1Contents));

        List<SqlNode> when2Contents = new ArrayList<>();
        when2Contents.add(new TextSqlNode("AND status = 2"));
        whenNodes.add(new WhenSqlNode("status == 2", when2Contents));

        // 创建 choose 节点（没有 otherwise）
        ChooseSqlNode chooseNode = new ChooseSqlNode(whenNodes, null);

        // 执行
        SqlContext context = new SqlContext();
        boolean applied = chooseNode.apply(context, param);

        // 验证 - 没有任何内容被添加
        assertFalse(applied);
        String sql = context.getSql();
        assertTrue(sql.isEmpty());
    }

    /**
     * 测试实际场景 - orgNo 判断
     */
    @Test
    void testRealScenarioOrgNo() {
        // 场景 1: orgNo 为正常值
        Map<String, Object> param1 = new HashMap<>();
        param1.put("orgNo", "ORG001");

        ChooseSqlNode chooseNode = createOrgNoChooseNode();
        SqlContext context1 = new SqlContext();
        chooseNode.apply(context1, param1);

        String sql1 = context1.getSql();
        assertTrue(sql1.contains("AND a.org_no ="));

        // 场景 2: orgNo 为 "-"
        Map<String, Object> param2 = new HashMap<>();
        param2.put("orgNo", "-");

        SqlContext context2 = new SqlContext();
        chooseNode.apply(context2, param2);

        String sql2 = context2.getSql();
        assertTrue(sql2.contains("AND a.org_no IS NULL"));
    }

    /**
     * 创建 orgNo 的 choose 节点（模拟实际使用场景）
     */
    private ChooseSqlNode createOrgNoChooseNode() {
        List<SqlNode> whenNodes = new ArrayList<>();

        // when: orgNo != '-'
        List<SqlNode> whenContents = new ArrayList<>();
        whenContents.add(new MixedSqlNode("AND a.org_no = #{orgNo}"));
        whenNodes.add(new WhenSqlNode("orgNo != '-'", whenContents));

        // otherwise: orgNo == '-'
        List<SqlNode> otherwiseContents = new ArrayList<>();
        otherwiseContents.add(new TextSqlNode("AND a.org_no IS NULL"));
        OtherwiseSqlNode otherwiseNode = new OtherwiseSqlNode(otherwiseContents);

        return new ChooseSqlNode(whenNodes, otherwiseNode);
    }
}
