package cn.tannn.jdevelops.utils.http.pojo;

import java.util.Objects;

/**
 * ping
 * @author tan
 * @date 2023-05-09 13:54:11
 */
public class PingVO {

    /**
     * 运行状态;0、已停止，1、运行中
     */
    private Integer runningState;

    /**
     * 有效状态; 1、有校，2、无效
     */
    private Integer vaildState;


    /**
     *
     * @param runningState 运行状态 0、已停止，1、运行中
     * @param vaildState 有效状态 1、有校，2、无效
     */
    public PingVO(Integer runningState, Integer vaildState) {
        this.runningState = runningState;
        this.vaildState = vaildState;
    }



    public Integer getRunningState() {
        return runningState;
    }

    public void setRunningState(Integer runningState) {
        this.runningState = runningState;
    }

    public Integer getVaildState() {
        return vaildState;
    }

    public void setVaildState(Integer vaildState) {
        this.vaildState = vaildState;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PingVO pingVO = (PingVO) o;
        return Objects.equals(runningState, pingVO.runningState) && Objects.equals(vaildState, pingVO.vaildState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runningState, vaildState);
    }

    @Override
    public String toString() {
        return "PingVO{" +
                "runningState=" + runningState +
                ", vaildState=" + vaildState +
                '}';
    }

}
