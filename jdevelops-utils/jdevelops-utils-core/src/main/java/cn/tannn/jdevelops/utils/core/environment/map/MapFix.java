package cn.tannn.jdevelops.utils.core.environment.map;




import cn.tannn.jdevelops.utils.core.environment.entity.MapPoint;

import java.math.BigDecimal;

/**
 * 经纬度纠偏工具类
 *
 * @author zhaowei
 * @version V1.1.0
 * @date 2020/6/30 0:07
 */
public class MapFix {
	private double casmF;

	private double casmRr;

	private double casmT1;

	private double casmT2;

	private double casmX1;

	private double casmX2;

	private double casmY1;

	private double casmY2;

	private MapFix() {
		casmRr = 0.0;
		casmT1 = 0.0;
		casmT2 = 0.0;
		casmX1 = 0.0;
		casmY1 = 0.0;
		casmX2 = 0.0;
		casmY2 = 0.0;
		casmF = 0.0;
	}

	private static volatile MapFix instance;

	public static MapFix getInstance() {
		if (instance == null) {
			synchronized (MapFix.class) {
				if (instance == null) {
					instance = new MapFix();
				}
			}
		}
		return instance;
	}

	/**
	 * 纠偏
	 *
	 * @param x 经度
	 * @param y 纬度
	 * @return [0]纠偏后经度   [1]纠偏后纬度
	 */
	public double[] fix(double x, double y) {
		double[] res = new double[2];
		try {
			double num = x * 3686400.0;
			double num2 = y * 3686400.0;
			MapPoint point = wgtochinaLb((int) num, (int) num2);
			assert point != null;
			double num6 = point.getX();
			double num7 = point.getY();
			num6 /= 3686400.0;
			num7 /= 3686400.0;
			res[0] = num6;
			res[1] = num7;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	private void iniCasm(double wTime, double wLng, double wLat) {
		casmT1 = wTime;
		casmT2 = wTime;
		double num = (int) (wTime / 0.357);
		casmRr = wTime - (num * 0.357);
		if (doubleEq(0.0, wTime)) {
			casmRr = 0.3;
		}
		casmX1 = wLng;
		casmY1 = wLat;
		casmX2 = wLng;
		casmY2 = wLat;
		casmF = 3.0;
	}

	private double randomYj() {
		double num = 314159269.0;
		double num2 = 453806245.0;
		casmRr = (num * casmRr) + num2;
		double num3 = (int) (casmRr / 2.0);
		casmRr -= num3 * 2.0;
		casmRr /= 2.0;
		return casmRr;
	}

	private double transformJy5(double x, double xx) {
		double num = 6378245.0;
		double num2 = 0.00669342;
		double num3 = Math.sqrt(1.0 - ((num2 * yjSin2(x * 0.0174532925199433)) * yjSin2(x * 0.0174532925199433)));
		return ((xx * 180.0) / (((num / num3) * Math.cos(x * 0.0174532925199433)) * 3.1415926));
	}

	private double transformJyj5(double x, double yy) {
		double num = 6378245.0;
		double num2 = 0.00669342;
		double d = 1.0 - ((num2 * yjSin2(x * 0.0174532925199433)) * yjSin2(x * 0.0174532925199433));
		double num4 = (num * (1.0 - num2)) / (d * Math.sqrt(d));
		return ((yy * 180.0) / (num4 * 3.1415926));
	}

	private double transformYj5(double x, double y) {
		double num = ((((300.0 + (x)) + (2.0 * y)) + ((0.1 * x) * x)) + ((0.1 * x) * y))
				+ (0.1 * Math.sqrt(Math.sqrt(x * x)));
		num += ((20.0 * yjSin2(18.849555921538762 * x)) + (20.0 * yjSin2(6.283185307179588 * x))) * 0.6667;
		num += ((20.0 * yjSin2(3.141592653589794 * x)) + (40.0 * yjSin2(1.0471975511965981 * x))) * 0.6667;
		return (num + (((150.0 * yjSin2(0.26179938779914952 * x)) + (300.0 * yjSin2(0.10471975511965979 * x))) * 0.6667));
	}

	private double transformYjy5(double x, double y) {
		double num = ((((-100.0 + (2.0 * x)) + (3.0 * y)) + ((0.2 * y) * y)) + ((0.1 * x) * y))
				+ (0.2 * Math.sqrt(Math.sqrt(x * x)));
		num += ((20.0 * yjSin2(18.849555921538762 * x)) + (20.0 * yjSin2(6.283185307179588 * x))) * 0.6667;
		num += ((20.0 * yjSin2(3.141592653589794 * y)) + (40.0 * yjSin2(1.0471975511965981 * y))) * 0.6667;
		return (num + (((160.0 * yjSin2(0.26179938779914952 * y)) + (320.0 * yjSin2(0.10471975511965979 * y))) * 0.6667));
	}

	private MapPoint wgtochinaLb(int wgLng, int wgLat) {
		MapPoint point = new MapPoint();
		if (0 <= 0x1388) {
			double num = wgLng;
			num /= 3686400.0;
			double x = wgLat;
			x /= 3686400.0;
			if (num < 72.004) {
				return null;
			}
			if (num > 137.8347) {
				return null;
			}
			if (x < 0.8293) {
				return null;
			}
			if (x > 55.8271) {
				return null;
			}
			if (1 == 0) {
				iniCasm(0, wgLng, wgLat);
				point = new MapPoint();
				point.setLatitude(wgLng);
				point.setLongitude(wgLat);
				return point;
			}
			casmT2 = 0;
			double num3 = (casmT2 - casmT1) / 1000.0;
			if (num3 <= 0.0) {
				casmT1 = casmT2;
				casmF++;
				casmX1 = casmX2;
				casmF++;
				casmY1 = casmY2;
				casmF++;
			} else if (num3 > 120.0) {
				// 浮点数之间的等值判断,基本数据类型不能用==比较,包装数据类型不能用equals来判断。
				if (doubleEq(3.0, casmF)) {
					casmF = 0.0;
					casmX2 = wgLng;
					casmY2 = wgLat;
					double num4 = casmX2 - casmX1;
					double num5 = casmY2 - casmY1;
					double num6 = Math.sqrt((num4 * num4) + (num5 * num5)) / num3;
					if (num6 > 3185.0) {
						return point;
					}
				}
				casmT1 = casmT2;
				casmF++;
				casmX1 = casmX2;
				casmF++;
				casmY1 = casmY2;
				casmF++;
			}
			double xx = transformYj5(num - 105.0, x - 35.0);
			double yy = transformYjy5(num - 105.0, x - 35.0);
			xx = ((xx + (0 * 0.001)) + yjSin2(0 * 0.0174532925199433)) + randomYj();
			yy = ((yy + (0 * 0.001)) + yjSin2(0 * 0.0174532925199433)) + randomYj();
			point = new MapPoint();
			point.setX((num + transformJy5(x, xx)) * 3686400.0);
			point.setY((x + transformJyj5(x, yy)) * 3686400.0);
		}
		return point;
	}

	private double yjSin2(double x) {
		double num = 0.0;
		if (x < 0.0) {
			x = -x;
			num = 1.0;
		}
		int num2 = (int) (x / 6.28318530717959);
		double num3 = x - (num2 * 6.28318530717959);
		if (num3 > 3.1415926535897931) {
			num3 -= 3.1415926535897931;
			if (doubleEq(1.0, num)) {
				num = 0.0;
			} else {
				num = 1.0;
			}
		}
		x = num3;
		double num4 = x;
		double num5 = x;
		num3 *= num3;
		num5 *= num3;
		num4 -= num5 * 0.166666666666667;
		num5 *= num3;
		num4 += num5 * 0.00833333333333333;
		num5 *= num3;
		num4 -= num5 * 0.000198412698412698;
		num5 *= num3;
		num4 += num5 * 2.75573192239859E-06;
		num5 *= num3;
		num4 -= num5 * 2.50521083854417E-08;
		if (doubleEq(1.0, num)) {
			num4 = -num4;
		}
		return num4;
	}

	/**
	 * 判断相等
	 *
	 * @param a a
	 * @param b b
	 * @return boolean
	 */
	private boolean doubleEq(double a, double b) {
		// 浮点数之间的等值判断,基本数据类型不能用==比较,包装数据类型不能用equals来判断。
		return BigDecimal.valueOf(a).compareTo(BigDecimal.valueOf(b)) == 0;
	}
}
