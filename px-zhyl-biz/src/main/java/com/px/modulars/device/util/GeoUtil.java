package com.px.modulars.device.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.px.modulars.device.entity.ScopeRequest;

import java.awt.geom.Path2D;
import java.util.List;

public class GeoUtil {
    /**
     * 使用Path2D创建一个多边形
     *
     * @param polygon 经纬度 集合
     * @return 返回Path2D.Double
     */
    private static Path2D.Double create(List<ScopeRequest> polygon) {
        //创建path2D对象
        Path2D.Double generalPath = new Path2D.Double();
        //获取第一个起点经纬度的坐标
        ScopeRequest first = polygon.get(0);
        //通过移动到以double精度指定的指定坐标，把第一个起点添加到路径中
        generalPath.moveTo(first.getLongitude(), first.getLatitude());
        //把集合中的第一个点删除防止重复添加
        polygon.remove(0);
        //循环集合里剩下的所有经纬度坐标
        for (ScopeRequest d : polygon) {
            //通过从当前坐标绘制直线到以double精度指定的新指定坐标，将路径添加到路径。
            //从第一个点开始，不断往后绘制经纬度点
            generalPath.lineTo(d.getLongitude(), d.getLatitude());
        }
        // 最后要多边形进行封闭，起点及终点
        generalPath.lineTo(first.getLongitude(), first.getLatitude());
        //将直线绘制回最后一个 moveTo的坐标来关闭当前子路径。
        generalPath.closePath();
        return generalPath;
    }

    /**
     * 判断点是否在区域内
     *
     * @param polygon   区域经纬度json字符串
     * @param longitude 经度
     * @param latitude  纬度
     * @return 返回true跟false
     */
    public static boolean isPoint(String polygon, double longitude, double latitude) {

        JSONArray jsonArray = JSON.parseArray(polygon);
        //将json转换成对象
        List<ScopeRequest> list = JSON.parseArray(jsonArray.toJSONString(), ScopeRequest.class);
        Path2D path2D = create(list);
        //true如果指定的坐标在Shape边界内; 否则为false 。
        return path2D.contains(longitude, latitude);
    }


    /**
     * 默认地球半径
     */

    private static double EARTH_RADIUS = 6371000;//赤道半径(单位m)

    /**
     * 转化为弧度(rad)
     */

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     */

    public static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;

    }

    /**
     * 计算中心经纬度与目标经纬度的距离(米)
     *
     * @param centerLon 中心精度
     * @param centerLat 中心纬度
     * @param targetLon 需要计算的精度
     * @param targetLat 需要计算的纬度
     * @return 米
     */
    public static double distance(double centerLon, double centerLat, double targetLon, double targetLat) {
        double jl_jd = 102834.74258026089786013677476285;// 每经度单位米;
        double jl_wd = 111712.69150641055729984301412873;// 每纬度单位米;
        double b = Math.abs((centerLat - targetLat) * jl_jd);
        double a = Math.abs((centerLon - targetLon) * jl_wd);
        return Math.sqrt((a * a + b * b));
    }

}
