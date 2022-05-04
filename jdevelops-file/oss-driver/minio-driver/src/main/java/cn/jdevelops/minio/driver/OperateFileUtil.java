package cn.jdevelops.minio.driver;

import cn.jdevelops.file.bean.FilePathResult;
import cn.jdevelops.file.config.OSSConfig;
import cn.jdevelops.file.constants.OSSConstants;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 文件操作工具类
 *
 * @author lxw
 * @version V1.0
 * @date 2021/2/1
 **/
@SuppressWarnings("all")
public class OperateFileUtil {

	private final MinioClient minioClient;
	private final OSSConfig ossConfig;

	public OperateFileUtil(MinioClient minioClient, OSSConfig minIoConfig) {
		this.minioClient = minioClient;
		this.ossConfig = minIoConfig;
	}

	/**
	 * 检查存储桶是否存在
	 *
	 * @param bucket 存储桶名称
	 */
	public boolean bucketExists(String bucket) throws Exception {

		return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
	}

	/**
	 * 创建一个新的存储桶
	 *
	 * @param bucket 存储桶名称（文件存放最外层的根目录）
	 * @author lxw
	 * @date 2021/2/1 12:01
	 */
	public void makeBucket(String bucket) throws Exception {
		// 如存储桶不存在，创建之。
		boolean found = bucketExists(bucket);
		if (!found) {
			// 创建名为'bucket'的存储桶。
			minioClient.makeBucket(
					MakeBucketArgs.builder()
							.bucket(bucket)
							.build());
			String str = " {\n" +
					"     \"Statement\": [\n" +
					"         {\n" +
					"             \"Action\": [\n" +
					"                 \"s3:GetBucketLocation\",\n" +
					"                 \"s3:ListBucket\"\n" +
					"             ],\n" +
					"             \"Effect\": \"Allow\",\n" +
					"             \"Principal\": \"*\",\n" +
					"             \"Resource\": \"arn:aws:s3:::" + bucket + "\"\n" +
					"         },\n" +
					"         {\n" +
					"             \"Action\": \"s3:GetObject\",\n" +
					"             \"Effect\": \"Allow\",\n" +
					"             \"Principal\": \"*\",\n" +
					"             \"Resource\": \"arn:aws:s3:::" + bucket + "/*\"\n" +
					"         }\n" +
					"     ],\n" +
					"     \"Version\": \"2012-10-17\"\n" +
					" }";
			minioClient.setBucketPolicy(
					SetBucketPolicyArgs.builder().bucket(bucket).config(str).build());
		}
	}

	/**
	 * 列出所有存储桶
	 *
	 * @return java.util.List<io.minio.messages.Bucket>
	 * @author lxw
	 * @date 2021/2/1 12:06
	 */
	public List<Bucket> listBuckets() throws Exception {
		// 列出所有存储桶
		return minioClient.listBuckets();
	}

	/**
	 * 删除桶
	 *
	 * @param bucketName 桶名称，桶中不能有对象，否则报错
	 * @author lxw
	 * @date 2021/11/26 15:37
	 */
	public void removeBucket(String bucketName) throws Exception {
		minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
	}

	/**
	 * 文件上传，成功后返回相对地址，需要自己加上minio的url
	 *
	 * @param file        文件流
	 * @param bucket      存储桶(文件地址)
	 * @param childFolder 下级文件夹（例单级："bus"，多级："bus/bus/bus" ）
	 * @return java.lang.String
	 * @author lxw
	 * @date 2021/2/1 10:38
	 */
	public FilePathResult uploadFile(MultipartFile file, String bucket, String childFolder) throws Exception {
		// 检查存储桶是否已经存在
		makeBucket(bucket);
		// 文件名称
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String filename = sdf.format(date).trim();
		// 文件名称为空值，则抛出空指针异常
		Objects.requireNonNull(file.getOriginalFilename());
		// 文件原名称
		String originalName = file.getOriginalFilename();
		// 文件类型后缀 如 jpg png
		String fileType = AboutFileUtil.getFileSuffix(originalName);
		// 文件上传之后的新文件名称
		String objectName = filename + OSSConstants.SYMBOL_POINT + fileType;
		if (StringUtils.isNotBlank(childFolder) && !StringUtils.equalsIgnoreCase("null", childFolder)) {
			objectName = childFolder + objectName;
		}
		InputStream in = file.getInputStream();
		//默认类型，该“application/octet-stream”类型的时候，浏览器访问地址为下载
		String contentType = file.getContentType();
		minioClient.putObject(
				PutObjectArgs.builder().bucket(bucket).object(objectName).stream(
								in, in.available(), -1)
						.contentType(contentType)
						.build());
		in.close();
		String relativePath = bucket + OSSConstants.PATH_SEPARATOR + objectName;
		return FilePathResult.builder()
				.absolutePath(ossConfig.getBrowseUrl() + OSSConstants.PATH_SEPARATOR + relativePath)
				.relativePath(relativePath)
				.freshName(objectName)
				.originalName(originalName)
				.build();
	}


