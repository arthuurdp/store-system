package application;

import services.StoreSystem;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        StoreSystem sistema = new StoreSystem(sc);

        while (true) {
            System.out.println(menu());
            System.out.print("Escolha uma opção: ");
            char opcao = sc.next().charAt(0);
            sc.nextLine();

            if (opcao == '1') {
                sistema.addProduct();
            }
            else if (opcao == '2') {
                sistema.excludeProduct();
            }
            else if (opcao == '3') {
                sistema.addPurchase();
            }
            else if (opcao == '4') {
                sistema.excludePurchase();
            }
            else if (opcao == '5') {
                sistema.changeProduct();
            }
            else if (opcao == '6') {
                sistema.changePurchase();
            }
            else if (opcao == '7') {
                sistema.listProducts();
            }
            else if (opcao == '8') {
                sistema.listPurchases();
            }
            else if (opcao == '9') {
                System.out.println("Saindo...");
                break;
            }
            else {
                System.out.println("Opção inválida!\n");
            }
        }
        sc.close();
    }

    private static String menu() {
        return """
                --- MENU ---
                1 - Add Product
                2 - Exclude Product
                3 - Add Purchase
                4 - Exclude Purchase
                5 - Change Product
                6 - Change Purchase
                7 - List Products
                8 - List Purchases
                9 - Exit Program
                """;
    }
}
