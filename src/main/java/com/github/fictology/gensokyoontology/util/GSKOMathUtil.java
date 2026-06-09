package com.github.fictology.gensokyoontology.util;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GSKOMathUtil {


    /**
     * 数值周期约束算法，比一般的约束算法多了一个判断周期的条件。周期的判定如下：<br>
     * 当数值小于下限时，即number
     * %
     * max
     * -
     * min
     * ==
     * 0同时number / max <= 1时，返回number本身，否则，先获取数值除以上限之后的整数部分，表示数值是上限的多少倍，然后用上限乘以这个倍率，最后用数值与这个结果相减，得出数值在下限和上限约束的周期内的对应值。
     */
    public static double clampPeriod(double number, double min, double max) {
        return number % max - min == 0 && number / max <= 1 ? number :
                number - max * Math.floor(number / max);
    }

    public static float clampPeriod(float number, float min, float max) {
        return number % max - min == 0 && number / max <= 1 ? number :
                (float) (number - max * Math.floor(number / max));
    }

    public static int clampPeriod(int number, int min, int max) {
        return number % max - min == 0 && number / max <= 1 ? number :
                (int) (float) (number - max * Math.floor((double) number / max));
    }

    /**
     * 波动周期算法，用于处理周期长度为 max - min，且周期在最大值和最小值之间线性变化，当超过最大值后，函数开始线性递减，直到达到最小值，然后再次线性递增的算法
     */
    public static float wavyPeriod(float time, float min, float max) {
        float period = max - min;
        float mod = time % (period * 2);
        return mod <= period ? min + mod : max - (mod - period);
    }

    /**
     * 波动周期算法，用于处理周期长度为 max - min，且周期在最大值和最小值之间线性变化，当超过最大值后，函数开始线性递减，直到达到最小值，然后再次线性递增的算法
     */
    public static double wavyPeriod(double time, double max, double min) {
        double period = max - min;
        double mod = time % (period * 2);
        return mod <= period ? min + mod : max - (mod - period);
    }

    /**
     * 传入四个双精度浮点数
     *
     * @param x1 第一个点的x坐标
     * @param y1 第一个点的y坐标
     * @param x2 第二个点的x坐标
     * @param y2 第二个点的y坐标
     * @return 上述两点间的距离
     */
    public static double distanceOf2D(double x1, double y1, double x2, double y2) {
        return Math.sqrt(square(x1 - x2) + square(y1 - y2));
    }

    public static double distanceOf3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.pow(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 + z2, 2), 0.5);
    }

    public static ArrayList<Vec3> getCirclePoints2D(Vec3 center, double radius, int count) {

        ArrayList<Vec3> coordinates = new ArrayList<>();
        double radians = (Math.PI / 180) * Math.round(360d / count);
        for (int i = 0; i < count; i++) {
            double x = center.x() + radius * Math.sin(radians * i);
            double y = center.y() + radius * Math.cos(radians * i);
            Vec3 coordinate = new Vec3(x, y, 0);
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    public static Vec3 getPointOnCircle(Vec3 center, double radius, double angle) {
        return new Vec3(
                center.x() + radius * Math.cos(Math.toDegrees(angle)),
                0, center.z() + radius * Math.sin(Math.toDegrees(angle)));
    }

    public static Vec3 getIntersection(Vec3 A1, Vec3 A2, Vec3 B1, Vec3 B2) {
        // 计算两条线段的方向向量
        Vec3 d1 = A2.subtract(A1); // 第一条线段的方向向量
        Vec3 d2 = B2.subtract(B1); // 第二条线段的方向向量

        // 定义变量
        double a1 = d1.x;
        double b1 = -d2.x;
        double c1 = B1.x - A1.x;

        double a2 = d1.y;
        double b2 = -d2.y;
        double c2 = B1.y - A1.y;

        // 使用克拉默法则求解 t1 和 t2
        double denominator = a1 * b2 - a2 * b1;
        if (denominator == 0) {
            // 平行或无解
            return null;
        }

        // 解出 t1 和 t2
        double t1 = (c1 * b2 - c2 * b1) / denominator;
        double t2 = (a1 * c2 - a2 * c1) / denominator;

        // 计算交点
        return A1.add(d1.scale(t1));
    }

    public static Vec3 getIntersectionFromRot(Vec3 start, Vec3 startRotation, Vec3 end, Vec3 endRotation) {
        Vec3 r = start.subtract(end);
        double a = startRotation.dot(startRotation); // D1·D1
        double e = endRotation.dot(endRotation); // D2·D2
        double f = endRotation.dot(r);  // D2·(P1 - P2)

        double epsilon = 1e-6;

        if (a <= epsilon && e <= epsilon) {
            // 两条射线方向向量的长度接近零
            return start;
        }

        double s, t;

        if (a <= epsilon) {
            // D1接近零，用P1点作为交点
            s = 0.0;
            t = f / e;
        } else {
            double c = startRotation.dot(r);
            if (e <= epsilon) {
                // D2接近零，用P2点作为交点
                t = 0.0;
                s = -c / a;
            } else {
                double b = startRotation.dot(endRotation); // D1·D2
                double denominator = a * e - b * b;

                if (Math.abs(denominator) > epsilon) {
                    s = (b * f - c * e) / denominator;
                } else {
                    // 两条射线平行
                    return end;
                }

                t = (b * s + f) / e;
            }
        }

        // 计算交点坐标
        Vec3 point1 = start.add(startRotation.scale(s));
        Vec3 point2 = end.add(endRotation.scale(t));

        // 如果交点之间的距离接近零，则认为射线相交
        if (point1.distanceTo(point2) <= epsilon) {
            return point1;
        } else {
            return start;
        }
    }

    public static Vec3 bezier2(Vec3 start, Vec3 end, Vec3 p, float time) {
        return lerp(time, lerp(time, start, p), lerp(time, p, end));
    }

    /**
     * 用于小于等于90度的水平转弯
     *
     * @param start 起始点
     * @param p     控制点
     * @param end   终点
     * @param time  步长
     * @return 在控制点的作用下，以起止点为基准的下一个点在曲线上的位置
     */
    public static Vec3 bezier2(Vec3 start, Vec3 end, Vec3 p, double time) {
        if (time > 1 || time < 0)
            return null;
        Vec3 v1 = start.scale(pow2(1 - time));
        Vec3 v2 = p.scale(2 * time * (1 - time));
        Vec3 v3 = end.scale(pow2(time));

        return v1.add(v2.add(v3));
    }

    public static Vec3 bezier3(Vec3 p1, Vec3 p2, Vec3 q1, Vec3 q2, float time) {
        if (time > 1 || time < 0)
            return null;
        Vec3 v1 = lerp(time, p1, q1);
        Vec3 v2 = lerp(time, q1, q2);
        Vec3 v3 = lerp(time, q2, p2);
        Vec3 inner1 = lerp(time, v1, v2);
        Vec3 inner2 = lerp(time, v2, v3);
        return lerp(time, inner1, inner2);
    }

    public static Vec3 bezierDerivative(Vec3 p1, Vec3 p2, Vec3 q1, Vec3 q2, float t) {
        return p1.scale(-3 * t * t + 6 * t - 3)
                .add(q1.scale(9 * t * t - 12 * t + 3))
                .add(q2.scale(-9 * t * t + 6 * t))
                .add(p2.scale(3 * t * t));
    }

    public static Vec3 lerp(float progress, Vec3 start, Vec3 end) {
        return start.add(end.subtract(start).scale(progress));
    }

    public static BlockPos lerp(float progress, BlockPos start, BlockPos end) {
        int x = (int) Math.floor(lerp(progress, start.getX(), end.getX()));
        int y = (int) Math.floor(lerp(progress, start.getY(), end.getY()));
        int z = (int) Math.floor(lerp(progress, start.getZ(), end.getZ()));
        return new BlockPos(x, y, z);
    }

    // tickExisted / MAX_TICK => not very smooth
    // tickExisted / MAX_TICK => a value between zero and one
    // partialTicks => a value between zero and one and is called between each tick
    // tick: 0, 0.1, 0.2, 0.3 ... 1, 1.1, 1.2, 1.3 ... 2 ...
    // ∴ (tickEx + partial) / MAX_TICK => a very smooth approach.
    public static float lerpTicks(float partial, int maxTick, int presentTick, float minValue, float maxValue) {
        return lerp((presentTick + partial) / maxTick, minValue, maxValue);
    }

    private static float lerp(float t, float minValue, float maxValue) {
        return minValue + ((maxValue - minValue * t));
    }

    public static float wavyLerpTicks(int presentTick, int monotonicPeriod, float partial, float min, float max) {
        float mod = (presentTick + partial) % (monotonicPeriod * 2);
        float lerpTick = lerpTicks(partial, monotonicPeriod, clampPeriod(presentTick, 0, monotonicPeriod * 2), min, max);
        return mod <= monotonicPeriod ? min + lerpTick : lerpTick - (mod - monotonicPeriod);
    }

    /**
     * 求三维向量模长的运算
     *
     * @param x 三维向量的x坐标
     * @param y 三维向量的y坐标
     * @param z 三维向量的z坐标
     * @return 返回三维向量的模长，或者立方体体对角线的长度
     */
    public static double toModulus3D(double x, double y, double z) {
        return Math.pow(x * x + y * y + z * z, 0.5);
    }

    public static double toModulus2D(double x, double y) {
        return Math.pow(x * x + y * y, 0.5);
    }

    /**
     * 球坐标转直角坐标，注意计算机图形学中的空间坐标与数学的空间坐标不同，x, z轴为水平轴，而y轴为竖直轴，
     *
     * @param sc 球坐标的三维的向量，成员属性 x 为球坐标半径Radius，y 为球坐标天顶角theta，z为球坐标方位角phi
     * @return 返回空间直角坐标系的三维向量
     */
    public static Vec3 toRectVec(Vec3 sc) {
        return new Vec3(sc.x * Math.sin(sc.y) * Math.cos(sc.z),
                sc.x * Math.sin(sc.y) * Math.sin(sc.z),
                sc.x * Math.cos(sc.y));
    }

    /**
     * 直角坐标转球坐标
     *
     * @param rc 空间直角坐标系的三维向量
     * @return 返回球坐标系的三维向量
     */
    public static Vec3 toSphereVec(Vec3 rc) {
        double r = GSKOMathUtil.toModulus3D(rc.x, rc.y, rc.z);
        return new Vec3(r, Math.acos(rc.z / r), Math.atan(rc.y / rc.x));
    }

    public static Vec3 toSphereVec(double x, double y, double z) {
        return toSphereVec(new Vec3(x, y, z));
    }

    /**
     * 思路是：先将一个圆的所有点的集合传进来，这个集合中存放的点是使用的平面直角坐标系，然后，将这些点转为球坐标系表示，并执行球坐标上的天顶角和方位角旋转。
     *
     * @param circleDots    组成一个圆周的所有点的平面直角坐标
     * @param thetaRotation 天顶角的旋转度数，取值为 0 ~ π
     * @return 旋转之后该圆周的每个新的点的平面直角坐标
     */
    public static List<Vec3> rotateCircle(List<Vec3> circleDots, double thetaRotation, double phiRotation) {
        List<Vec3> nextCircleDots = new ArrayList<>();
        for (Vec3 dotOnCircle : circleDots) {

            Vec3 prevSphereVec = toSphereVec(dotOnCircle);
            Vec3 nextSphereVec = new Vec3(prevSphereVec.x, prevSphereVec.y + thetaRotation, prevSphereVec.z + phiRotation);
            nextCircleDots.add(toRectVec(nextSphereVec));
        }
        return nextCircleDots;
    }

    public static Vec3 rotateCircleDot(Vec3 rectVec, double thetaRotation, double phiRotation) {
        Vec3 sphereVec = toSphereVec(rectVec);
        return new Vec3(sphereVec.x, sphereVec.y + thetaRotation, sphereVec.z + phiRotation);
    }

    public static Vec3 toLocalCoordinate(Vec3 newOriginIn, Vec3 globalIn) {
        return new Vec3(globalIn.x() - newOriginIn.x(),
                globalIn.y() - newOriginIn.y(),
                globalIn.z() - newOriginIn.z());

    }

    /**
     * 计算方法：设斜边为r，两条直角边为x和y，斜边与y轴夹角为d，那么——
     * <p>
     * 1. 求出 tan(d) 的值，为一个常量，用a表示，由已知条件可得：tan()函数表示的是对边比邻边，即x比上y，且两条直角边的平方和等于斜边的平方，所以——
     * <p>
     * 2. 设方程① -- x / y = a;
     * <p>
     * 3. 设方程② -- x^2 + y^2 = r^2;
     * <p>
     * 4. 联立方程①②可得：
     * <p>
     * x = a * y;
     * <p>
     * a^2 * y^2 + y^2 = r^2;
     * <p>
     * y^2 * (a^2 + 1) = r^2;
     * <p>
     * ∴ y = √(z^2 / a^2 + 1)
     *
     * @param hypotenuse 斜边长，即空间向量在该平面上的投影线段
     * @param roll       斜边与y轴的夹角
     * @return 返回一个仅有可知的两边组成的平面向量坐标
     */
    public static Vec3 toRollCoordinate(double hypotenuse, double roll) {
        double x = Math.sqrt(Math.pow(hypotenuse, 2) / Math.pow(Math.tan(roll), 2) + 1);
        return new Vec3(x, Math.tan(roll) * x, 0);
    }

    public static Vec3 toYawCoordinate(double hypotenuse, double yaw) {
        double z = Math.sqrt(Math.pow(hypotenuse, 2) / Math.pow(Math.tan(yaw), 2) + 1);
        return new Vec3(Math.tan(yaw) * z, 0, z);
    }

    public static Vec3 toPitchCoordinate(double hypotenuse, double pitch) {
        double y = Math.sqrt(Math.pow(hypotenuse, 2) / Math.pow(Math.tan(pitch), 2) + 1);
        return new Vec3(0, y, Math.tan(pitch) * y);
    }

    public static Vec3 vecCeil(Vec3 vec) {
        return new Vec3(
                Math.ceil(vec.x()),
                Math.ceil(vec.y()),
                Math.ceil(vec.z()));
    }

    public static Vec3 vecFloor(Vec3 vec) {
        return new Vec3(
                Math.floor(vec.x()),
                Math.floor(vec.y()),
                Math.floor(vec.z()));
    }

    public static Vec2 getEulerAngle(Vec3 vectorA, Vec3 vectorB) {
        // 计算旋转矩阵的第一行
        double m11 = vectorA.x * vectorB.x + vectorA.y * vectorB.y;
        double m12 = vectorA.x * vectorB.y - vectorA.y * vectorB.x;
        double m13 = vectorA.z * vectorB.x;

        // 计算旋转矩阵的第三行
        double m31 = -vectorA.y;
        double m32 = vectorA.x;
        double m33 = 0;

        // 计算 yaw、pitch 和 roll 欧拉角
        double yaw = Math.atan2(m12, m11);
        double pitch = Math.asin(m31);

        return new Vec2((float) yaw, (float) pitch);
    }

    public static int randomRange(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static float randomRange(float min, float max) {
        Random random = new Random();
        float randomValue = random.nextFloat();
        return min + (randomValue * (max - min));
    }

    public static double randomRange(double min, double max) {
        Random random = new Random();
        double randomValue = random.nextDouble();
        return min + (randomValue * (max - min));
    }

    public static Vec3 randomVec(double min, double max) {
        return new Vec3(randomRange(min, max), randomRange(min, max), randomRange(min, max));
    }

    public static <V> V rollByWeight(int total, int weight, V value) {
        return new Random().nextInt(total) < weight ? null : value;
    }

    public static Vec2 toYawPitch(Vec3 vector3d) {
        double yaw = Math.atan2(-vector3d.x, vector3d.z);
        double pitch = Math.atan2(vector3d.y, Math.sqrt(vector3d.x * vector3d.x + vector3d.z * vector3d.z));
        return new Vec2((float) toDegree(yaw), (float) toDegree(pitch));
    }

    public static Direction toDirection(Vec3 vector3d) {
        double x = vector3d.x;
        double y = vector3d.y;
        double z = vector3d.z;

        // 确定方向
        if (Math.abs(y) > 0.5) { // 垂直方向优先
            return y > 0 ? Direction.UP : Direction.DOWN;
        } else if (Math.abs(x) > Math.abs(z)) {
            return x > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    // public static void rotateMatrixToLookVec(PoseStack matrixStackIn, Vec3 lookVec) {
    //     Vec3 rotationVec = lookVec.reverse();
    //     float f5 = (float)Math.acos(rotationVec.y);
    //     float f6 = (float)Math.atan2(rotationVec.z, rotationVec.x);
    //     matrixStackIn.rotateAround(Vector3f.YP.rotationDegrees(((float)Math.PI / 2 - f6) * (180 / (float)Math.PI)));
    //     matrixStackIn.rotateAround(Vector3f.XP.rotationDegrees(f5 * (180 / (float)Math.PI)));
    // }

    public static Vec3 fromYawPitch(float yaw, float pitch) {
        return new Vec3(Math.cos(yaw) * Math.cos(pitch), Math.sin(yaw) * Math.sin(pitch), Math.sin(pitch));
    }

    /**
     * 弧度值除以Math.PI的结果为【180度的几分之几】
     *
     * @param radIn 弧度值
     * @return 角度值
     */
    public static double toDegree(double radIn) {
        return radIn / Math.PI * 180d;
    }

    /**
     * Math.PI 除以180度的结果为【每一角度等于多少弧度】
     *
     * @param degIn 角度值
     * @return 弧度值
     */
    public static double toRadian(double degIn) {
        return degIn * Math.PI / 180d;
    }

    public static double pow2(double base) {
        return square(base);
    }

    public static double pow3(double base) {
        return cube(base);
    }

    public static double square(double base) {
        return base * base;
    }

    public static double cube(double base) {
        return base * base * base;
    }

    public static boolean isBetween(int num, int min, int max) {
        return num >= min && num < max;
    }

    public static boolean isBetween(float num, float min, float max) {
        return num >= min && num < max;
    }

    public static boolean isBetween(double num, double min, double max) {
        return num >= min && num < max;
    }

}
