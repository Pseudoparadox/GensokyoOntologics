package github.thelawf.gensokyoontology.common.util.math;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GSKOMathUtil {

    /**
     * 四元数球面线性插值（SLERP）
     * @param start 起始四元数
     * @param end 结束四元数
     * @param t 插值因子 (0.0 - 1.0)
     * @return 插值后的四元数
     */
    public static Quaternion slerp(Quaternion start, Quaternion end, float t) {
        // 计算点积（判断旋转方向）
        float dot = start.getX()*end.getX() + start.getY()*end.getY() +
                start.getZ()*end.getZ() + start.getW()*end.getW();

        // 确保走最短路径
        if (dot < 0) {
            end = new Quaternion(-end.getX(), -end.getY(), -end.getZ(), -end.getW());
            dot = -dot;
        }

        // 处理小角度（线性插值）
        float threshold = 0.9995f;
        if (dot > threshold) {
            Quaternion quaternion = new Quaternion(
                    MathHelper.lerp(t, start.getX(), end.getX()),
                    MathHelper.lerp(t, start.getY(), end.getY()),
                    MathHelper.lerp(t, start.getZ(), end.getZ()),
                    MathHelper.lerp(t, start.getW(), end.getW())
            );
            quaternion.normalize();
            return quaternion;
        }

        // 球面插值
        float angle = (float)Math.acos(dot);
        float sinAngle = (float)Math.sin(angle);
        float w1 = (float)Math.sin((1 - t) * angle) / sinAngle;
        float w2 = (float)Math.sin(t * angle) / sinAngle;

        Quaternion quaternion = new Quaternion(
                w1 * start.getX() + w2 * end.getX(),
                w1 * start.getY() + w2 * end.getY(),
                w1 * start.getZ() + w2 * end.getZ(),
                w1 * start.getW() + w2 * end.getW()
        );
        quaternion.normalize();
        return quaternion;
    }

    private static float qDot(Quaternion q0, Quaternion q1) {
        return q0.getX() * q1.getX() +
                q0.getY() * q1.getY() +
                q0.getZ() * q1.getZ() +
                q0.getW() * q1.getW();
    }

    /**
     * 线性插值（当夹角很小时使用）
     * @param q0 起始四元数
     * @param q1 结束四元数
     * @param t 插值因子
     * @return 插值后的四元数
     */
    private static Quaternion qlerp(Quaternion q0, Quaternion q1, float t) {
        Quaternion result = new Quaternion(
                q0.getX() + t * (q1.getX() - q0.getX()),
                q0.getY() + t * (q1.getY() - q0.getY()),
                q0.getZ() + t * (q1.getZ() - q0.getZ()),
                q0.getW() + t * (q1.getW() - q0.getW())
        );
        result.normalize();
        return result;
    }

    public static Matrix4f transform(MatrixStack matrixStack, Vector3f pivot, Quaternion q, Vector3f scale, Vector3f translation) {
        matrixStack.translate(pivot.getX(), pivot.getY(), pivot.getZ());
        matrixStack.rotate(q);
        matrixStack.scale(scale.getX(), scale.getY(), scale.getZ());
        matrixStack.translate(translation.getX(), translation.getY(), translation.getZ());
        return matrixStack.getLast().getMatrix();
    }

    public static Vector3d rotateBy(Vector3d oldVector, Quaternion rotation) {
        rotation.normalize();

        double qx = rotation.getX();
        double qy = rotation.getY();
        double qz = rotation.getZ();
        double qw = rotation.getW();

        // 提取向量分量
        double vx = oldVector.x;
        double vy = oldVector.y;
        double vz = oldVector.z;

        // 计算 q * v
        double ix = qw * vx + qy * vz - qz * vy;
        double iy = qw * vy + qz * vx - qx * vz;
        double iz = qw * vz + qx * vy - qy * vx;
        double iw = -qx * vx - qy * vy - qz * vz;

        // 计算 (q * v) * q⁻¹
        double x = ix * qw + iw * -qx + iy * -qz - iz * -qy;
        double y = iy * qw + iw * -qy + iz * -qx - ix * -qz;
        double z = iz * qw + iw * -qz + ix * -qy - iy * -qx;

        return new Vector3d(x, y, z);
    }

    public static Color kelvinToRGB(int kelvin) {
        // 确保色温在有效范围内（1000K - 40000K）
        int clampedKelvin = Math.min(40000, Math.max(1000, kelvin));
        double tmp = clampedKelvin / 100.0;

        double red, green, blue;

        // 计算红色分量
        if (tmp <= 66) {
            red = 255;
        } else {
            double redCalc = tmp - 60;
            red = 329.698727446 * Math.pow(redCalc, -0.1332047592);
            red = clamp(red);
        }

        // 计算绿色分量
        if (tmp <= 66) {
            green = 99.4708025861 * Math.log(tmp) - 161.1195681661;
            green = clamp(green);
        } else {
            double greenCalc = tmp - 60;
            green = 288.1221695283 * Math.pow(greenCalc, -0.0755148492);
            green = clamp(green);
        }

        // 计算蓝色分量
        if (tmp >= 66) {
            blue = 255;
        } else if (tmp <= 19) {
            blue = 0;
        } else {
            double blueCalc = tmp - 10;
            blue = 138.5177312231 * Math.log(blueCalc) - 305.0447927307;
            blue = clamp(blue);
        }

        // 四舍五入取整并返回RGB数组
        return new Color((int) Math.round(red), (int) Math.round(green), (int) Math.round(blue));
    }

    public static String[][] rotateMatrix(String[][] matrix, Direction direction) {
        // 处理空矩阵情况
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new String[0][0];
        }


        int m = matrix.length;    // 原矩阵行数
        int n = matrix[0].length; // 原矩阵列数

        // 根据旋转次数k执行不同操作
        switch (direction) {
            case EAST: // 0度：直接返回副本
            case UP:
            case DOWN:
            default:
                return copy(matrix);
            case SOUTH: // 90度：顺时针旋转
                return rotate90(matrix, m, n);
            case WEST: // 180度：旋转两次
                return rotate180(matrix, m, n);
            case NORTH: // 270度（逆时针90度）
                return rotate270(matrix, m, n);
        }
    }


    private static double clamp(double value) {
        return Math.min(255, Math.max(0, value));
    }

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

    public static float normalize(float number, float numberMin, float numberMax) {
        float min = numberMin < 0 ? numberMin : -numberMin;
        float max = numberMax < 0 ? numberMax : -numberMax;
        float abs = min + max;
        float normalized = number % abs;
        if (normalized > 180) normalized -= abs;
        if (normalized < -180) normalized += abs;

        return normalized / 180.0f;
    }

    /**
     * 反归一化
     */
    public static float denormalize(float number, float numberMax) {
        float max = numberMax < 0 ? numberMax : -numberMax;
        return number * max;
    }

    /**
     * 三角波周期算法，思路来源于信号处理中的三角波波形。该算法用于处理周期长度为 max - min，且周期在最大值和最小值之间线性变化，当超过最大值后，函数开始线性递减，直到达到最小值，然后再次线性递增的算法。
     */
    public static float triangularPeriod(float time, float min, float max) {
        float period = max - min;
        float mod = time % (period * 2);
        return mod <= period ? min + mod : max - (mod - period);
    }

    /**
     * 三角波周期算法，思路来源于信号处理中的三角波波形。该算法用于处理周期长度为 max - min，且周期在最大值和最小值之间线性变化，当超过最大值后，函数开始线性递减，直到达到最小值，然后再次线性递增的算法。
     */
    public static double triangularPeriod(double time, double min, double max) {
        double period = max - min;
        double mod = time % (period * 2);
        return mod <= period ? min + mod : max - (mod - period);
    }

    public static float sineSmoothPeriod(float time, float period, float min, float max) {
        float amplitude = (max - min) / 2.0f;
        float midpoint = (min + max) / 2.0f;
        float phase = (time % period) / period * 2.0f * (float)Math.PI;
        float sineValue = (float)Math.sin(phase);
        return midpoint + amplitude * sineValue;
    }

    public static double distanceOf3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.pow(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 + z2, 2), 0.5);
    }

    public static Vector3d getIntersection(Vector3d A1, Vector3d A2, Vector3d B1, Vector3d B2) {
        // 计算两条线段的方向向量
        Vector3d d1 = A2.subtract(A1); // 第一条线段的方向向量
        Vector3d d2 = B2.subtract(B1); // 第二条线段的方向向量

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

    public static Vector3d getIntersectionFromRot(Vector3d start, Vector3d startRotation, Vector3d end, Vector3d endRotation) {
        Vector3d r = start.subtract(end);
        double a = startRotation.dotProduct(startRotation); // D1·D1
        double e = endRotation.dotProduct(endRotation); // D2·D2
        double f = endRotation.dotProduct(r);  // D2·(P1 - P2)

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
            double c = startRotation.dotProduct(r);
            if (e <= epsilon) {
                // D2接近零，用P2点作为交点
                t = 0.0;
                s = -c / a;
            } else {
                double b = startRotation.dotProduct(endRotation); // D1·D2
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
        Vector3d point1 = start.add(startRotation.scale(s));
        Vector3d point2 = end.add(endRotation.scale(t));

        // 如果交点之间的距离接近零，则认为射线相交
        if (point1.distanceTo(point2) <= epsilon) {
            return point1;
        } else {
            return start;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static Vector3d bezier2(Vector3d start, Vector3d end, Vector3d p, float time) {
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
    public static Vector3d bezier2(Vector3d start, Vector3d end, Vector3d p, double time) {
        if (time > 1 || time < 0)
            return null;
        Vector3d v1 = start.scale(pow2(1 - time));
        Vector3d v2 = p.scale(2 * time * (1 - time));
        Vector3d v3 = end.scale(pow2(time));

        return v1.add(v2.add(v3));
    }

    public static Vector3d bezier3(Vector3d start, Vector3d end, Vector3d ctrl1, Vector3d ctrl2, float time) {
        if (time > 1 || time < 0)
            return Vector3d.ZERO;
        Vector3d v1 = lerp(time, start, ctrl1);
        Vector3d v2 = lerp(time, ctrl1, ctrl2);
        Vector3d v3 = lerp(time, ctrl2, end);
        Vector3d inner1 = lerp(time, v1, v2);
        Vector3d inner2 = lerp(time, v2, v3);
        return lerp(time, inner1, inner2);
    }

    public static Vector3d bezier3Derivative(Vector3d start, Vector3d end, Vector3d ctrl1, Vector3d ctrl2, float t) {
        return start.scale(-3 * t * t + 6 * t - 3)
                .add(ctrl1.scale(9 * t * t - 12 * t + 3))
                .add(ctrl2.scale(-9 * t * t + 6 * t))
                .add(end.scale(3 * t * t));
    }

    public static Vector3d bezier2Derivative(Vector3d start, Vector3d ctrl, Vector3d end, float t) {
        Vector3d term1 = ctrl.subtract(start).scale(2 * (1 - t));
        Vector3d term2 = end.subtract(ctrl).scale(2 * t);
        return term1.add(term2);
    }

    public static Vector3d lerp(float progress, Vector3d start, Vector3d end) {
        return start.add(end.subtract(start).scale(progress));
    }

    public static BlockPos lerp(float progress, BlockPos start, BlockPos end) {
        int x = MathHelper.floor(MathHelper.lerp(progress, start.getX(), end.getX()));
        int y = MathHelper.floor(MathHelper.lerp(progress, start.getY(), end.getY()));
        int z = MathHelper.floor(MathHelper.lerp(progress, start.getZ(), end.getZ()));
        return new BlockPos(x, y, z);
    }

    // tickExisted / MAX_TICK => not very smooth
    // tickExisted / MAX_TICK => a value between zero and one
    // partialTicks => a value between zero and one and is called between each tick
    // tick: 0, 0.1, 0.2, 0.3 ... 1, 1.1, 1.2, 1.3 ... 2 ...
    // ∴ (tickEx + partial) / MAX_TICK => a very smooth approach.
    public static float lerpTicks(float partial, int maxTick, int presentTick, float from, float to) {
        return MathHelper.lerp((presentTick + partial) / maxTick, from, to);
    }

    public static float triangularLerpTicks(int presentTick, int monotonicPeriod, float partial, float min, float max) {
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
    public static Vector3d toRectVec(Vector3d sc) {
        return new Vector3d(sc.x * Math.sin(sc.y) * Math.cos(sc.z),
                sc.x * Math.sin(sc.y) * Math.sin(sc.z),
                sc.x * Math.cos(sc.y));
    }

    /**
     * 直角坐标转球坐标
     *
     * @param rc 空间直角坐标系的三维向量
     * @return 返回球坐标系的三维向量
     */
    public static Vector3d toSphereVec(Vector3d rc) {
        double r = GSKOMathUtil.toModulus3D(rc.x, rc.y, rc.z);
        return new Vector3d(r, Math.acos(rc.z / r), Math.atan(rc.y / rc.x));
    }

    public static Vector3d toSphereVec(double x, double y, double z) {
        return toSphereVec(new Vector3d(x, y, z));
    }

    /**
     * 思路是：先将一个圆的所有点的集合传进来，这个集合中存放的点是使用的平面直角坐标系，然后，将这些点转为球坐标系表示，并执行球坐标上的天顶角和方位角旋转。
     *
     * @param circleDots    组成一个圆周的所有点的平面直角坐标
     * @param thetaRotation 天顶角的旋转度数，取值为 0 ~ π
     * @return 旋转之后该圆周的每个新的点的平面直角坐标
     */
    public static List<Vector3d> rotateCircle(List<Vector3d> circleDots, double thetaRotation, double phiRotation) {
        List<Vector3d> nextCircleDots = new ArrayList<>();
        for (Vector3d dotOnCircle : circleDots) {

            Vector3d prevSphereVec = toSphereVec(dotOnCircle);
            Vector3d nextSphereVec = new Vector3d(prevSphereVec.x, prevSphereVec.y + thetaRotation, prevSphereVec.z + phiRotation);
            nextCircleDots.add(toRectVec(nextSphereVec));
        }
        return nextCircleDots;
    }

    public static Vector3d rotateCircleDot(Vector3d rectVec, double thetaRotation, double phiRotation) {
        Vector3d sphereVec = toSphereVec(rectVec);
        return new Vector3d(sphereVec.x, sphereVec.y + thetaRotation, sphereVec.z + phiRotation);
    }


    public static Vector3d vecCeil(Vector3d vec) {
        return new Vector3d(
                Math.ceil(vec.getX()),
                Math.ceil(vec.getY()),
                Math.ceil(vec.getZ()));
    }

    public static Vector3d vecFloor(Vector3d vec) {
        return new Vector3d(
                Math.floor(vec.getX()),
                Math.floor(vec.getY()),
                Math.floor(vec.getZ()));
    }

    public static Vector2f getEulerAngle(Vector3d vectorA, Vector3d vectorB) {
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

        return new Vector2f((float) pitch, (float) yaw);
    }

    public static Rot2f getEulerAngle(Vector3d direction) {
        // 确保是单位向量
        Vector3d normalized = direction.normalize();
        double x = normalized.x;
        double y = normalized.y;
        double z = normalized.z;

        // 计算偏航角 (yaw)
        double yaw = Math.atan2(z, x);
        float yawDegrees = (float) Math.toDegrees(yaw);

        // 调整范围到 [0, 360)
        if (yawDegrees < 0) {
            yawDegrees += 360.0f;
        }

        // 计算俯仰角 (pitch)
        double pitch = Math.asin(-y);
        float pitchDegrees = (float) Math.toDegrees(pitch);

        return new Rot2f(yawDegrees, pitchDegrees);
    }

    public static EulerAngle getEulerAngleZYX(Quaternion quaternion) {
        float w = quaternion.getW(), x = quaternion.getX(), y = quaternion.getY(), z = quaternion.getZ();
        float yaw = (float) Math.atan2(2 * (w * z + x * y), 1 - 2 * (y * y + z * z));    // 绕Z轴
        float pitch = (float) Math.asin(2 * (w * y - z * x));                            // 绕Y轴
        float roll = (float) Math.atan2(2 * (w * x + y * z), 1 - 2 * (x * x + y * y));   // 绕X轴
        return new EulerAngle((float) Math.toDegrees(yaw), (float) Math.toDegrees(pitch), (float) Math.toDegrees(roll));
    }

    // 欧拉角 → 四元数（ZYX顺序）
    public static Quaternion toQuaternion(EulerAngle eulerAngle) {
        Quaternion qz = new Quaternion(Vector3f.ZP, eulerAngle.roll(), true);    // 先绕Z轴（Yaw）
        Quaternion qy = new Quaternion(Vector3f.YP, eulerAngle.yaw(), true);   // 再绕Y轴（Pitch）
        Quaternion qx = new Quaternion(Vector3f.XP, eulerAngle.pitch(), true);    // 后绕X轴（Roll）
        qz.multiply(qx);
        qz.multiply(qy); // 顺序: Z → X → Y
        return qz;
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

    public static Vector3d randomVec(double min, double max) {
        return new Vector3d(randomRange(min, max), randomRange(min, max), randomRange(min, max));
    }

    /**
     * Random Spherical Range Algorithm, returns a pair of3D coordinates on the given surface of3D a sphere.
     */
    public static Pair<Vector3d, Vector3d> rsr(Vector3d orientation, float yawRange, float pitchRange) {
        double yaw = toYawPitch(orientation).x;
        double pitch = toYawPitch(orientation).y;
        if (yaw < yawRange || yaw >= yawRange) {
            if (pitch > pitchRange) {
                return Pair.of(fromYawPitch(yawRange, pitchRange), fromYawPitch(yawRange, pitchRange).inverse());
            } else if (pitch < pitchRange) {
                return Pair.of(fromYawPitch(yawRange, pitchRange), fromYawPitch(yawRange, pitchRange).inverse());
            } else {
                return Pair.of(fromYawPitch(yawRange, (float) pitch), fromYawPitch(yawRange, (float) pitch).inverse());
            }
        } else {
            if (pitch > pitchRange) {
                return Pair.of(fromYawPitch(yawRange, pitchRange), fromYawPitch(yawRange, pitchRange).inverse());
            } else if (pitch < pitchRange) {
                return Pair.of(fromYawPitch(yawRange, pitchRange), fromYawPitch(yawRange, pitchRange).inverse());
            }
            return Pair.of(orientation, orientation.inverse());
        }
    }

    public static <V> V rollByWeight(int total, int weight, V value) {
        return new Random().nextInt(total) < weight ? null : value;
    }

    public static Vector2f toYawPitch(Vector3d vector3d) {
        double yaw = Math.atan2(-vector3d.x, vector3d.z);
        double pitch = Math.atan2(vector3d.y, Math.sqrt(vector3d.x * vector3d.x + vector3d.z * vector3d.z));
        return new Vector2f((float) toDegree(yaw), (float) toDegree(pitch));
    }

    public static Direction toDirection(Vector3d vector3d) {
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

    public static void rotateMatrixToLookVec(MatrixStack matrixStackIn, Vector3d lookVec) {
        Vector3d rotationVec = lookVec.inverse();
        float f5 = (float)Math.acos(rotationVec.y);
        float f6 = (float)Math.atan2(rotationVec.z, rotationVec.x);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(((float)Math.PI / 2 - f6) * (180 / (float)Math.PI)));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f5 * (180 / (float)Math.PI)));
    }

    public static Vector3d fromYawPitch(float yaw, float pitch) {
        return new Vector3d(Math.cos(yaw) * Math.cos(pitch), Math.sin(yaw) * Math.sin(pitch), Math.sin(pitch));
    }

    public static Vector3f fromYawPitch(Number yaw, float pitch) {
        return new Vector3f(
                (float) (Math.cos(yaw.floatValue()) * Math.cos(pitch)),
                (float) (Math.sin(yaw.floatValue()) * Math.sin(pitch)),
                (float) Math.sin(pitch));
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
        return base * base;
    }

    public static double pow3(double base) {
        return base * base * base;
    }

    public static float square(float base) {
        return base * base;
    }

    public static double cube(double base) {
        return base * base * base;
    }

    /**
     * Generate this code field by ChatGPT
     *
     * @param world 世界
     * @param block 将要被放置的方块
     * @param start 起始点
     * @param end   终点
     */
    public static void generateBlockOnBezierCurve(World world, Block block, BlockPos start, BlockPos end) {
        // 获取起点和终点之间的距离
        double dist = start.distanceSq(end);

        // 计算中点和控制点的坐标
        double mx = (start.getX() + end.getX()) / 2.0;
        double my = (start.getY() + end.getY()) / 2.0 + (dist / 16.0);
        double mz = (start.getZ() + end.getZ()) / 2.0;
        double cx = (start.getX() + end.getX() + my - start.getY()) / 2.0;
        double cz = (start.getZ() + end.getZ() + my - start.getY()) / 2.0;

        // 获取贝塞尔曲线上的坐标，并在每个坐标处放置草方块
        int numPoints = (int) Math.sqrt(dist) * 2;
        for (int i = 0; i <= numPoints; i++) {
            double t = (double) i / (double) numPoints;
            double x = Math.pow(1 - t, 2) * start.getX() + 2 * (1 - t) * t * cx + Math.pow(t, 2) * end.getX();
            double y = Math.pow(1 - t, 2) * start.getY() + 2 * (1 - t) * t * my + Math.pow(t, 2) * end.getY();
            double z = Math.pow(1 - t, 2) * start.getZ() + 2 * (1 - t) * t * cz + Math.pow(t, 2) * end.getZ();

            BlockPos pos = new BlockPos(x, y, z);
            if (world.getBlockState(pos).getBlock() == Blocks.AIR) {
                world.setBlockState(pos, block.getDefaultState());
            }
        }
    }

    public static ArrayList<Vector3d> getBlocksOnBezierCurve(BlockPos start, BlockPos end) {
        // 获取起点和终点之间的距离
        double dist = start.distanceSq(end);
        ArrayList<Vector3d> vecList = new ArrayList<>();

        // 计算中点和控制点的坐标
        double mx = (start.getX() + end.getX()) / 2.0;
        double my = (start.getY() + end.getY()) / 2.0 + (dist / 16.0);
        double mz = (start.getZ() + end.getZ()) / 2.0;
        double cx = (start.getX() + end.getX() + my - start.getY()) / 2.0;
        double cz = (start.getZ() + end.getZ() + my - start.getY()) / 2.0;

        // 获取贝塞尔曲线上的坐标，并在每个坐标处放置草方块
        int numPoints = (int) Math.sqrt(dist) * 2;
        for (int i = 0; i <= numPoints; i++) {
            double t = (double) i / (double) numPoints;
            double x = Math.pow(1 - t, 2) * start.getX() + 2 * (1 - t) * t * cx + Math.pow(t, 2) * end.getX();
            double y = Math.pow(1 - t, 2) * start.getY() + 2 * (1 - t) * t * my + Math.pow(t, 2) * end.getY();
            double z = Math.pow(1 - t, 2) * start.getZ() + 2 * (1 - t) * t * cz + Math.pow(t, 2) * end.getZ();

            vecList.add(new Vector3d(x, y, z));
        }
        return vecList;
    }

    public static Quaternion vecToQuaternion(Vector3f direction) {
        // 确保是单位向量
        Vector3f v = direction.copy();
        v.normalize();
        Vector3f ZP = new Vector3f(0,0,1);

        // 计算旋转轴
        Vector3f axis = ZP.copy();
        axis.cross(direction);

        // 如果向量平行（叉积接近零），使用默认四元数
        if (v3fLength(axis) * v3fLength(axis) < 1e-4) {
            // 相同方向
            if (ZP.dot(direction) > 0) {
                return Quaternion.ONE.copy();
            }
            // 相反方向，使用Y轴旋转180度
            return new Quaternion(new Vector3f(0, 1, 0), 180, true);
        }

        // 归一化旋转轴
        axis.normalize();

        // 计算旋转角度
        float cosTheta = ZP.dot(v);
        float theta = (float) Math.acos(MathHelper.clamp(cosTheta, -1.0, 1.0));

        // 计算四元数分量
        double halfTheta = theta / 2.0;
        double sinHalfTheta = Math.sin(halfTheta);

        return new Quaternion(
                (float)(axis.getX() * sinHalfTheta),
                (float)(axis.getY() * sinHalfTheta),
                (float)(axis.getZ() * sinHalfTheta),
                (float)Math.cos(halfTheta)
        );
    }

    public static float v3fLength(Vector3f vec) {
        return MathHelper.sqrt(vec.getX() * vec.getX() + vec.getY() * vec.getY() + vec.getZ() * vec.getZ());
    }

    public static Quaternion getRotationFrom(Vector3f firstVec, Vector3f targetVec) {
        // 归一化向量
        firstVec.normalize();
        targetVec.normalize();

        // 处理向量平行的情况
        double dot = firstVec.dot(targetVec);
        if (Math.abs(dot - 1.0) < 1e-6) {
            // 向量相同，无旋转
            return Quaternion.ONE.copy();
        }
        if (Math.abs(dot + 1.0) < 1e-6) {
            // 向量相反，旋转180度（任意垂直轴）
            Vector3f axis = findPerpendicularAxis(firstVec);
            return new Quaternion(axis, 180.0f, true);
        }

        // 计算旋转轴
        firstVec.cross(targetVec);
        firstVec.normalize();
        Vector3f axis = firstVec.copy();

        // 计算旋转角度
        double angle = Math.acos(dot);

        // 转换为半角
        double halfAngle = angle / 2.0;
        double sinHalf = Math.sin(halfAngle);
        double cosHalf = Math.cos(halfAngle);

        // 构造四元数
        return new Quaternion(
                (float)(axis.getX() * sinHalf),
                (float)(axis.getY() * sinHalf),
                (float)(axis.getZ() * sinHalf),
                (float)cosHalf
        );
    }

    /**
     * 找到与给定向量垂直的轴
     * @param vector 输入向量
     * @return 垂直向量
     */
    private static Vector3f findPerpendicularAxis(Vector3f vector) {
        // 尝试使用 (1,0,0) 叉乘
        Vector3f candidate = new Vector3f(1, 0, 0);
        vector.cross(candidate);

        float f = MathHelper.sqrt(vector.getX() * vector.getX() +
                vector.getY() * vector.getY() +
                vector.getZ() * vector.getZ());
        // 如果叉积太小，尝试其他轴
        if (f * f < 1e-5) {
            candidate = new Vector3f(0, 1, 0);
            vector.cross(candidate);
        }
        vector.normalize();
        return vector;
    }

    /**
     * 使用四元数旋转向量
     * @param quaternion 旋转四元数
     * @param vector 要旋转的向量
     * @return 旋转后的向量
     */
    public static Vector3d rotateVector(Quaternion quaternion, Vector3d vector) {
        // 提取四元数分量
        double qx = quaternion.getX();
        double qy = quaternion.getY();
        double qz = quaternion.getZ();
        double qw = quaternion.getW();

        // 提取向量分量
        double vx = vector.x;
        double vy = vector.y;
        double vz = vector.z;

        // 计算 q * v
        double ix = qw * vx + qy * vz - qz * vy;
        double iy = qw * vy + qz * vx - qx * vz;
        double iz = qw * vz + qx * vy - qy * vx;
        double iw = -qx * vx - qy * vy - qz * vz;

        // 计算 (q * v) * q⁻¹
        double rx = ix * qw + iw * -qx + iy * -qz - iz * -qy;
        double ry = iy * qw + iw * -qy + iz * -qx - ix * -qz;
        double rz = iz * qw + iw * -qz + ix * -qy - iy * -qx;

        return new Vector3d(rx, ry, rz);
    }

    public static Vector3f rotateVector(Quaternion quaternion, Vector3f vector) {
        // 提取四元数分量
        double qx = quaternion.getX();
        double qy = quaternion.getY();
        double qz = quaternion.getZ();
        double qw = quaternion.getW();

        // 提取向量分量
        float vx = vector.getX();
        float vy = vector.getY();
        float vz = vector.getZ();

        // 计算 q * v
        double ix = qw * vx + qy * vz - qz * vy;
        double iy = qw * vy + qz * vx - qx * vz;
        double iz = qw * vz + qx * vy - qy * vx;
        double iw = -qx * vx - qy * vy - qz * vz;

        // 计算 (q * v) * q⁻¹
        double rx = ix * qw + iw * -qx + iy * -qz - iz * -qy;
        double ry = iy * qw + iw * -qy + iz * -qx - ix * -qz;
        double rz = iz * qw + iw * -qz + ix * -qy - iy * -qx;

        return new Vector3f((float) rx, (float) ry, (float) rz);
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


    // 创建矩阵的副本
    private static String[][] copy(String[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        String[][] newMatrix = new String[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                newMatrix[i][j] = matrix[i][j];
            }
        }
        return newMatrix;
    }

    // 顺时针旋转90度
    private static String[][] rotate90(String[][] matrix, int m, int n) {
        String[][] newMatrix = new String[n][m]; // 新矩阵尺寸: n行×m列
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newMatrix[i][j] = matrix[m - 1 - j][i];
            }
        }
        return newMatrix;
    }

    // 旋转180度
    private static String[][] rotate180(String[][] matrix, int m, int n) {
        String[][] newMatrix = new String[m][n]; // 尺寸不变
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                newMatrix[i][j] = matrix[m - 1 - i][n - 1 - j];
            }
        }
        return newMatrix;
    }

    // 顺时针旋转270度（等效逆时针90度）
    private static String[][] rotate270(String[][] matrix, int m, int n) {
        String[][] newMatrix = new String[n][m]; // 新矩阵尺寸: n行×m列
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newMatrix[i][j] = matrix[j][n - 1 - i];
            }
        }
        return newMatrix;
    }

    public static float length(Vector3f vector3f){
        return MathHelper.sqrt(square(vector3f.getX()) + square(vector3f.getY()) + square(vector3f.getZ()));
    }

    public static Vector3d getMidPointOf(Vector3d prev, Vector3d next) {
        return new Vector3d((prev.x + next.x) / 2, (prev.y + next.y) / 2, (prev.z + next.z) / 2);
    }
}
