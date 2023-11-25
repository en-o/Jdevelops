package cn.jdevelops.file.oss.api.util;


import cn.jdevelops.file.oss.api.constants.OSSConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 文件相关工具类
 *
 * @author lxw
 * @version V1.0
 * @date 2021/10/15
 **/
public class AboutFileUtil {

	private static final Logger LOG = LoggerFactory.getLogger(AboutFileUtil.class);

	public static final String[] IMG_FILE = {
			OSSConstants.FILE_SUFFIX_BMP,
			OSSConstants.FILE_SUFFIX_JPG,
			OSSConstants.FILE_SUFFIX_PNG,
			OSSConstants.FILE_SUFFIX_TIF,
			OSSConstants.FILE_SUFFIX_GIF,
			OSSConstants.FILE_SUFFIX_JPEG,
			OSSConstants.FILE_SUFFIX_WEBP};
	public static final String[] DOC_FILE = {
			OSSConstants.FILE_SUFFIX_DOC,
			OSSConstants.FILE_SUFFIX_DOCX,
			OSSConstants.FILE_SUFFIX_TXT,
			OSSConstants.FILE_SUFFIX_HLP,
			OSSConstants.FILE_SUFFIX_WPS,
			OSSConstants.FILE_SUFFIX_RTF,
			OSSConstants.FILE_SUFFIX_XLS,
			OSSConstants.FILE_SUFFIX_XLSX,
			OSSConstants.FILE_SUFFIX_PPT,
			OSSConstants.FILE_SUFFIX_PPTX,
			OSSConstants.FILE_SUFFIX_JAVA,
			OSSConstants.FILE_SUFFIX_HTML,
			OSSConstants.FILE_SUFFIX_PDF,
			OSSConstants.FILE_SUFFIX_MD,
			OSSConstants.FILE_SUFFIX_SQL,
			OSSConstants.FILE_SUFFIX_CSS,
			OSSConstants.FILE_SUFFIX_JS,
			OSSConstants.FILE_SUFFIX_VUE,
			OSSConstants.FILE_SUFFIX_JAVA};
	public static final String[] VIDEO_FILE = {
			OSSConstants.FILE_SUFFIX_AVI,
			OSSConstants.FILE_SUFFIX_MP4,
			OSSConstants.FILE_SUFFIX_MPG,
			OSSConstants.FILE_SUFFIX_MOV,
			OSSConstants.FILE_SUFFIX_SWF,
			OSSConstants.FILE_SUFFIX_3GP,
			OSSConstants.FILE_SUFFIX_RM,
			OSSConstants.FILE_SUFFIX_RMVB,
			OSSConstants.FILE_SUFFIX_WMV,
			OSSConstants.FILE_SUFFIX_MKV};
	public static final String[] MUSIC_FILE = {
			OSSConstants.FILE_SUFFIX_WAV,
			OSSConstants.FILE_SUFFIX_AIF,
			OSSConstants.FILE_SUFFIX_AU,
			OSSConstants.FILE_SUFFIX_MP3,
			OSSConstants.FILE_SUFFIX_RAM,
			OSSConstants.FILE_SUFFIX_WMA,
			OSSConstants.FILE_SUFFIX_MMF,
			OSSConstants.FILE_SUFFIX_AMR,
			OSSConstants.FILE_SUFFIX_AAC,
			OSSConstants.FILE_SUFFIX_FLAC};
	public static String[] ALL_FILE;

	static {
		ALL_FILE = new String[IMG_FILE.length+DOC_FILE.length+VIDEO_FILE.length+MUSIC_FILE.length];
		ALL_FILE = ArrayUtils.addAll(ALL_FILE, IMG_FILE);
		ALL_FILE = ArrayUtils.addAll(ALL_FILE, DOC_FILE);
		ALL_FILE = ArrayUtils.addAll(ALL_FILE, VIDEO_FILE);
		ALL_FILE = ArrayUtils.addAll(ALL_FILE, MUSIC_FILE);
	}


	/**
	 * 从文件名中得到其后缀名
	 *
	 * @param filename 文件名称
	 * @return 后缀名
	 */
	public static String getFileSuffix(String filename) {
		return filename.substring(
				filename.lastIndexOf(OSSConstants.SYMBOL_POINT) + 1);
	}

	/**
	 * 通过其后缀名判断其是否合法,合法后缀名为常见的
	 *
	 * @param suffix 后缀名
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean isSafe(String suffix) {
		if(suffix == null){ return false;}
		suffix = suffix.toLowerCase();
		for (String s : ALL_FILE) {
			if (suffix.equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过其后缀名判断其是否是图片
	 *
	 * @param suffix 后缀名
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean isPic(String suffix) {
		if(suffix == null){ return false;}
		suffix = suffix.toLowerCase();
		for (String s : IMG_FILE) {
			if (suffix.equals(s)) {
				return true;
			}
		}
		return false;
	}


	/**
	 *  获取文件大小
	 *
	 * @param file File
	 * @return String
	 */
	public static String getFileSizeUnit(File file){
		return getFileSizeUnit(file.length());
	}

	/**
	 *  文件大小添加单位
	 *
	 * @param fileSize 文件大小 [最小KB]
	 * @return String
	 */
	public static String getFileSizeUnit(long fileSize) {
		//size不能为0？
		double temp;
		String size;
		temp = (double) fileSize / OSSConstants.NUM_1024;
		if (temp >= OSSConstants.NUM_1024) {
			temp = temp / OSSConstants.NUM_1024;
			if (temp >= OSSConstants.NUM_1024) {
				temp = temp / OSSConstants.NUM_1024;
				size = temp + "000";
				size = size.substring(OSSConstants.NUM_ZERO, size.indexOf(OSSConstants.SYMBOL_POINT) + OSSConstants.NUM_THREE) + "GB";
			} else {
				size = temp + "000";
				size = size.substring(OSSConstants.NUM_ZERO, size.indexOf(OSSConstants.SYMBOL_POINT) + OSSConstants.NUM_THREE) + "MB";
			}
		} else {
			size = temp + "000";
			size = size.substring(OSSConstants.NUM_ZERO, size.indexOf(OSSConstants.SYMBOL_POINT) + OSSConstants.NUM_THREE) + "KB";
		}
		return size;
	}
}
