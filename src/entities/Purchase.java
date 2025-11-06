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
        sb.append("-------------------------\n");
        sb.append("Purchase: ").append(code).append("\n");
        sb.append("Products: \n");

        if (listProducts.isEmpty()) {
            sb.append("No product has been added.\n");
        } else {
            for (Product p : listProducts) {
                sb.append("- ").append(p.toString()).append("\n");
            }
        }

        sb.append("\n");
        sb.append(String.format("Purchase total: R$ %.2f%n", total()));
        sb.append(String.format("Total with ICMS: R$ %.2f", calculateIcms()));
        sb.append("\n");

        return sb.toString();
    }

}