	/**
	 * 创建文本文件
	 *
	 * @param objJson    字符串对象
	 * @param bucket     存储桶(文件地址)
	 * @param objectName 对象名称（uid/v001/xxxx.json)
	 * @return java.lang.String
	 * @author lxw
	 * @date 2021/2/1 10:38
	 */
	public FilePathResult uploadFile(String objJson, String bucket, String objectName) throws Exception {
		// 检查存储桶是否已经存在
		makeBucket(bucket);
		ByteArrayInputStream basis = new
				ByteArrayInputStream(objJson.getBytes(StandardCharsets.UTF_8));

		//默认类型，该“application/octet-stream”类型的时候，浏览器访问地址为下载
		minioClient.putObject(
				PutObjectArgs.builder().bucket(bucket).object(objectName).stream(
								basis, basis.available(), -1)
						.contentType("application/octet-stream")
						.build());
		basis.close();
		String relativePath = bucket + OSSConstants.PATH_SEPARATOR + objectName;
		return FilePathResult.builder()
				.absolutePath(ossConfig.getBrowseUrl() + OSSConstants.PATH_SEPARATOR + relativePath)
				.relativePath(relativePath)
				.freshName(objectName)
				.originalName(objectName)
				.build();
	}

	/**
	 * 获取指定位置下的文件（也可能是文件夹）
	 *
	 * @param bucket      桶
	 * @param childFolder 桶下的指定路径
	 * @return java.lang.Iterable<io.minio.Result < io.minio.messages.Item>>
	 * @author lxw
	 * @date 2021/11/10 10:07
	 */
	public Iterable<Result<Item>> listObjects(String bucket, String childFolder) throws Exception {
		// Lists objects information.
		return minioClient.listObjects(
				ListObjectsArgs.builder().bucket(bucket).prefix(childFolder).build());

	}

	/**
	 * 获取指定位置下的所有文件（递归循环出所有文件，不包含文件夹）
	 *
	 * @param bucket      桶
	 * @param childFolder 桶下的指定路径
	 * @return java.lang.Iterable<io.minio.Result < io.minio.messages.Item>>
	 * @author lxw
	 * @date 2021/11/10 10:08
	 */
	public Iterable<Result<Item>> allListObjects(String bucket, String childFolder) throws Exception {
		return minioClient.listObjects(
				ListObjectsArgs.builder().bucket(bucket).prefix(childFolder).recursive(true).build());
	}

	/**
	 * 获取对象信息和对象的元数据
	 *
	 * @param bucket     桶
	 * @param objectName 桶中的对象名称
	 * @return io.minio.StatObjectResponse
	 * @author lxw
	 * @date 2021/11/10 14:20
	 */
	public StatObjectResponse statObject(String bucket, String objectName) throws Exception {
		boolean found = bucketExists(bucket);
		if (!found) {
			throw new RuntimeException("桶不存在！");
		}
		return minioClient.statObject(
				StatObjectArgs.builder()
						.bucket(bucket)
						.object(objectName)
						.build());
	}

	/**
	 * 获取对象的数据。 返回的InputStream必须在使用后关闭，以释放网络资源。
	 *
	 * @param bucket     桶
	 * @param objectName 桶中的对象名称
	 * @return java.io.InputStream
	 * @author lxw
	 * @date 2021/11/10 14:22
	 */
	public InputStream getObject(String bucket, String objectName) throws Exception {
		boolean found = bucketExists(bucket);
		if (!found) {
			throw new RuntimeException("桶不存在！");
		}
		return minioClient.getObject(
				GetObjectArgs.builder()
						.bucket(bucket)
						.object(objectName)
						.build());

	}

