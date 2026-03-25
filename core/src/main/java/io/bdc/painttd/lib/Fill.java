package io.bdc.painttd.lib;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class Fill {
    protected Batch batch;
    protected TextureRegion region;
    protected Texture texture;
    float[] vertices = new float[128];
    FloatArray points = new FloatArray();

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
        this.texture = region.getTexture();
    }

    public void rect(float x, float y, float width, float height) {
        batch.draw(region, x, y, width, height);
    }

    public void crect(float x, float y, float width, float height) {
        rect(x - width / 2, y - height / 2, width, height);
    }

    public void tri(float x1, float y1, float x2, float y2, float x3, float y3) {
        quad(x1, y1, x2, y2, x3, y3, x3, y3);
    }

    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float u = region.getU();
        float v = region.getV2();
        float u2 = region.getU2();
        float v2 = region.getV();
        float c = batch.getPackedColor();

        int idx = 0;
        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = c;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = c;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = c;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = x4;
        vertices[idx++] = y4;
        vertices[idx++] = c;
        vertices[idx++] = u2;
        vertices[idx] = v;

        batch.draw(texture, vertices, 0, 20);
    }

    public void polygon(float x, float y, float size, float rotation, int sides) {
        // 确保至少有3条边
        sides = Math.max(3, sides);

        // 计算每个顶点的角度
        float angleStep = 2 * MathUtils.PI / sides;

        // 将旋转角度转换为弧度
        float rotationRad = rotation * MathUtils.degreesToRadians;

        // 计算多边形的顶点坐标
        points.clear();
        for (int i = 0; i < sides; i++) {
            float angle = i * angleStep + rotationRad;
            points.add(x + size * MathUtils.cos(angle));
            points.add(y + size * MathUtils.sin(angle));
        }

        // 使用tri函数绘制多边形，将多边形分解为多个三角形
        // 所有三角形共享中心点
        for (int i = 0; i < sides - 1; i++) {
            tri(x, y, points.items[i * 2], points.items[i * 2 + 1], points.items[(i + 1) * 2], points.items[(i + 1) * 2 + 1]);
        }
        tri(x, y, points.items[(sides - 1) * 2], points.items[(sides - 1) * 2 + 1], points.items[0], points.items[1]);
    }

    public void circle(float x, float y, float radius, int sides) {
        polygon(x, y, radius, 0, sides);
    }

    public void circle(float x, float y, float radius, int sides, float rotation) {
        polygon(x, y, radius, rotation, sides);
    }

    /**
     * 绘制椭圆
     *
     * @param cx       椭圆中心X坐标
     * @param cy       椭圆中心Y坐标
     * @param a        半长轴（X轴方向半径）
     * @param b        半短轴（Y轴方向半径）
     * @param rotation 旋转角度（度）
     * @param sides    边数（控制椭圆平滑度）
     */
    public void ellipse(float cx, float cy, float a, float b, float rotation, int sides) {
        // 确保至少有3条边
        sides = Math.max(3, sides);

        // 将旋转角度转换为弧度
        float rotationRad = rotation * MathUtils.degreesToRadians;

        // 预计算旋转矩阵的sin和cos值
        float cosRot = MathUtils.cos(rotationRad);
        float sinRot = MathUtils.sin(rotationRad);

        // 计算每个顶点的角度步长
        float angleStep = 2 * MathUtils.PI / sides;

        // 计算椭圆的顶点坐标
        points.clear();
        for (int i = 0; i < sides; i++) {
            float angle = i * angleStep;
            float cosAngle = MathUtils.cos(angle);
            float sinAngle = MathUtils.sin(angle);

            // 椭圆参数方程（未旋转）
            float x = a * cosAngle;
            float y = b * sinAngle;

            // 应用旋转
            float rotatedX = x * cosRot - y * sinRot;
            float rotatedY = x * sinRot + y * cosRot;

            // 平移至中心点
            points.add(cx + rotatedX);
            points.add(cy + rotatedY);
        }

        // 使用tri函数绘制椭圆，将椭圆分解为多个三角形
        // 所有三角形共享中心点
        for (int i = 0; i < sides - 1; i++) {
            tri(cx, cy,
                points.items[i * 2], points.items[i * 2 + 1],
                points.items[(i + 1) * 2], points.items[(i + 1) * 2 + 1]);
        }
        // 连接最后一个顶点和第一个顶点
        tri(cx, cy,
            points.items[(sides - 1) * 2], points.items[(sides - 1) * 2 + 1],
            points.items[0], points.items[1]);
    }

    /**
     * 绘制椭圆（简化版，无旋转）
     */
    public void ellipse(float cx, float cy, float a, float b, int sides) {
        ellipse(cx, cy, a, b, 0, sides);
    }

    public void polygonStart() {
        points.clear();
    }

    public void polygonAdd(float x, float y) {
        points.add(x);
        points.add(y);
    }

    public void polygonEnd() {
        if (points.size > 0) {
            fill(points.items, points.size);
        }
    }

    public void fill(float[] vertices, int size) {
        if (size < 2 * 3) return;

        for (int i = 2; i < size - 4; i += 4) {
            quad(
                vertices[0], vertices[1],
                vertices[i], vertices[i + 1],
                vertices[i + 2], vertices[i + 3],
                vertices[i + 4], vertices[i + 5]
            );
        }
    }
}
