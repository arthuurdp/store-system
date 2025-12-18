package system;

import entities.Purchase;
import entities.Product;
import services.ProductService;
import services.PurchaseService;

import java.util.List;
import java.util.Scanner;

public class StoreSystem {

    private final ProductService productService;
    private final PurchaseService purchaseService;
    private final Scanner sc;

    public StoreSystem(ProductService productService, PurchaseService purchaseService, Scanner sc) {
        this.productService = productService;
        this.purchaseService = purchaseService;
        this.sc = sc;
    }

    // ------------------------------------------------- //
    public void addProduct() {
        System.out.print("Enter product´s name: ");
        String name = sc.nextLine();

        System.out.print("Enter the quantity: ");
        int quantity = sc.nextInt();

        System.out.print("Enter the price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        Product product = new Product(null, name, quantity, price);
        productService.insert(product);

        System.out.println("Product added successfully to database!\n");
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void excludeProduct() {
        
        productService.findAll().forEach(System.out::println);
        System.out.print("Enter product id that you want to exclude: ");
        int id = sc.nextInt();

        if (productService.deleteById(id)) {
            System.out.println("Product excluded successfully from database!\n");
        } else {
            System.out.println("Product not found.\n");
        }

    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void addPurchase() {
        Purchase purchase = new Purchase(null);

        char _continue;
        do {
            addProductInPurchase(purchase);

            System.out.print("Do you want to add another product? (y/n) ");
            _continue = sc.next().toLowerCase().charAt(0);
            sc.nextLine();
        } while (_continue == 'y');

        if (!purchase.getListProducts().isEmpty()) {
            purchaseService.insert(purchase);
            System.out.println("Purchase registered successfully!\n");
        } else {
            System.out.println("Purchase canceled (no items added).\n");
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void excludePurchase() {
        List<Purchase> purchases = purchaseService.findAll();
        if (purchases.isEmpty()) {
            System.out.println("No purchase registered.\n");
            return;
        }

        purchases.forEach(System.out::println);
        System.out.print("Enter the purchase code that you want to exclude: ");
        int id = sc.nextInt();

        if (purchaseService.deleteById(id)) {
            System.out.println("Purchase has been excluded and products returned to stock!\n");
        } else {
            System.out.println("Invalid code.\n");
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void changeProduct() {
        
        productService.findAll().forEach(System.out::println);
        System.out.print("Enter the product code that you want to change: ");
        int id = sc.nextInt();
        sc.nextLine();

        Product p = productService.findById(id);

        if (p != null) {
            System.out.print("New name (or Enter to maintain): ");

            String name = sc.nextLine().trim();

            if (!name.isBlank()) {
                p.setName(name);
            }

            System.out.print("New quantity (or -1 to maintain): ");
            int quantity = sc.nextInt();
            if (quantity >= 0) {
                p.setQuantity(quantity);
            }

            System.out.print("New price (or -1 to maintain): ");
            double price = sc.nextDouble();
            sc.nextLine();
            if (price >= 0) {
                p.setPrice(price);
            }
            
            productService.update(p);
            System.out.println("Product refreshed successfully!\n");
        } else {
            System.out.println("Product not found.\n");
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void changePurchase() {
        List<Purchase> purchases = purchaseService.findAll();
        if (purchases.isEmpty()) {
            System.out.println("No purchase registered.\n");
            return;
        }

        purchases.forEach(System.out::println);
        System.out.print("Enter the purchase code that you want to change: ");
        int code = sc.nextInt();

        Purchase purchase = purchaseService.findById(code);
        if (purchase == null) {
            System.out.println("Invalid code.\n");
            return;
        }

        System.out.println(purchase);

        System.out.println("What do you want to do?");
        System.out.println("1 - Add Product");
        System.out.println("2 - Exclude Product");
        System.out.print("Option: ");
        int option = sc.nextInt();

        if (option == 1) {
            addProductInPurchase(purchase);
        } else if (option == 2) {
            removeProductFromPurchase(purchase);
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    private void addProductInPurchase(Purchase purchase) {
        
        productService.findAll().forEach(System.out::println);
        System.out.print("Enter the product code to add: ");
        int id = sc.nextInt();

        Product stockProduct = productService.findById(id);

        if (stockProduct != null) {
            System.out.print("Enter the quantity you want to add: ");
            int quantity = sc.nextInt();

            try {
                purchaseService.addProductToPurchase(purchase, stockProduct, quantity);
                System.out.println("Product added successfully to purchase!\n");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + "\n");
            }

        } else {
            System.out.println("Product not found.\n");
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    private void removeProductFromPurchase(Purchase purchase) {
        if (purchase.getListProducts().isEmpty()) {
            System.out.println("This purchase has no products.\n");
            return;
        }

        System.out.println("Products in this purchase:");
        purchase.getListProducts().forEach(System.out::println);

        System.out.print("Enter the product code to remove: ");
        int productCode = sc.nextInt();

        Product productInPurchase = purchase.getProductById(productCode);

        if (productInPurchase != null) {
            System.out.print("Enter the quantity you want to remove: ");
            int quantity = sc.nextInt();

            try {
                purchaseService.removeProductFromPurchase(purchase, productInPurchase, quantity);
                System.out.println("Product removed successfully!\n");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + "\n");
            }
        } else {
            System.out.println("Product not found in this purchase.\n");
        }
    }
    // ------------------------------------------------- //

    public void listProducts() {
        productService.findAll().forEach(System.out::println);
    }

    public void listPurchases() {
        purchaseService.findAll().forEach(System.out::println);
    }
}