	/**
	 * 复制文件
	 *
	 * @param bucket           目标桶
	 * @param objectName       目标对象
	 * @param sourceBucket     源桶
	 * @param sourceObjectName 源对象
	 * @author lxw
	 * @date 2022/2/23 14:42
	 */
	public void copyObject(String bucket, String objectName, String sourceBucket, String sourceObjectName) throws Exception {
		minioClient.copyObject(
				CopyObjectArgs.builder()
						.bucket(bucket)
						.object(objectName)
						.source(
								CopySource.builder()
										.bucket(sourceBucket)
										.object(sourceObjectName)
										.build())
						.build());
	}

	/**
	 * 下载文件
	 *
	 * @param response   response
	 * @param bucket     桶
	 * @param objectName 桶中的对象名称
	 * @author lxw
	 * @date 2021/11/10 14:55
	 */
	public void download(HttpServletResponse response, String bucket, String objectName) throws Exception {
		boolean found = bucketExists(bucket);
		if (!found) {
			throw new RuntimeException("桶不存在！");
		}
		// 处理桶内的对象名称
		objectName = verifyObjectName(bucket, objectName);
		//获取对象信息和对象的元数据。
		StatObjectResponse statObjectResponse = statObject(bucket, objectName);
		//setContentType 设置发送到客户机的响应的内容类型
		response.setContentType(statObjectResponse.contentType());
		//设置响应头
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(statObjectResponse.object(), "UTF-8"));
		//文件流
		InputStream inputStream = getObject(bucket, objectName);
		//设置文件大小
		response.setHeader("Content-Length", String.valueOf(statObjectResponse.size()));
		IOUtils.copy(inputStream, response.getOutputStream());
		//关闭流
		inputStream.close();
	}

	/**
	 * 生成一个有效时间内的访问连接
	 *
	 * @param bucket     桶
	 * @param objectName 名称
	 * @param expires    过期时间 失效时间（以秒为单位），默认是7天，不得大于七天
	 * @return java.lang.String
	 * @author lxw
	 * @date 2021/11/10 15:21
	 */
	public String getExpiryObjectUrl(String bucket, String objectName, Integer expires) throws Exception {
		boolean found = bucketExists(bucket);
		if (!found) {
			throw new RuntimeException("桶不存在！");
		}
		objectName = verifyObjectName(bucket, objectName);
		return minioClient.getPresignedObjectUrl(
				GetPresignedObjectUrlArgs.builder()
						.method(Method.GET)
						.bucket(bucket)
						.object(objectName)
						.expiry(expires, TimeUnit.SECONDS)
						.build());
	}

	/**
	 * 删除一个对象
	 *
	 * @param bucket     存储桶名称
	 * @param objectName 存储桶里的对象名称
	 */
	public void removeObject(String bucket, String objectName) throws Exception {
		boolean flag = bucketExists(bucket);
		if (flag) {
			objectName = verifyObjectName(bucket, objectName);
			minioClient.removeObject(
					RemoveObjectArgs.builder()
							.bucket(bucket)
							.object(objectName)
							.build());
		}
	}

	/**
	 * 删除指定桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
	 *
	 * @param bucket      存储桶名称
	 * @param objectNames 含有要删除的多个object名称的迭代器对象
	 */
	public List<String> removeObjects(String bucket, List<String> objectNames) throws Exception {
		boolean flag = bucketExists(bucket);
		List<String> deleteErrorNames = new ArrayList<>();
		if (flag) {
			List<DeleteObject> objects = new LinkedList<>();
			for (String objectName : objectNames) {
				objectName = verifyObjectName(bucket, objectName);
				objects.add(new DeleteObject(objectName));
			}
			Iterable<Result<DeleteError>> results = minioClient.removeObjects(
					RemoveObjectsArgs.builder()
							.bucket(bucket)
							.objects(objects)
							.build());
			for (Result<DeleteError> result : results) {
				DeleteError error = result.get();
				deleteErrorNames.add(error.objectName());
			}
		}
		return deleteErrorNames;
	}

	/**
	 * 处理对象名称
	 * <p>
	 * 如果对象名称包含服务路径、桶名，则将服务路径、桶名删除<br/>
	 * 如：http://127.0.0.1:9000/bucket/4/20211110144515362.png  --> 4/20211110144515362.png
	 * </p>
	 *
	 * @param bucket     桶
	 * @param objectName 对象名称
	 * @author lxw
	 * @date 2021/11/10 15:26
	 */
	private String verifyObjectName(String bucket, String objectName) {
		String replace = ossConfig.getBrowseUrl() + OSSConstants.PATH_SEPARATOR + bucket + OSSConstants.PATH_SEPARATOR;
		return objectName.replaceFirst(replace, "");
	}
}
