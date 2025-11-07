package entities;

import java.util.ArrayList;
import java.util.List;

public class Purchase {
    private Integer code;
    private final List<Product> listProducts = new ArrayList<>();

    public Purchase(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public List<Product> getListProducts() {
        return listProducts;
    }

    public void addProduct(Product product) {
        listProducts.add(product);
    }

    public void removeProduct(Product product, int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity.\n");
            return;
        }

        if (!listProducts.contains(product)) {
            System.out.println("Product not found in this purchase.\n");
            return;
        }

        if (product.getQuantity() < quantity) {
            System.out.println("You are trying to remove more than purchased.\n");
            return;
        }

        product.setQuantity(product.getQuantity() - quantity);

        if (product.getQuantity() == 0) {
            listProducts.remove(product);
        }
    }

    public Product getProductByCode(int code) {
        for (Product p : listProducts) {
            if (p.getCode() == code) {
                return p;
            }
        }
        return null;
    }

    public double total() {
        double sum = 0.0;
        for (Product p : listProducts) {
            sum += p.getPrice() * p.getQuantity();
        }
        return sum;
    }

    public double calculateIcms() {
        return total() * 1.17;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n" + "-=-=-=-=-=-=-=-=- PURCHASE DETAILS -=-=-=-=-=-=-=-=-=-=").append("\n");
        sb.append(String.format("Purchase Code: %s%n", code));

        if (listProducts.isEmpty()) {
            sb.append("\nNo products have been added.\n");
        } else {
            sb.append("------------------- PRODUCTS ----------------------\n");
            for (Product p : listProducts) {
                sb.append(p).append("\n");
            }
            sb.append("---------------------------------------------------\n");
        }

        sb.append(String.format("Purchase Total    : R$ %.2f%n", total()));
        sb.append(String.format("Total with ICMS   : R$ %.2f%n", calculateIcms()));
        sb.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=").append("\n");

        return sb.toString();

    }
}
