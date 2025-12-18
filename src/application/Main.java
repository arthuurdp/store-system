package application;

import services.ProductService;
import services.PurchaseService;
import system.StoreSystem;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        ProductService productService = new ProductService();
        PurchaseService purchaseService = new PurchaseService();
        StoreSystem system = new StoreSystem(productService, purchaseService, sc);

        while (true) {
            System.out.println(menu());
            System.out.print("Choose an option: ");
            char option = sc.next().charAt(0);
            sc.nextLine();

            switch (option) {
                case '1' -> system.addProduct();
                case '2' -> system.excludeProduct();
                case '3' -> system.addPurchase();
                case '4' -> system.excludePurchase();
                case '5' -> system.changeProduct();
                case '6' -> system.changePurchase();
                case '7' -> system.listProducts();
                case '8' -> system.listPurchases();
                case '9' -> {
                    System.out.println("Bye bye...");
                    return;
                }
                default -> System.out.println("Invalid Option!\n");
            }
        }
    }

    private static String menu() {
        return """
                -=-=-=-=-=-=-=-=- MENU -=-=-=-=-=-=-=-=-=-=
                1 - Add Product
                2 - Exclude Product
                3 - Add Purchase
                4 - Exclude Purchase
                5 - Change Product
                6 - Change Purchase
                7 - List Products
                8 - List Purchases
                9 - Exit Program
                -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                """;
    }
}
