package io.blackdeluxecat.painttd.utils.color;

import io.blackdeluxecat.painttd.utils.math.FloatVector;

import java.util.Collections;
import java.util.LinkedList;


public class TileColor {
    //所有颜色
    LinkedList<HSVColor> colorList = new LinkedList<>();

    /*该向量参数依次表示色相（方位角），饱和度（距中轴线距离），明度（高度）
    *原点在下底面中心
    * */
    public FloatVector colorVector;
    //颜色
    public HSVColor finalColor;
    //参考索引
    private static final int HUE = 0;
    private static final int SATURATION = 1;
    private static final int VALUE = 2;

    public TileColor() {
        colorVector = new FloatVector(0.0f,0.0f,0.0f);
    }
    //三个属性
    public TileColor(float hue, float saturation, float brightness) {
        colorVector = new FloatVector(hue, saturation, brightness);
    }

    //总饱和度，加权的核心（？），同时作为向量缩放的系数
    float totalSaturation = 0.0f;
    public TileColor mixWith(HSVColor ...colors) {
        //放入颜色堆
        Collections.addAll(colorList, colors);
        //计算总饱和度
        for (HSVColor color : colorList) {
            totalSaturation += color.s;
        }

        float totalWeight = 0.0f;
        float weightedX = 0.0f;
        float weightedY = 0.0f;
        float weightedZ = 0.0f;

        for (HSVColor color : colorList) {
            // 将HSV转换为色柱图坐标系中的点
            float hueRad = (float) Math.toRadians(color.h);
            float radius = color.s; // 饱和度作为到中心轴的距离
            float height = color.v; // 明度作为高度

            // 转换为直角坐标
            float x = radius * (float) Math.cos(hueRad);
            float y = radius * (float) Math.sin(hueRad);
            float z = height;

            // 使用饱和度作为权重
            float weight = color.s;
            weightedX += x * weight;
            weightedY += y * weight;
            weightedZ += z * weight;
            totalWeight += weight;
        }

// 计算加权平均位置
        float avgX = weightedX / totalWeight;
        float avgY = weightedY / totalWeight;
        float avgZ = weightedZ / totalWeight;

// 转换回HSV坐标
        float radius = (float) Math.sqrt(avgX * avgX + avgY * avgY);
        float hue = (float) Math.toDegrees(Math.atan2(avgY, avgX));
        float saturation = radius;
        float brightness = avgZ;

// 确保色相在0-360范围内
        while (hue < 0.0f) {
            hue += 360.0f;
        }
        while (hue >= 360.0f) {
            hue -= 360.0f;
        }

// 限制饱和度和亮度在有效范围内
        saturation = Math.max(0.0f, Math.min(1.0f, saturation));
        brightness = Math.max(0.0f, Math.min(1.0f, brightness));
//极短向量归零
        if (saturation < 0.0001f) {
            saturation = 0.0f;
        }
// 更新颜色向量
        this.colorVector.set(0, hue);
        this.colorVector.set(1, saturation);
        this.colorVector.set(2, brightness);

        finalColor = new HSVColor(hue,saturation,brightness);
        return this;
    }

    public String toString() {
        return "TileColor{" +
                "finalColor=" + finalColor +
                '}';
    }
}
