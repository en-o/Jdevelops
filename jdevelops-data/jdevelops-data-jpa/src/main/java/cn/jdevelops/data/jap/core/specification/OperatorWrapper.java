package cn.jdevelops.data.jap.core.specification;


import javax.persistence.criteria.Expression;


/**
 * 查询查找包装
 * @author tan
 * @date 2023-03-24 10:59:17
 */
public class OperatorWrapper {

    /**
     * specWrapper
     */
    private SpecificationWrapper<?> specWrapper;

    /**
     * 添加 key
     */
    private Expression selectKey;

    /**
     * 添加值
     */
    private Object selectValue;



    /**
     * 比较
     * @return selectValue
     * @param <Y>  Y
     */
    public <Y extends Comparable<? super Y>> Y getCompareValue(){
        return (Y)selectValue;
    }


    public SpecificationWrapper<?> getSpecWrapper() {
        return specWrapper;
    }

    public void setSpecWrapper(SpecificationWrapper<?> specWrapper) {
        this.specWrapper = specWrapper;
    }


    public Expression getSelectKey() {
        return selectKey;
    }

    public void setSelectKey(Expression selectKey) {
        this.selectKey = selectKey;
    }

    public Object getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(Object selectValue) {
        this.selectValue = selectValue;
    }


    public OperatorWrapper() {
    }

    public OperatorWrapper(SpecificationWrapper<?> specWrapper, Expression selectKey, Object selectValue) {
        this.specWrapper = specWrapper;
        this.selectKey = selectKey;
        this.selectValue = selectValue;
    }

    @Override
    public String toString() {
        return "OperatorWrapper{" +
                "specWrapper=" + specWrapper +
                ", selectKey=" + selectKey +
                ", selectValue=" + selectValue +
                '}';
    }
}
