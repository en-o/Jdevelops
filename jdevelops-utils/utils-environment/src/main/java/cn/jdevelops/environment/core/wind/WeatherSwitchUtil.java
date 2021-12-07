package cn.jdevelops.environment.core.wind;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.*;

/**
 * 风向公共工具类
 * @author tn
 * @date 2020-09-18 10:40
 */
public class WeatherSwitchUtil {


    /**
     * 根据角度算风向
     * @param windAvg2Mi 角度
     * @return String
     */
    public static String getWindDirection(double windAvg2Mi){
        String windDirection;
        if( 0 == windAvg2Mi  || 360 == windAvg2Mi  ){
            windDirection = "北";
        } else if(windAvg2Mi == 90){
            windDirection = "东";
        } else if(windAvg2Mi == 180){
            windDirection = "南";
        } else if(windAvg2Mi == 270){
            windDirection = "西";
        } else if(windAvg2Mi > 0 && windAvg2Mi < 90){
            windDirection = "东北";
        } else if(windAvg2Mi > 90 && windAvg2Mi < 180){
            windDirection = "东南";
        } else if(windAvg2Mi > 180 && windAvg2Mi < 270){
            windDirection = "西南";
        } else {
            windDirection = "西北";
        }
        return windDirection;
    }



    private static Map<String, String> windPowerMap = getWindInfo();
    private static Map<String, String> windDirectMap = getWindDirect();

    public static String getWindPower(double windVelocity){
        for(Entry<String, String> map : windPowerMap.entrySet()){
            String windRegion = map.getValue();
            String windName = map.getKey();
            if(windRegion.contains("-")){
                String[] windRegionArray = windRegion.split("-");
                double windMin = Double.parseDouble(windRegionArray[0]);
                double windMax = Double.parseDouble(windRegionArray[1]);
                if(windVelocity >= windMin && windVelocity <= windMax + 0.1){
                    return windName;
                }
            }

            if(windRegion.contains(">")){
                double windMin = Double.parseDouble(windRegion.split(">")[0]);
                if(windVelocity >= windMin){
                    return windName;
                }
            }
        }

        return "无风";
    }

    public static String getWindVelocity(double windVelocity){
        for(Entry<String, String> map : windDirectMap.entrySet()){
            String windRegion = map.getValue();
            String windName = map.getKey();
            if(windRegion.contains("-")){
                String[] windRegionArray = windRegion.split("-");
                double windMin = Double.parseDouble(windRegionArray[0]);
                double windMax = Double.parseDouble(windRegionArray[1]);
                if(windVelocity >= windMin && windVelocity <= windMax + 0.01){
                    return windName;
                }

                if(windVelocity >= 348.76 || windVelocity <= 11.25){
                    return windName;
                }
            }
        }

        return "无风";
    }

    private static Map<String, String> getWindInfo(){
        Map<String, String> windPowerMap = new HashMap<>(16);
        windPowerMap.put("无风", "0.0-0.2");
        windPowerMap.put("软风", "0.3-1.5");
        windPowerMap.put("轻风", "1.6-3.3");
        windPowerMap.put("微风", "3.4-5.4");
        windPowerMap.put("和风", "5.5-7.9");
        windPowerMap.put("清风", "8.0-10.7");
        windPowerMap.put("强风", "10.8-13.8");
        windPowerMap.put("劲风", "13.9-17.1");
        windPowerMap.put("大风", "17.2-20.7");
        windPowerMap.put("烈风", "20.8-24.4");
        windPowerMap.put("狂风", "24.5-28.4");
        windPowerMap.put("暴风", "28.5-32.6");
        windPowerMap.put("台风", ">32.6");
        return windPowerMap;
    }

    private static Map<String, String> getWindDirect(){
        Map<String, String> windDirectMap = new HashMap<>(16);
        windDirectMap.put("北", "348.76-11.25");
        windDirectMap.put("北东北", "11.26-33.75");
        windDirectMap.put("东北", "33.76-56.25");
        windDirectMap.put("东东北", "56.26-78.75");
        windDirectMap.put("东", "78.76-101.25");
        windDirectMap.put("东东南", "101.26-123.75");
        windDirectMap.put("东南", "123.76-146.25");
        windDirectMap.put("南东南", "146.26-168.75");
        windDirectMap.put("南", "168.76-191.25");
        windDirectMap.put("南西南", "191.26-213.75");
        windDirectMap.put("西南", "213.76-236.25");
        windDirectMap.put("西西南", "236.26-258.75");
        windDirectMap.put("西", "258.76-281.25");
        windDirectMap.put("西西北", "281.26-303.75");
        windDirectMap.put("西北", "303.76-326.25");
        windDirectMap.put("北西北", "326.26-348.75");
        return windDirectMap;
    }
}
