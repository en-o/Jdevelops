package com.deabes.environment.core.map;


import com.deabes.environment.entity.MapPoint;

/**
 * 经纬度纠偏工具类
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @ClassName: MapFix
 * @author  zhaowei
 * @date  2020/6/30 0:07
 * @version  V1.1.0
 */
public class MapFix {
    private double casm_f;

    private double casm_rr;

    private double casm_t1;

    private double casm_t2;

    private double casm_x1;

    private double casm_x2;

    private double casm_y1;

    private double casm_y2;

    private MapFix() {
        casm_rr = 0.0;
        casm_t1 = 0.0;
        casm_t2 = 0.0;
        casm_x1 = 0.0;
        casm_y1 = 0.0;
        casm_x2 = 0.0;
        casm_y2 = 0.0;
        casm_f = 0.0;
    }

    private static volatile MapFix instance;

    public static MapFix getInstance() {
        if (instance == null) {
            synchronized (MapFix.class){
                if(instance==null){
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
            double num3 = 0.0;
            double num4 = 0.0;
            double num5 = 0.0;
            MapPoint point = wgtochina_lb(1, (int) num, (int) num2, (int) num5, (int) num3, (int) num4);
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

    private void iniCasm(double w_time, double w_lng, double w_lat) {
        casm_t1 = w_time;
        casm_t2 = w_time;
        double num = (int) (w_time / 0.357);
        casm_rr = w_time - (num * 0.357);
        if (w_time == 0.0) {
            casm_rr = 0.3;
        }
        casm_x1 = w_lng;
        casm_y1 = w_lat;
        casm_x2 = w_lng;
        casm_y2 = w_lat;
        casm_f = 3.0;
    }

    private double random_yj() {
        double num = 314159269.0;
        double num2 = 453806245.0;
        casm_rr = (num * casm_rr) + num2;
        double num3 = (int) (casm_rr / 2.0);
        casm_rr -= num3 * 2.0;
        casm_rr /= 2.0;
        return casm_rr;
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
        double num = ((((300.0 + (1.0 * x)) + (2.0 * y)) + ((0.1 * x) * x)) + ((0.1 * x) * y))
                + (0.1 * Math.sqrt(Math.sqrt(x * x)));
        num += ((20.0 * yjSin2(18.849555921538762 * x)) + (20.0 * yjSin2(6.283185307179588 * x))) * 0.6667;
        num += ((20.0 * yjSin2(3.141592653589794 * x)) + (40.0 * yjSin2(1.0471975511965981 * x))) * 0.6667;
        return (num + (((150.0 * yjSin2(0.26179938779914952 * x)) + (300.0 * yjSin2(0.10471975511965979 * x))) * 0.6667));
    }

    private double transform_yjy5(double x, double y) {
        double num = ((((-100.0 + (2.0 * x)) + (3.0 * y)) + ((0.2 * y) * y)) + ((0.1 * x) * y))
                + (0.2 * Math.sqrt(Math.sqrt(x * x)));
        num += ((20.0 * yjSin2(18.849555921538762 * x)) + (20.0 * yjSin2(6.283185307179588 * x))) * 0.6667;
        num += ((20.0 * yjSin2(3.141592653589794 * y)) + (40.0 * yjSin2(1.0471975511965981 * y))) * 0.6667;
        return (num + (((160.0 * yjSin2(0.26179938779914952 * y)) + (320.0 * yjSin2(0.10471975511965979 * y))) * 0.6667));
    }

    private MapPoint wgtochina_lb(int wg_flag, int wg_lng, int wg_lat, int wg_heit, int wg_week, int wg_time) {
        MapPoint point = null;
        if (wg_heit <= 0x1388) {
            double num = wg_lng;
            num /= 3686400.0;
            double x = wg_lat;
            x /= 3686400.0;
            if (num < 72.004) {
                return point;
            }
            if (num > 137.8347) {
                return point;
            }
            if (x < 0.8293) {
                return point;
            }
            if (x > 55.8271) {
                return point;
            }
            if (wg_flag == 0) {
                iniCasm( wg_time,  wg_lng,  wg_lat);
                point = new MapPoint();
                point.setLatitude( wg_lng);
                point.setLongitude( wg_lat);
                return point;
            }
            casm_t2 = wg_time;
            double num3 = (casm_t2 - casm_t1) / 1000.0;
            if (num3 <= 0.0) {
                casm_t1 = casm_t2;
                casm_f++;
                casm_x1 = casm_x2;
                casm_f++;
                casm_y1 = casm_y2;
                casm_f++;
            } else if (num3 > 120.0) {
                if (casm_f == 3.0) {
                    casm_f = 0.0;
                    casm_x2 = wg_lng;
                    casm_y2 = wg_lat;
                    double num4 = casm_x2 - casm_x1;
                    double num5 = casm_y2 - casm_y1;
                    double num6 = Math.sqrt((num4 * num4) + (num5 * num5)) / num3;
                    if (num6 > 3185.0) {
                        return point;
                    }
                }
                casm_t1 = casm_t2;
                casm_f++;
                casm_x1 = casm_x2;
                casm_f++;
                casm_y1 = casm_y2;
                casm_f++;
            }
            double xx = transformYj5(num - 105.0, x - 35.0);
            double yy = transform_yjy5(num - 105.0, x - 35.0);
            xx = ((xx + (wg_heit * 0.001)) + yjSin2(wg_time * 0.0174532925199433)) + random_yj();
            yy = ((yy + (wg_heit * 0.001)) + yjSin2(wg_time * 0.0174532925199433)) + random_yj();
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
            if (num == 1.0) {
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
        if (num == 1.0) {
            num4 = -num4;
        }
        return num4;
    }
}
