import java.util.Scanner;

public class Bai1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhap ban kinh r: ");
        double r = scanner.nextDouble();

        int soDiemThu = 1_000_000;
        int demTrongHinhTron = 0;

        for (int i = 0; i < soDiemThu; i++) {
            double x = Math.random() * 2 * r - r; 
            double y = Math.random() * 2 * r - r;

            if (x * x + y * y <= r * r) {
                demTrongHinhTron++;
            }
        }

        double dienTichVuong = 4 * r * r;

        double dienTichXapXi = ((double) demTrongHinhTron / soDiemThu) * dienTichVuong;

        System.out.println("Xap xi dien tich hinh tron la: " + dienTichXapXi);

        scanner.close();
    }
}
