import java.util.*;

class Menu {
    String name;
    double price;
    String category;

    public Menu(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
}

public class Main {
    private static Menu[] menuItems = new Menu[]{
        new Menu("Nasi Goreng", 25000, "makanan"),
        new Menu("Mie Goreng", 20000, "makanan"),
        new Menu("Kentang Goreng", 15000, "makanan"),
        new Menu("Ayam Penyet", 30000, "makanan"),
        new Menu("Kopi", 10000, "minuman"),
        new Menu("Teh Manis", 5000, "minuman"),
        new Menu("Es Jeruk", 8000, "minuman"),
        new Menu("Jus Buah", 12000, "minuman")
    };

    public static void main(String[] args) {
        System.out.println("Selamat datang di Restoran Kami!");
        tampilkanMenu();
        Map<String, Integer> pesanan = terimaPesanan();
        double[] totalBiayaData = hitungTotalBiaya(pesanan);
        cetakStruk(pesanan, totalBiayaData);
    }

    private static void tampilkanMenu() {
        System.out.println("Daftar Menu:");
        for (Menu item : menuItems) {
            System.out.printf("%s (Rp %.2f) - %s\n", item.name, item.price, item.category);
        }
        System.out.println();
    }

    private static Map<String, Integer> terimaPesanan() {
        try (Scanner scanner = new Scanner(System.in)) {
            Map<String, Integer> pesanan = new HashMap<>();

            System.out.println("Masukkan pesanan Anda, maksimal 4 item (ketik 'selesai' untuk mengakhiri):");
            for (int i = 0; i < 4; i++) {
                System.out.print("Nama Menu dan Jumlah: ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("selesai")) {
                    break;
                }
                String[] parts = input.split("=");
                if (parts.length == 2) {
                    String menuName = parts[0].trim();
                    try {
                        int quantity = Integer.parseInt(parts[1].trim());
                        if (isMenuValid(menuName)) {
                            pesanan.put(menuName, quantity);
                        } else {
                            System.out.println("Menu tidak ditemukan. Silakan coba lagi.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Jumlah tidak valid. Harap masukkan angka yang benar.");
                        i--; // Mengulangi input jika terjadi kesalahan
                    }
                } else {
                    System.out.println("Format tidak valid. Harap masukkan dengan format: NamaMenu = Jumlah");
                    i--; // Mengulangi input jika format tidak valid
                }
            }

            return pesanan;
        }
    }

    private static double[] hitungTotalBiaya(Map<String, Integer> pesanan) {
        double totalBiaya = 0;
        double tax;
        double serviceCharge = 20000; // Biaya pelayanan tetap
        double discount = 0; // Diskon total
        boolean eligibleForFreeDrink = false; // Penawaran beli satu gratis satu

        for (String item : pesanan.keySet()) {
            totalBiaya += getMenuPrice(item) * pesanan.get(item);
        }

        // Mengecek kelayakan diskon
        if (totalBiaya > 100000) {
            discount = totalBiaya * 0.1;  // Diskon 10%
        }

        // Mengecek penawaran beli satu gratis satu jika total lebih dari Rp 50.000
        if (totalBiaya > 50000) {
            for (String item : pesanan.keySet()) {
                if (getMenuCategory(item).equals("minuman")) {
                    if (pesanan.get(item) > 0) {
                        eligibleForFreeDrink = true;
                        break;
                    }
                }
            }
        }

        // Menghitung pajak
        tax = (totalBiaya - discount) * 0.1;

        // Mengembalikan total biaya, diskon dan pajak sebagai array
        return new double[]{totalBiaya - discount + tax + serviceCharge, discount, tax, eligibleForFreeDrink ? serviceCharge : 0};
    }

    private static double getMenuPrice(String menuName) {
        for (Menu item : menuItems) {
            if (item.name.equalsIgnoreCase(menuName)) {
                return item.price;
            }
        }
        return 0; // Jika tidak ditemukan
    }

    private static String getMenuCategory(String menuName) {
        for (Menu item : menuItems) {
            if (item.name.equalsIgnoreCase(menuName)) {
                return item.category;
            }
        }
        return "";
    }

    private static void cetakStruk(Map<String, Integer> pesanan, double[] totalBiayaData) {
        System.out.println("\n** STRUK PEMESANAN **");
        System.out.println("----------------------------------------");

        // Menampilkan item pesanan
        for (String item : pesanan.keySet()) {
            double price = getMenuPrice(item);
            int quantity = pesanan.get(item);
            double itemTotal = price * quantity;
            System.out.printf("%-20s x %d = Rp. %-10.2f\n", item, quantity, itemTotal);
        }

        double totalBiaya = totalBiayaData[0];
        double discount = totalBiayaData[1];
        double tax = totalBiayaData[2];
        double serviceCharge = totalBiayaData[3];

        if (discount > 0) {
            System.out.printf("Diskon (10%%): Rp. %-10.2f\n", discount);
        }
        if (serviceCharge > 0) {
            System.out.printf("Biaya Pelayanan: Rp. %-10.2f\n", serviceCharge);
        }
        System.out.printf("Pajak (10%%): Rp. %-10.2f\n", tax);
        System.out.printf("----------------------------------------\n");
        System.out.printf("Total Biaya: Rp. %-10.2f\n", totalBiaya);
        System.out.println("Terima kasih telah berkunjung ke Restoran Kami!");
    }

    private static boolean isMenuValid(String menuName) {
        for (Menu item : menuItems) {
            if (item.name.equalsIgnoreCase(menuName)) {
                return true;
            }
        }
        return false;
    }
}
