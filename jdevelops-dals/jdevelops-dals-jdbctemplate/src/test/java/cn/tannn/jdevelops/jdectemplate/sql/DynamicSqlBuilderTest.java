package cn.tannn.jdevelops.jdectemplate.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DynamicSqlBuilder Tests")
class DynamicSqlBuilderTest {

    private DynamicSqlBuilder builder;
    private final String BASE_SQL = "SELECT * FROM users";

    @BeforeEach
    void setUp() {
        builder = new DynamicSqlBuilder(BASE_SQL);
    }

    @Nested
    @DisplayName("Basic Conditions Tests")
    class BasicConditionsTests {

        @Test
        @DisplayName("Should create basic equals condition with positional parameters")
        void shouldCreateBasicEqualsCondition() {
            builder.addCondition("name", "John");

            assertEquals("SELECT * FROM users WHERE name = ?", builder.getSql());
            assertArrayEquals(new Object[]{"John"}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create multiple AND conditions")
        void shouldCreateMultipleAndConditions() {
            builder.addCondition("name", "John")
                    .addCondition("age", 25);

            assertEquals("SELECT * FROM users WHERE name = ? AND age = ?", builder.getSql());
            assertArrayEquals(new Object[]{"John", 25}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should handle null values properly")
        void shouldHandleNullValues() {
            builder.addCondition("name", null)
                    .addCondition("age", 25);

            assertEquals("SELECT * FROM users WHERE age = ?", builder.getSql());
            assertArrayEquals(new Object[]{25}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create IS NULL condition")
        void shouldCreateIsNullCondition() {
            builder.addIsNullCondition("deleted_at");

            assertEquals("SELECT * FROM users WHERE deleted_at IS NULL", builder.getSql());
            assertEquals(0, builder.getPositionalParams().length);
        }
    }

    @Nested
    @DisplayName("Named Parameters Tests")
    class NamedParametersTests {

        private DynamicSqlBuilder namedBuilder;

        @BeforeEach
        void setUp() {
            namedBuilder = new DynamicSqlBuilder(BASE_SQL, ParameterMode.NAMED);
        }

        @Test
        @DisplayName("Should create basic equals condition with named parameters")
        void shouldCreateBasicNamedCondition() {
            namedBuilder.addCondition("name", "userName", "John");

            assertEquals("SELECT * FROM users WHERE name = :userName", namedBuilder.getSql());
            assertEquals("John", namedBuilder.getNamedParams().getValue("userName"));
        }

        @Test
        @DisplayName("Should create multiple AND conditions with named parameters")
        void shouldCreateMultipleNamedConditions() {
            namedBuilder.addCondition("name", "userName", "John")
                    .addCondition("age", "userAge", 25);

            String expectedSql = "SELECT * FROM users WHERE name = :userName AND age = :userAge";
            assertEquals(expectedSql, namedBuilder.getSql());

            MapSqlParameterSource params = namedBuilder.getNamedParams();
            assertEquals("John", params.getValue("userName"));
            assertEquals(25, params.getValue("userAge"));
        }
    }

    @Nested
    @DisplayName("Complex AND/OR Conditions Tests")
    class ComplexConditionsTests {

        @Test
        @DisplayName("Should create complex OR conditions")
        void shouldCreateComplexOrConditions() {
            builder.addCondition("status", "active")
                    .or(or -> or.addCondition("age", 18)
                            .addCondition("role", "admin"))
                    .addCondition("deleted", false);

            String expected = "SELECT * FROM users WHERE status = ? AND (age = ? OR role = ?) AND deleted = ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"active", 18, "admin", false}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create nested OR conditions")
        void shouldCreateNestedOrConditions() {
            builder.addCondition("status", "active")
                    .or(or -> or.addCondition("age", 18)
                            .or(nested -> nested.addCondition("role", "admin")
                                    .addCondition("department", "IT")))
                    .addCondition("deleted", false);

            String expected = "SELECT * FROM users WHERE status = ? AND (age = ? OR (role = ? OR department = ?)) AND deleted = ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"active", 18, "admin", "IT", false}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create OR condition group")
        void shouldCreateOrConditionGroup() {
            DynamicSqlBuilder builder1 = new DynamicSqlBuilder("SELECT 1")
                    .addCondition("age", 18);
            DynamicSqlBuilder builder2 = new DynamicSqlBuilder("SELECT 1")
                    .addCondition("role", "admin");

            builder.addCondition("status", "active")
                    .addOrConditionGroup(builder1, builder2)
                    .addCondition("deleted", false);

            String expected = "SELECT * FROM users WHERE status = ? AND (age = ? OR role = ?) AND deleted = ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"active", 18, "admin", false}, builder.getPositionalParams());
        }
    }

    @Nested
    @DisplayName("Pagination Tests")
    class PaginationTests {

        @Test
        @DisplayName("Should add pagination with LIMIT")
        void shouldAddPagination() {
            builder.addCondition("status", "active")
                    .addPagination(2, 10);

            String expected = "SELECT * FROM users WHERE status = ? LIMIT ?, ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"active", 10, 10}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should add pagination with ORDER BY")
        void shouldAddPaginationWithOrderBy() {
            builder.addCondition("status", "active")
                    .addOrderBy("name DESC")
                    .addPagination(2, 10);

            String expected = "SELECT * FROM users WHERE status = ? ORDER BY name DESC LIMIT ?, ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"active", 10, 10}, builder.getPositionalParams());
        }
    }

    @Nested
    @DisplayName("Dynamic Conditions Tests")
    class DynamicConditionsTests {

        @Test
        @DisplayName("Should handle dynamic conditions based on null strategy")
        void shouldHandleDynamicConditions() {
            builder.addDynamicCondition("name", "John", NullHandleStrategy.IGNORE)
                    .addDynamicCondition("age", null, NullHandleStrategy.NULL_AS_IS_NULL)
                    .addDynamicCondition("email", "", NullHandleStrategy.EMPTY_AS_IS_NULL);

            String expected = "SELECT * FROM users WHERE name = ? AND age IS NULL AND email IS NULL";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"John"}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should handle dynamic LIKE conditions")
        void shouldHandleDynamicLikeConditions() {
            builder.addDynamicLikeCondition("name", "John")
                    .addDynamicLikeCondition("email", null)
                    .addDynamicLikeCondition("phone", "");

            String expected = "SELECT * FROM users WHERE name LIKE ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"%John%"}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should handle all null strategies correctly")
        void shouldHandleAllNullStrategies() {
            // Test NULL_AS_IS_NULL
            builder.addDynamicCondition("field1", null, NullHandleStrategy.NULL_AS_IS_NULL);
            assertEquals("SELECT * FROM users WHERE field1 IS NULL", builder.getSql());
            builder.reset();

            // Test NULL_AS_IS_NOT_NULL
            builder.addDynamicCondition("field1", null, NullHandleStrategy.NULL_AS_IS_NOT_NULL);
            assertEquals("SELECT * FROM users WHERE field1 IS NOT NULL", builder.getSql());
            builder.reset();

            // Test EMPTY_AS_IS_NULL with empty string
            builder.addDynamicCondition("field1", "", NullHandleStrategy.EMPTY_AS_IS_NULL);
            assertEquals("SELECT * FROM users WHERE field1 IS NULL", builder.getSql());
            builder.reset();

            // Test NULL_AND_EMPTY_AS_IS_NULL with both null and empty
            builder.addDynamicCondition("field1", null, NullHandleStrategy.NULL_AND_EMPTY_AS_IS_NULL)
                    .addDynamicCondition("field2", "", NullHandleStrategy.NULL_AND_EMPTY_AS_IS_NULL);
            assertEquals("SELECT * FROM users WHERE field1 IS NULL AND field2 IS NULL", builder.getSql());
            builder.reset();

            // Test IGNORE strategy
            builder.addDynamicCondition("field1", null, NullHandleStrategy.IGNORE)
                    .addDynamicCondition("field2", "value", NullHandleStrategy.IGNORE);
            assertEquals("SELECT * FROM users WHERE field2 = ?", builder.getSql());
            assertArrayEquals(new Object[]{"value"}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should handle dynamic conditions with different types")
        void shouldHandleDynamicConditionsWithDifferentTypes() {
            builder.addDynamicCondition("intField", 123, NullHandleStrategy.IGNORE)
                    .addDynamicCondition("boolField", true, NullHandleStrategy.IGNORE)
                    .addDynamicCondition("doubleField", 45.67, NullHandleStrategy.IGNORE);

            String expected = "SELECT * FROM users WHERE intField = ? AND boolField = ? AND doubleField = ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{123, true, 45.67}, builder.getPositionalParams());
        }

    }

    @Nested
    @DisplayName("Special Conditions Tests")
    class SpecialConditionsTests {

        @Test
        @DisplayName("Should create IN condition")
        void shouldCreateInCondition() {
            List<Integer> ages = Arrays.asList(18, 19, 20);
            builder.addInCondition("age", ages);

            String expected = "SELECT * FROM users WHERE age IN (?, ?, ?)";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{18, 19, 20}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create BETWEEN condition")
        void shouldCreateBetweenCondition() {
            builder.addBetweenCondition("age", 18, 25);

            String expected = "SELECT * FROM users WHERE age BETWEEN ? AND ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{18, 25}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create partial BETWEEN condition")
        void shouldCreatePartialBetweenCondition() {
            builder.addBetweenCondition("age", 18, null)
                    .addBetweenCondition("salary", null, 5000);

            String expected = "SELECT * FROM users WHERE age >= ? AND salary <= ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{18, 5000}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create LIKE conditions with different patterns")
        void shouldCreateDifferentLikePatterns() {
            builder.addLikeCondition("name", "John")
                    .addLeftLikeCondition("email", "test")
                    .addRightLikeCondition("phone", "123");

            String expected = "SELECT * FROM users WHERE name LIKE ? AND email LIKE ? AND phone LIKE ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"%John%", "test%", "%123"}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create LEFT LIKE conditions")
        void shouldCreateLeftLikeConditions() {
            builder.addLeftLikeCondition("name", "John");

            String expected = "SELECT * FROM users WHERE name LIKE ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"John%"}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create LEFT LIKE conditions with named parameters")
        void shouldCreateLeftLikeConditionsWithNamedParameters() {
            DynamicSqlBuilder namedBuilder = new DynamicSqlBuilder(BASE_SQL, ParameterMode.NAMED);
            namedBuilder.addLeftLikeCondition("name", "userName", "John");

            String expected = "SELECT * FROM users WHERE name LIKE :userName";
            assertEquals(expected, namedBuilder.getSql());
            assertEquals("John%", namedBuilder.getNamedParams().getValue("userName"));
        }

        @Test
        @DisplayName("Should handle multiple LIKE patterns in combination")
        void shouldHandleMultipleLikePatternsInCombination() {
            builder.addLikeCondition("email", "test")           // 包含匹配
                    .addLeftLikeCondition("name", "John")        // 左匹配
                    .addRightLikeCondition("phone", "123");      // 右匹配

            String expected = "SELECT * FROM users WHERE email LIKE ? AND name LIKE ? AND phone LIKE ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(
                    new Object[]{"%test%", "John%", "%123"},
                    builder.getPositionalParams()
            );
        }
    }

    @Nested
    @DisplayName("Reset and Mode Switch Tests")
    class ResetAndModeSwitchTests {

        @Test
        @DisplayName("Should reset conditions")
        void shouldResetConditions() {
            builder.addCondition("name", "John")
                    .addCondition("age", 25)
                    .reset();

            assertEquals(BASE_SQL, builder.getSql());
            assertEquals(0, builder.getPositionalParams().length);
        }

        @Test
        @DisplayName("Should switch to named mode")
        void shouldSwitchToNamedMode() {
            builder.switchToNamedMode()
                    .addCondition("name", "userName", "John");

            assertEquals("SELECT * FROM users WHERE name = :userName", builder.getSql());
            assertEquals("John", builder.getNamedParams().getValue("userName"));
        }
    }

    @Nested
    @DisplayName("Group By and Having Tests")
    class GroupByAndHavingTests {

        @Test
        @DisplayName("Should create GROUP BY with HAVING")
        void shouldCreateGroupByWithHaving() {
            builder.addCondition("status", "active")
                    .addGroupBy("department")
                    .addHaving("COUNT(*) > 5")
                    .addOrderBy("department");

            String expected = "SELECT * FROM users WHERE status = ? GROUP BY department HAVING COUNT(*) > 5 ORDER BY department";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"active"}, builder.getPositionalParams());
        }
    }

    @Nested
    @DisplayName("Complex Query Building Tests")
    class ComplexQueryBuildingTests {

        @Test
        @DisplayName("Should create complex query with multiple features")
        void shouldCreateComplexQuery() {
            builder.addCondition("status", "active")
                    .or(or -> or.addCondition("department", "IT")
                            .addCondition("role", "admin"))
                    .addInCondition("location", Arrays.asList("NY", "LA"))
                    .addBetweenCondition("salary", 50000, 100000)
                    .addGroupBy("department")
                    .addHaving("AVG(salary) > 60000")
                    .addOrderBy("department ASC")
                    .addPagination(1, 10);

            String expected = "SELECT * FROM users WHERE status = ? AND (department = ? OR role = ?) AND " +
                    "location IN (?, ?) AND salary BETWEEN ? AND ? GROUP BY department " +
                    "HAVING AVG(salary) > 60000 ORDER BY department ASC LIMIT ?, ?";
            assertEquals(expected, builder.getSql());

            Object[] expectedParams = new Object[]{
                    "active", "IT", "admin", "NY", "LA", 50000, 100000, 0, 10
            };
            assertArrayEquals(expectedParams, builder.getPositionalParams());
        }
    }


    @Nested
    @DisplayName("Like Conditions Tests")
    class LikeConditionsTests {

        @Test
        @DisplayName("Should create RIGHT LIKE condition with positional parameters")
        void shouldCreateRightLikeCondition() {
            builder.addRightLikeCondition("email", "example.com");

            String expected = "SELECT * FROM users WHERE email LIKE ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"%example.com"}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should create RIGHT LIKE condition with named parameters")
        void shouldCreateRightLikeConditionWithNamedParams() {
            DynamicSqlBuilder namedBuilder = new DynamicSqlBuilder(BASE_SQL, ParameterMode.NAMED);
            namedBuilder.addRightLikeCondition("email", "emailParam", "example.com");

            String expected = "SELECT * FROM users WHERE email LIKE :emailParam";
            assertEquals(expected, namedBuilder.getSql());
            assertEquals("%example.com", namedBuilder.getNamedParams().getValue("emailParam"));
        }

        @Test
        @DisplayName("Should handle null and empty values in RIGHT LIKE condition")
        void shouldHandleNullAndEmptyInRightLike() {
            builder.addRightLikeCondition("email", (String)null)
                    .addRightLikeCondition("name", "");

            assertEquals(BASE_SQL, builder.getSql());
            assertEquals(0, builder.getPositionalParams().length);
        }

        @Test
        @DisplayName("Should create dynamic LIKE condition with different null strategies")
        void shouldCreateDynamicLikeConditionWithNullStrategies() {
            // Test NULL_AS_IS_NULL strategy
            builder.addDynamicLikeCondition("email", null, NullHandleStrategy.NULL_AS_IS_NULL);
            assertEquals("SELECT * FROM users WHERE email IS NULL", builder.getSql());
            builder.reset();

            // Test NULL_AS_IS_NOT_NULL strategy
            builder.addDynamicLikeCondition("email", null, NullHandleStrategy.NULL_AS_IS_NOT_NULL);
            assertEquals("SELECT * FROM users WHERE email IS NOT NULL", builder.getSql());
            builder.reset();

            // Test EMPTY_AS_IS_NULL strategy
            builder.addDynamicLikeCondition("email", "", NullHandleStrategy.EMPTY_AS_IS_NULL);
            assertEquals("SELECT * FROM users WHERE email IS NULL", builder.getSql());
            builder.reset();

            // Test normal case
            builder.addDynamicLikeCondition("email", "test", NullHandleStrategy.IGNORE);
            assertEquals("SELECT * FROM users WHERE email LIKE ?", builder.getSql());
            assertArrayEquals(new Object[]{"%test%"}, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should combine different LIKE conditions")
        void shouldCombineDifferentLikeConditions() {
            builder.addLikeCondition("description", "test")          // 包含匹配 %test%
                    .addLeftLikeCondition("name", "John")             // 左匹配 John%
                    .addRightLikeCondition("email", "example.com")    // 右匹配 %example.com
                    .addDynamicLikeCondition("title", "manager");     // 动态LIKE %manager%

            String expected = "SELECT * FROM users WHERE description LIKE ? AND name LIKE ? " +
                    "AND email LIKE ? AND title LIKE ?";
            assertEquals(expected, builder.getSql());

            Object[] expectedParams = new Object[]{
                    "%test%",
                    "John%",
                    "%example.com",
                    "%manager%"
            };
            assertArrayEquals(expectedParams, builder.getPositionalParams());
        }

        @Test
        @DisplayName("Should handle dynamic LIKE conditions in OR group")
        void shouldHandleDynamicLikeConditionsInOrGroup() {
            builder.or(or -> or.addDynamicLikeCondition("name", "John")
                    .addDynamicLikeCondition("email", "example"));

            String expected = "SELECT * FROM users WHERE (name LIKE ? OR email LIKE ?)";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(
                    new Object[]{"%John%", "%example%"},
                    builder.getPositionalParams()
            );
        }

        @Test
        @DisplayName("Should ignore invalid values in dynamic LIKE conditions")
        void shouldIgnoreInvalidValuesInDynamicLike() {
            builder.addDynamicLikeCondition("name", "John")
                    .addDynamicLikeCondition("email", null)
                    .addDynamicLikeCondition("phone", "");

            String expected = "SELECT * FROM users WHERE name LIKE ?";
            assertEquals(expected, builder.getSql());
            assertArrayEquals(new Object[]{"%John%"}, builder.getPositionalParams());
        }
    }
}
