package github.thelawf.gensokyoontology.common.util.math;

import com.mojang.datafixers.util.Pair;
import github.thelawf.gensokyoontology.data.HermiteNodeInfo;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CurveUtil {
    /**
     * @param time    线性插值的步长，小于1，起点一般为0.001，用于曲线精度的测定
     * @param pointIn 定义的曲线起点和终点
     * @return 用于将直线绘制成曲线的二维节点
     */
    public static Point2D getSmoothSegment2d(double time, ArrayList<Point2D> pointIn) {
        Point2D[] points = new Point2D[pointIn.size()];
        for (int k = 0; k < pointIn.size(); k++)
            points[k] = pointIn.get(k);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4 - i - 1; j++) {
                double x = (1 - time) * points[j].getX() + time * points[j + 1].getX();
                double y = (1 - time) * points[j].getY() + time * points[j + 1].getY();
                points[j] = new Point2D.Double(x, y);
            }
        }
        return points[0];
    }

    @OnlyIn(Dist.CLIENT)
    public static List<Vector3d> getBezierPos(List<Vector3d> bezierPositions, Vector3d start, Vector3d end, Vector3d p, float time) {
        for (float i = 0; i < 1; i += time) {
            if (bezierPositions.size() > 1F / time) break;
            bezierPositions.add(GSKOMathUtil.bezier2(start, end, p, i));
        }
        return bezierPositions;
    }

    public static List<Vector3d> getBezierPos(List<Vector3d> bezierPositions, Vector3d start, Vector3d end, Vector3d ctrl1, Vector3d ctrl2, float time) {
        for (float i = 0; i < 1; i += time) {
            if (bezierPositions.size() > 1F / time) break;
            bezierPositions.add(GSKOMathUtil.bezier3(start, end, ctrl1, ctrl2, i));
        }
        return bezierPositions;
    }

    public static List<Vector3d> getBezierNormal(List<Vector3d> bezierPositions, Vector3d start, Vector3d end,  Vector3d ctrl1, Vector3d ctrl2, float time) {
        for (float i = 0; i < 1; i += time) {
            if (bezierPositions.size() > 1F / time) break;
            bezierPositions.add(GSKOMathUtil.bezier3Derivative(start, end, ctrl1, ctrl2, i));
        }
        return bezierPositions;
    }

    /**
     * 计算法线向量
     * @param tangent 切线向量
     * @return 法线向量
     */
    private static Vector3d calculateNormal(Vector3d tangent) {
        // 选择参考上向量（通常是世界向上或曲线平面法线）
        Vector3d upVector = new Vector3d(0, 1, 0);

        // 计算法线（垂直于切线和上向量）
        Vector3d binormal = tangent.crossProduct(upVector).normalize();

        // 如果法线太小（切线接近垂直），使用备用上向量
        if (binormal.lengthSquared() < 0.01) {
            upVector = new Vector3d(0, 0, 1);
            binormal = tangent.crossProduct(upVector).normalize();
        }

        return binormal;
    }

    public static List<Vector3d> getBezierNormal(List<Vector3d> bezierPositions, Vector3d start, Vector3d ctrl,  Vector3d end, float time) {
        for (float i = 0; i < 1; i += time) {
            if (bezierPositions.size() > 1F / time) break;
            bezierPositions.add(GSKOMathUtil.bezier2Derivative(start, ctrl, end, i));
        }
        return bezierPositions;
    }


    public static Pair<Vector3d, Vector3d> getParallelDotAt(Vector3d start, Vector3d end, Vector3d ctrl1, Vector3d ctrl2, int maxStep, int currentStep) {
        float unitStep = 1F / maxStep;
        if (currentStep > maxStep) currentStep = maxStep - 1;
        else if (currentStep < 0) currentStep = 0;

        // 1. 计算曲线上的点
        Vector3d curvePoint = GSKOMathUtil.bezier3(start, end, ctrl1, ctrl2, unitStep);

        // 2. 计算切线向量（曲线方向）
        Vector3d tangent = GSKOMathUtil.bezier3Derivative(start, end, ctrl1, ctrl2, unitStep).normalize();

        // 3. 计算法线向量（垂直于曲线）
        Vector3d normal = calculateNormal(tangent);

        // 4. 计算偏移点
        Vector3d leftRail = curvePoint.add(normal.scale(1.2F / 2));
        Vector3d rightRail = curvePoint.subtract(normal.scale(1.2F / 2));

        return Pair.of(leftRail, rightRail);
    }

    /**
     * 自动计算控制点
     * @param start 起点位置
     * @param end 终点位置
     * @param startRotation 起点旋转
     * @param endRotation 终点旋转
     * @return 控制点位置
     */
    public static Vector3d defaultCtrlDot(Vector3d start, Vector3d end,
                                          Quaternion startRotation, Quaternion endRotation) {
        // 计算中点
        Vector3d midPoint = start.add(end).scale(0.5);

        // 计算起点方向
        Vector3d startDirection = GSKOMathUtil.rotateVector(startRotation, new Vector3d(0, 0, 1));
        // 计算终点方向
        Vector3d endDirection = GSKOMathUtil.rotateVector(endRotation, new Vector3d(0, 0, 1));
        // 计算平均方向
        Vector3d avgDirection = startDirection.add(endDirection).normalize();

        // 计算偏移距离
        double distance = start.distanceTo(end);
        double offset = distance * 0.5;

        // 计算控制点
        return midPoint.add(avgDirection.scale(offset));
    }

    public static Vector3d catmullRom(CatmullRomSpline spline, float time) {
        return catmullRom(spline.ctrl1, spline.start, spline.end, spline.ctrl2, time);
    }
    public static Vector3d catmullRom(Vector3d ctrl1, Vector3d start, Vector3d end,  Vector3d ctrl2, float time) {
        double t2 = time * time;
        double t3 = t2 * time;

        return new Vector3d(
                0.5 * ((2 * start.x) + (-ctrl1.x + end.x) * time +
                        (2 * ctrl1.x - 5 * start.x + 4 * end.x - ctrl2.x) * t2 +
                        (-ctrl1.x + 3 * start.x - 3 * end.x + ctrl2.x) * t3),
                0.5 * ((2 * start.y) + (-ctrl1.y + end.y) * time +
                        (2 * ctrl1.y - 5 * start.y + 4 * end.y - ctrl2.y) * t2 +
                        (-ctrl1.y + 3 * start.y - 3 * end.y + ctrl2.y) * t3),
                0.5 * ((2 * start.z) + (-ctrl1.z + end.z) * time +
                        (2 * ctrl1.z - 5 * start.z + 4 * end.z - ctrl2.z) * t2 +
                        (-ctrl1.z + 3 * start.z - 3 * end.z + ctrl2.z) * t3)
        );
    }

    /**
     * 计算切线向量
     * @param t 参数 (0.0 - 1.0)
     * @return 切线向量
     */
    public static Vector3d catmullRomTangent(Vector3d start, Vector3d ctrl1, Vector3d end,  Vector3d ctrl2, double t) {
        Vector3d p0 = start;
        Vector3d p1 = ctrl1;
        Vector3d p2 = end;
        Vector3d p3 = ctrl2;

        double t2 = t * t;

        return new Vector3d(
                0.5 * ((-p0.x + p2.x) +
                        2 * (2 * p0.x - 5 * p1.x + 4 * p2.x - p3.x) * t +
                        3 * (-p0.x + 3 * p1.x - 3 * p2.x + p3.x) * t2),
                0.5 * ((-p0.y + p2.y) +
                        2 * (2 * p0.y - 5 * p1.y + 4 * p2.y - p3.y) * t +
                        3 * (-p0.y + 3 * p1.y - 3 * p2.y + p3.y) * t2),
                0.5 * ((-p0.z + p2.z) +
                        2 * (2 * p0.z - 5 * p1.z + 4 * p2.z - p3.z) * t +
                        3 * (-p0.z + 3 * p1.z - 3 * p2.z + p3.z) * t2)
        ).normalize();
    }

    public static Vector3d normal(Vector3d tangent) {
        Vector3d up = new Vector3d(0, 1, 0);
        Vector3d binormal = tangent.crossProduct(up).normalize();
        return binormal.crossProduct(tangent).normalize();
    }

    public static Vector3d catmullRomNormal(Vector3d start, Vector3d end, Vector3d ctrl1, Vector3d ctrl2, int index, int segments) {
        // 使用平均法线确保连续性
        List<Vector3d> tangents = tangents(start, end, ctrl1, ctrl2, segments);
        if (index == 0) {
            return tangents.get(0);
        }

        Vector3d prevNormal = CurveUtil.normal(tangents.get(index - 1));
        Vector3d currentNormal = CurveUtil.normal(tangents.get(index));

        return prevNormal.add(currentNormal).scale(0.5).normalize();
    }

    public static List<Vector3d> tangents(Vector3d start, Vector3d end,
                                          Vector3d ctrl1, Vector3d ctrl2,
                                          int segments) {
        List<Vector3d> tangents = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            float t = i / (float) segments;
            tangents.add(CurveUtil.catmullRomTangent(ctrl1, start, end, ctrl2, t).normalize());
        }
        return tangents;
    }
    public static List<Vector3d> curveDots(Vector3d start, Vector3d end,
                                           Vector3d ctrl1, Vector3d ctrl2,
                                           int segments) {
        List<Vector3d> points = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            float t = i / (float) segments;
            points.add(CurveUtil.catmullRom(ctrl1, start, end, ctrl2, t));
        }
        return points;
    }

    public static Vector3d catmulRomNormals(List<Vector3d> tangents, int index) {
        // 使用平均法线确保连续性
        if (index == 0) {
            return CurveUtil.normal(tangents.get(0));
        }

        Vector3d prevNormal = CurveUtil.normal(tangents.get(index - 1));
        Vector3d currentNormal = CurveUtil.normal(tangents.get(index));

        return prevNormal.add(currentNormal).scale(0.5).normalize();
    }

    public static Vector3d hermite3(Vector3d start, Vector3d end,
                                    Vector3f startTangent, Vector3f endTangent, float time) {
        double t2 = time * time;
        double t3 = t2 * time;

        double h1 = 2 * t3 - 3 * t2 + 1;
        double h2 = -2 * t3 + 3 * t2;
        double h3 = t3 - 2 * t2 + time;
        double h4 = t3 - t2;

        Vector3d startDirection = new Vector3d(startTangent);
        Vector3d endDirection = new Vector3d(endTangent);

        return start.scale(h1)
                .add(end.scale(h2))
                .add(startDirection.scale(h3))
                .add(endDirection.scale(h4));
    }

    /**
     * 计算埃尔米特三次曲线在参数t处的切线
     */
    public static Vector3d hermiteTangent(Vector3d start, Vector3d end,
                                          Vector3d startTangent, Vector3d endTangent,
                                          double t) {
        double t2 = t * t;

        double h1_deriv = 6 * t2 - 6 * t;
        double h2_deriv = -6 * t2 + 6 * t;
        double h3_deriv = 3 * t2 - 4 * t + 1;
        double h4_deriv = 3 * t2 - 2 * t;

        Vector3d tangent = start.scale(h1_deriv)
                .add(end.scale(h2_deriv))
                .add(startTangent.scale(h3_deriv))
                .add(endTangent.scale(h4_deriv));

        return tangent.normalize();
    }

    public static Vector3d hermiteNormal(Vector3d start, Vector3d end,
                                         Vector3d startTangent, Vector3d endTangent,
                                         double t) {
        // 计算切线
        Vector3d tangent = hermiteTangent(start, end, startTangent, endTangent, t);
        Vector3d upVector = new Vector3d(0, 1, 0);
        // 确保上向量与切线不平行
        if (Math.abs(tangent.dotProduct(upVector)) > 0.99) {
            upVector = new Vector3d(1, 0, 0);
            if (Math.abs(tangent.dotProduct(upVector)) > 0.99) {
                upVector = new Vector3d(0, 0, 1);
            }
        }

        // 计算副法线（切线 × 上向量）
        Vector3d binormal = tangent.crossProduct(upVector).normalize();
        // 计算法线（副法线 × 切线）
        return binormal.crossProduct(tangent).normalize();
    }

    public static Vector3d hermiteBinormal(Vector3d start, Vector3d end,
                                           Vector3d startTangent, Vector3d endTangent,
                                           double t){
        Vector3d tangent = hermiteTangent(start, end, startTangent, endTangent, t);
        Vector3d upVector = new Vector3d(0, 1, 0);
        // 确保上向量与切线不平行
        if (Math.abs(tangent.dotProduct(upVector)) > 0.99) {
            upVector = new Vector3d(1, 0, 0);
            if (Math.abs(tangent.dotProduct(upVector)) > 0.99) {
                upVector = new Vector3d(0, 0, 1);
            }
        }

        // 计算副法线（切线 × 上向量）
        return tangent.crossProduct(upVector).normalize();
    }

    public static Vector3d hermiteDerivative(Vector3d start, Vector3d end,
                                               Vector3d startTangent, Vector3d endTangent,
                                               double t) {
        double h1_deriv2 = 12 * t - 6;
        double h2_deriv2 = -12 * t + 6;
        double h3_deriv2 = 6 * t - 4;
        double h4_deriv2 = 6 * t - 2;

        return start.scale(h1_deriv2)
                .add(end.scale(h2_deriv2))
                .add(startTangent.scale(h3_deriv2))
                .add(endTangent.scale(h4_deriv2));
    }

    public static float hermiteLength(HermiteNodeInfo nodeInfo, float t0, float t1, int resolution){
        float totalLength = 0.0F;
        Vector3d prevPoint = hermite3(Vector3d.ZERO, nodeInfo.getEndOffset(), nodeInfo.prevOrientation(), nodeInfo.nextOrientation(),0);

        for (int i = 1; i <= resolution; i++) {
            float t = t0 + (t1 - t0) * i / resolution;
            Vector3d point = hermite3(Vector3d.ZERO, nodeInfo.getEndOffset(), nodeInfo.prevOrientation(), nodeInfo.nextOrientation(),t);
            totalLength += (float) point.subtract(prevPoint).length();
            prevPoint = point;
        }
        return totalLength;
    }

    /**
     * 根据弧长找到对应的t值（二分查找）
     */
    public static float hermiteProgress(HermiteNodeInfo nodeInfo, float arcLength, int samples) {
        float low = 0.0F;
        float high = 1.0F;
        float mid = 0.5F;

        for (int iteration = 0; iteration < 20; iteration++) {
            mid = (low + high) / 2.0F;
            float length = hermiteLength(nodeInfo, 0.0F, mid, samples);

            if (Math.abs(length - arcLength) < 0.001) {
                break;
            }

            if (length < arcLength) {
                low = mid;
            } else {
                high = mid;
            }
        }

        return mid;
    }

    /**
     * 计算埃尔米特曲线在参数t处的二阶导数（加速度）
     * @param start 起点位置
     * @param end 终点位置
     * @param startTangent 起点切线（一阶导数）
     * @param endTangent 终点切线（一阶导数）
     * @param t 参数值 [0,1]
     * @return 二阶导数向量（加速度）
     */
    public static Vector3d hermiteCurvature(Vector3d start, Vector3d end, Vector3d startTangent, Vector3d endTangent, double t) {
        double h1_2nd = 12 * t - 6;    // h₁''(t)
        double h2_2nd = 6 * t - 4;     // h₂''(t)
        double h3_2nd = -12 * t + 6;   // h₃''(t)
        double h4_2nd = 6 * t - 2;     // h₄''(t)

        return start.scale(h1_2nd)
                .add(startTangent.scale(h2_2nd))
                .add(end.scale(h3_2nd))
                .add(endTangent.scale(h4_2nd));
    }

    /**
     * 计算曲率半径（用于向心加速度计算）
     * @param firstDerivative 一阶导数（速度/切线）
     * @param secondDerivative 二阶导数（加速度）
     * @return 曲率半径
     */
    public static double calculateCurvatureRadius(Vector3d firstDerivative,
                                                  Vector3d secondDerivative) {
        double speed = firstDerivative.length(); // 速度大小

        if (speed < 1e-10) {
            return Double.MAX_VALUE; // 速度接近0时，曲率半径无限大
        }

        // 计算曲率：κ = |v × a| / |v|³
        Vector3d crossProduct = firstDerivative.crossProduct(secondDerivative);
        double curvature = crossProduct.length() / Math.pow(speed, 3);

        // 曲率半径：ρ = 1/κ
        return (curvature > 1e-10) ? 1.0 / curvature : Double.MAX_VALUE;
    }

    /**
     * 计算埃尔米特三次曲线在参数t处的法线
     */
    public static Vector3d hermiteNormalAdvanced(Vector3d start, Vector3d end, Vector3d startTangent, Vector3d endTangent,
                                                 Vector3d previousNormal, double t) {

        // 计算曲率向量（加速度在切线垂直方向的分量）
        Vector3d derivative = hermiteDerivative(start, end, startTangent, endTangent, t);
        Vector3d tangent = hermiteTangent(start, end, startTangent, endTangent, t);
        Vector3d curvatureVector = derivative.subtract(
                tangent.scale(derivative.dotProduct(tangent)));

        // 如果曲率太小，使用参考向量法
        if (curvatureVector.lengthSquared() < 1e-10) {
            return CurveUtil.normal(tangent);
        }
        // 计算基于曲率的法线
        Vector3d curvatureNormal = curvatureVector.normalize();

        // 如果曲率法线与前一个法线方向相反，翻转它
        if (previousNormal != null && curvatureNormal.dotProduct(previousNormal) < 0) {
            curvatureNormal = curvatureNormal.scale(-1);
        }

        // 如果曲率太小，使用参考向量法并保持连续性
        if (curvatureNormal.lengthSquared() < 1e-10) {
            curvatureNormal = CurveUtil.normal(tangent);
            if (previousNormal != null && curvatureNormal.dotProduct(previousNormal) < 0) {
                curvatureNormal = curvatureNormal.scale(-1);
            }
        }

        return curvatureNormal;
    }

}
