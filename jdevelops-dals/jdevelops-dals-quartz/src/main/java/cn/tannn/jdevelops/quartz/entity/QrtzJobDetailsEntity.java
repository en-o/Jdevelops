package cn.tannn.jdevelops.quartz.entity;



import cn.tannn.jdevelops.quartz.entity.key.QrtzJobDetailsUPK;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 任务详情
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_job_details")
@AutoConfigurationPackage
public class QrtzJobDetailsEntity   implements Serializable,Cloneable{


    @EmbeddedId
    private QrtzJobDetailsUPK jobDetailsUPK;

    /** 描述  */
    private  String  description ;


    /** 任务类名 */
    private  String  jobClassName ;


    /** 是否持久 */
    private  String  isDurable ;


    /** 是否集群 */
    private  String  isNonconcurrent ;


    /** 跟新数据 */
    private  String  isUpdateData ;


    /** 需要恢复 */
    private  String  requestsRecovery ;


    /** 任务数据 */
    @Column(columnDefinition="Blob")
    private  byte[]  jobData ;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzJobDetailsEntity that = (QrtzJobDetailsEntity) o;
        return Objects.equals(jobDetailsUPK, that.jobDetailsUPK) && Objects.equals(description, that.description) && Objects.equals(jobClassName, that.jobClassName) && Objects.equals(isDurable, that.isDurable) && Objects.equals(isNonconcurrent, that.isNonconcurrent) && Objects.equals(isUpdateData, that.isUpdateData) && Objects.equals(requestsRecovery, that.requestsRecovery) && Arrays.equals(jobData, that.jobData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(jobDetailsUPK, description, jobClassName, isDurable, isNonconcurrent, isUpdateData, requestsRecovery);
        result = 31 * result + Arrays.hashCode(jobData);
        return result;
    }

    @Override
    public String toString() {
        return "QrtzJobDetailsEntity{" +
                "jobDetailsUPK=" + jobDetailsUPK +
                ", description='" + description + '\'' +
                ", jobClassName='" + jobClassName + '\'' +
                ", isDurable='" + isDurable + '\'' +
                ", isNonconcurrent='" + isNonconcurrent + '\'' +
                ", isUpdateData='" + isUpdateData + '\'' +
                ", requestsRecovery='" + requestsRecovery + '\'' +
                ", jobData=" + Arrays.toString(jobData) +
                '}';
    }

    public QrtzJobDetailsUPK getJobDetailsUPK() {
        return jobDetailsUPK;
    }

    public void setJobDetailsUPK(QrtzJobDetailsUPK jobDetailsUPK) {
        this.jobDetailsUPK = jobDetailsUPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getIsDurable() {
        return isDurable;
    }

    public void setIsDurable(String isDurable) {
        this.isDurable = isDurable;
    }

    public String getIsNonconcurrent() {
        return isNonconcurrent;
    }

    public void setIsNonconcurrent(String isNonconcurrent) {
        this.isNonconcurrent = isNonconcurrent;
    }

    public String getIsUpdateData() {
        return isUpdateData;
    }

    public void setIsUpdateData(String isUpdateData) {
        this.isUpdateData = isUpdateData;
    }

    public String getRequestsRecovery() {
        return requestsRecovery;
    }

    public void setRequestsRecovery(String requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }

    public byte[] getJobData() {
        return jobData;
    }

    public void setJobData(byte[] jobData) {
        this.jobData = jobData;
    }
}
