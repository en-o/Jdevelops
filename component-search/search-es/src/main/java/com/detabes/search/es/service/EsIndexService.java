package com.detabes.search.es.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 索引操做工具接口
 *
 * @author lxw
 * @version V1.0
 * @date 2021/5/12
 **/
public interface EsIndexService {

	/**
	 * 创建索引
	 * PS：相当于创建了一个表，一个类型的仓库
	 *
	 * @param index 索引名称
	 * @return boolean true is success！
	 * @throws IOException IOException
	 */
	boolean createIndex(String index) throws IOException;

	/**
	 * 判断索引是否存在
	 *
	 * @param index 索引名称
	 * @return boolean true is exist！
	 * @throws IOException IOException
	 */
	boolean isIndexExist(String index) throws IOException;

	/**
	 * 删除索引
	 *
	 * @param index 索引名称
	 * @return boolean true is success！
	 * @throws IOException IOException
	 */
	boolean deleteIndex(String index) throws IOException;

	/**
	 * 创建索引库映射
	 * <p> index不存在会先创建，存在则会先执行删除，再创建index，最后创建字段映射</p>
	 * <p>
	 * 若结尾是"time"，则类型为 keyword；<br/>
	 * 若结尾是 "dateFormat"，则类型为 date； <br/>
	 * 若等于 "status"，则类型为 integer； <br/>
	 * 若等于 "id"，则类型为 integer； <br/>
	 * 若结尾是 "stats"，则类型为 integer； <br/>
	 * 若结尾是 "type"，则类型为 integer； <br/>
	 * </p>
	 *
	 * @param index  索引名称
	 * @param fields 常规字段
	 * @return boolean true is success！
	 * @throws Exception Exception
	 * @author lxw
	 * @date 2021/6/22 14:34
	 */
	boolean createIndexMapping(String index, List<String> fields) throws Exception;

	/**
	 * 单个字段创建索引库映射
	 * PS: index 必须存在，若不存在则无法创建映射。
	 * <p>
	 * 若结尾是"time"，则类型为 keyword；<br/>
	 * 若结尾是 "dateFormat"，则类型为 date； <br/>
	 * 若等于 "status"，则类型为 integer； <br/>
	 * 若等于 "id"，则类型为 integer； <br/>
	 * 若结尾是 "stats"，则类型为 integer； <br/>
	 * 若结尾是 "type"，则类型为 integer； <br/>
	 * </p>
	 *
	 * @param index 索引名称
	 * @param field 常规字段
	 * @return boolean true is success！
	 * @throws Exception Exception
	 */
	boolean createIndexMapping(String index, String field) throws Exception;

	/**
	 * 创建嵌套映射
	 * <p>
	 * 若结尾是"time"，则类型为 keyword；<br/>
	 * 若结尾是 "dateFormat"，则类型为 date； <br/>
	 * 若等于 "status"，则类型为 integer； <br/>
	 * 若等于 "id"，则类型为 integer； <br/>
	 * 若结尾是 "stats"，则类型为 integer； <br/>
	 * 若结尾是 "type"，则类型为 integer； <br/>
	 * </p>
	 *
	 * @param index        索引名称
	 * @param nested       嵌套名称
	 * @param nestedFields 嵌套字段
	 * @return boolean true is success！
	 * @throws Exception Exception
	 * @author lxw
	 * @date 2021/6/21 15:38
	 */
	boolean createIndexMapping(String index, String nested, List<String> nestedFields) throws Exception;

	/**
	 * 新增/更新数据
	 *
	 * @param index    索引，类似数据库
	 * @param source   要新增/更新的数据,map格式的对象数据
	 * @param esOnlyId ES索引唯一值,为null时es随机生成
	 * @return boolean true is success！
	 * @throws IOException IOException
	 */
	boolean submitData(String index, Object source, String esOnlyId) throws IOException;

	/**
	 * 新增数据，自定义esOnlyId
	 *
	 * @param index    索引，类似数据库
	 * @param source   要增加的数据,map格式的对象数据
	 * @param esOnlyId ES索引唯一值,为null时es随机生成
	 * @return boolean true is success！
	 * @throws IOException IOException
	 */
	boolean addData(String index, Object source, String esOnlyId) throws IOException;

	/**
	 * 数据添加 随机esOnlyId
	 *
	 * @param index  索引，类似数据库
	 * @param source 要增加的数据,map格式的对象数据
	 * @return boolean true is success！
	 * @throws IOException IOException
	 */
	boolean addData(String index, Object source) throws IOException;

	/**
	 * 通过ID删除数据
	 *
	 * @param index    索引，类似数据库
	 * @param esOnlyId ES索引唯一值
	 * @return boolean true is success！
	 * @throws IOException IOException
	 */
	boolean deleteDataById(String index, String esOnlyId) throws IOException;

	/**
	 * 通过ID 更新数据
	 *
	 * @param index    索引，类似数据库
	 * @param source   要更新数据,map格式的对象数据
	 * @param esOnlyId ES索引唯一值
	 * @return boolean true is success！
	 * @throws IOException IOException
	 */
	boolean updateDataById(String index, Object source, String esOnlyId) throws IOException;

	/**
	 * 通过ID 更新数据,保证实时性
	 *
	 * @param index    索引，类似数据库
	 * @param source   要增加的数据,map格式的对象数据
	 * @param esOnlyId ES索引唯一值
	 * @return boolean true is success！
	 * @throws IOException IOException
	 */
	boolean updateDataByIdNoRealTime(String index, Object source, String esOnlyId) throws IOException;


	/**
	 * 通过ID判断文档是否存在
	 *
	 * @param index    索引，类似数据库
	 * @param esOnlyId ES索引唯一值
	 * @return boolean true is success！
	 * @throws IOException IOException
	 */
	boolean existsById(String index, String esOnlyId) throws IOException;

	/**
	 * 随机ID，批量插入
	 * PS: false 指得是没有失败的数据
	 *
	 * @param index   索引，类似数据库
	 * @param objects 数据集合,每一条均为map格式的对象数据
	 * @return boolean false is success！
	 * @throws IOException IOException
	 */
	boolean bulkPost(String index, List<Object> objects) throws IOException;

	/**
	 * 指定ID，批量插入
	 * PS: false 指得是没有失败的数据
	 *
	 * @param index 索引，类似数据库
	 * @param list  数据 map 中必须有一个key:esOnlyId,且在数据中唯一，用作设定索引唯一值
	 * @return boolean false is success！
	 * @throws IOException IOException
	 */
	boolean bulkPostAppointId(String index, List<Map<String, Object>> list) throws IOException;
}
