package services;

import entities.Purchase;
import entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StoreSystem {

    private final List<Product> listProducts = new ArrayList<>();
    private final List<Purchase> listPurchases = new ArrayList<>();
    private final Scanner sc;

    public StoreSystem(Scanner sc) {
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

        Product product = new Product(listProducts.size() + 1, name, quantity, price);
        listProducts.add(product);

        System.out.println("Product added successfully!\n");
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void excludeProduct() {
        if (listProducts.isEmpty()) {
            System.out.println("No product registered.\n");
            return;
        }

        listProducts();
        System.out.print("Enter product code that you want to exclude: ");
        int code = sc.nextInt();

        Product productToExclude = searchProductByCode(code);

        if (productToExclude != null) {
            listProducts.remove(productToExclude);
            System.out.println("Product excluded successfully!\n");
        } else {
            System.out.println("Invalid code!\n");
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void addPurchase() {
        Purchase purchase = new Purchase(listPurchases.size() + 1);

        if (listProducts.isEmpty()) {
            System.out.println("No available product to buy.\n");
            return;
        }

        char _continue;
        do {
            addProductInPurchase(purchase);

            System.out.print("Do you want to add another product? (y/n) ");
            _continue = sc.next().toLowerCase().charAt(0);
            sc.nextLine();
        } while (_continue == 'y');

        if (!purchase.getListProducts().isEmpty()) {
            listPurchases.add(purchase);
            System.out.println("Purchase " + purchase.getCode() + " registered successfully!\n");
        } else {
            System.out.println("Purchase canceled (no items added).\n");
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void excludePurchase() {
        if (listPurchases.isEmpty()) {
            System.out.println("No purchase registered.\n");
            return;
        }

        listPurchases();
        System.out.print("Enter the purchase code that you want to exclude: ");
        int code = sc.nextInt();

        Purchase purchaseToRemove = searchPurchaseByCode(code);

        if (purchaseToRemove != null) {
            for (Product p : purchaseToRemove.getListProducts()) {
                returnProductToStock(p, p.getQuantity());
            }

            listPurchases.remove(purchaseToRemove);
            System.out.println("Purchase has been excluded and products returned to stock!\n");
        } else {
            System.out.println("Invalid code.\n");
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void changeProduct() {
        if (listProducts.isEmpty()) {
            System.out.println("No products registered.\n");
            return;
        }

        listProducts();
        System.out.print("Enter the product code that you want to change: ");
        int code = sc.nextInt();
        sc.nextLine();

        Product product = searchProductByCode(code);
        if (product == null) {
            System.out.println("Invalid code!\n");
            return;
        }

        System.out.print("New name (or Enter to maintain): ");

        String name = sc.nextLine().trim();

        if (!name.isBlank()) {
            product.setName(name);
        }

        System.out.print("New quantity (or -1 to maintain): ");
        int quantity = sc.nextInt();
        if (quantity >= 0) {
            product.setQuantity(quantity);
        }

        System.out.print("New price (or -1 to maintain): ");
        double price = sc.nextDouble();
        sc.nextLine();
        if (price >= 0) {
            product.setPrice(price);
        }

        System.out.println("Product refreshed successfully!\n");
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void changePurchase() {
        if (listPurchases.isEmpty()) {
            System.out.println("No purchase registered.\n");
            return;
        }

        listPurchases();
        System.out.print("Enter the purchase code that you want to change: ");
        int code = sc.nextInt();

        Purchase purchase = searchPurchaseByCode(code);
        if (purchase == null) {
            System.out.println("Invalid code.\n");
            return;
        }

        System.out.println("Current purchase: ");
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
    public void listProducts() {
        if (listProducts.isEmpty()) {
            System.out.println("No product registered.\n");
        } else {
            listProducts.forEach(System.out::println);
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    public void listPurchases() {
        if (listPurchases.isEmpty()) {
            System.out.println("No purchase registered.\n");
        } else {
            listPurchases.forEach(System.out::println);
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    private void addProductInPurchase(Purchase purchase) {
        if (listProducts.isEmpty()) {
            System.out.println("No products available in stock.\n");
            return;
        }

        listProducts();
        System.out.print("Enter the product code to add: ");
        int productCode = sc.nextInt();

        Product stockProduct = searchProductByCode(productCode);

        if (stockProduct != null) {
            System.out.print("Enter the quantity you want to add: ");
            int quantity = sc.nextInt();

            if (quantity > stockProduct.getQuantity()) {
                System.out.println("Invalid quantity! Only " + stockProduct.getQuantity() + " units available.\n");
                return;
            } else if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.\n");
                return;
            }

            Product existingProduct = purchase.getProductByCode(stockProduct.getCode());

            if (existingProduct != null) {
                existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
            } else {
                Product purchasedProduct = new Product(
                        stockProduct.getCode(),
                        stockProduct.getName(),
                        quantity,
                        stockProduct.getPrice()
                );
                purchase.addProduct(purchasedProduct);
            }

            stockProduct.setQuantity(stockProduct.getQuantity() - quantity);
            if (stockProduct.getQuantity() == 0) {
                listProducts.remove(stockProduct);
                System.out.println("Product " + stockProduct.getName() + " is now out of stock and has been removed.\n");
            }

            System.out.println("Product added successfully to purchase!\n");

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

        Product productInPurchase = purchase.getProductByCode(productCode);

        if (productInPurchase != null) {
            System.out.print("Enter the quantity you want to remove: ");
            int quantity = sc.nextInt();

            if (quantity <= 0 || quantity > productInPurchase.getQuantity()) {
                System.out.println("Invalid quantity.\n");
                return;
            }

            purchase.removeProduct(productInPurchase, quantity);
            returnProductToStock(productInPurchase, quantity);

            System.out.println("Product removed successfully!\n");
        } else {
            System.out.println("Product not found in this purchase.\n");
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    private void returnProductToStock(Product product, int quantity) {
        Product stockProduct = searchProductByCode(product.getCode());
        if (stockProduct != null) {
            stockProduct.setQuantity(stockProduct.getQuantity() + quantity);
        } else {
            Product newProduct = new Product(
                    product.getCode(),
                    product.getName(),
                    quantity,
                    product.getPrice()
            );
            listProducts.add(newProduct);
        }
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    private Product searchProductByCode(int code) {
        for (Product p : listProducts) {
            if (p.getCode() == code) {
                return p;
            }
        }
        return null;
    }
    // ------------------------------------------------- //

    // ------------------------------------------------- //
    private Purchase searchPurchaseByCode(int code) {
        for (Purchase c : listPurchases) {
            if (c.getCode() == code) {
                return c;
            }
        }
        return null;
    }
    // ------------------------------------------------- //

}
