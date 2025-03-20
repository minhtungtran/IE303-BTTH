public class Bai2 {
    public static void main(String[] args) {
        int soDiem = 1_000_000; 
        int diemTrongHinhTron = 0;

        for (int i = 0; i < soDiem; i++) {
            double x = Math.random() * 2 - 1; 
            double y = Math.random() * 2 - 1; 

            if (x * x + y * y <= 1) {
                diemTrongHinhTron++;
            }
        }

        double pi = 4.0 * diemTrongHinhTron / soDiem;
        System.out.println("Gia tri xap xi cua PI la: " + pi);
    }
}
