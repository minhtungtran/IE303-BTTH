import java.util.*;

// Lớp đại diện cho một điểm trong mặt phẳng
class Point implements Comparable<Point> {
    int x, y;

    public Point(Scanner scanner) {
        this.x = scanner.nextInt();
        this.y = scanner.nextInt();
    }

    @Override
    public int compareTo(Point other) {
        if (this.x == other.x) {
            return Integer.compare(this.y, other.y);
        }
        return Integer.compare(this.x, other.x);
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}

// Lớp đại diện cho vector
class Vector {
    int x, y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int crossProduct(Vector other) {
        return this.x * other.y - this.y * other.x;
    }

    public static Vector fromPoints(Point a, Point b) {
        return new Vector(b.x - a.x, b.y - a.y);
    }
}

// Lớp thực hiện thuật toán tìm bao lồi
class ConvexHull {
    public static int orientation(Point a, Point b, Point c) {
        Vector ab = Vector.fromPoints(a, b);
        Vector bc = Vector.fromPoints(b, c);
        return ab.crossProduct(bc);
    }

    public static boolean clockwise(Point a, Point b, Point c, boolean allowColinear) {
        int orient = orientation(a, b, c);
        return orient < 0 || (allowColinear && orient == 0);
    }

    public static boolean counterClockwise(Point a, Point b, Point c, boolean allowColinear) {
        int orient = orientation(a, b, c);
        return orient > 0 || (allowColinear && orient == 0);
    }

    public static List<Point> getConvexHull(List<Point> points, boolean allowColinear) {
        int n = points.size();
        if (n < 3) return new ArrayList<>(points);

        Collections.sort(points);
        Point first = points.get(0);
        Point last = points.get(n - 1);

        List<Point> upper = new ArrayList<>();
        List<Point> lower = new ArrayList<>();
        upper.add(first);
        lower.add(first);

        for (int i = 1; i < n; i++) {
            Point p = points.get(i);

            if (i == n - 1 || clockwise(first, p, last, allowColinear)) {
                while (upper.size() >= 2 && !clockwise(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p, allowColinear)) {
                    upper.remove(upper.size() - 1);
                }
                upper.add(p);
            }

            if (i == n - 1 || counterClockwise(first, p, last, allowColinear)) {
                while (lower.size() >= 2 && !counterClockwise(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p, allowColinear)) {
                    lower.remove(lower.size() - 1);
                }
                lower.add(p);
            }
        }

        List<Point> hull = new ArrayList<>(upper);
        for (int i = lower.size() - 2; i > 0; i--) {
            hull.add(lower.get(i));
        }

        return hull;
    }
}

// Lớp chính
public class Bai3 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            List<Point> points = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                points.add(new Point(scanner));
            }

            List<Point> convexHull = ConvexHull.getConvexHull(points, false);

            for (Point point : convexHull) {
                System.out.println(point);
            }
        }
    }
}
