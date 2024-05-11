package cn.tannn.jdevelops.jpa.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * 公共dao层
 * <br></>
 * {NoRepositoryBean：Spring Data Jpa在启动时就不会去实例化BaseRepository这个接口}
 * <br></>
 *
 * @author tn
 * JpaSpecificationExecutor ：JPA复杂查询
 * JpaRepository 普通查询
 * @date 2020/5/14 15:31
 */
@NoRepositoryBean
public interface JpaBasicsRepository<B, ID> extends JpaRepository<B, ID>, JpaSpecificationExecutor<B> {

}
