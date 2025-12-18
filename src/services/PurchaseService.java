package services;

import dao.DaoFactory;
import dao.ProductDao;
import dao.PurchaseDao;
import entities.Product;
import entities.Purchase;

import java.util.List;

public class PurchaseService {

    private final PurchaseDao purchaseDao = DaoFactory.createPurchaseDao();
    private final ProductDao productDao = DaoFactory.createProductDao();

    public List<Purchase> findAll() {
        return purchaseDao.findAll();
    }

    public Purchase findById(Integer id) {
        return purchaseDao.findById(id);
    }

    public void insert(Purchase obj) {
        purchaseDao.insert(obj);
    }

    public void update(Purchase obj) {
        purchaseDao.update(obj);
    }

    public boolean deleteById(Integer id) {
        Purchase purchase = purchaseDao.findById(id);
        if (purchase != null) {
            for (Product p : purchase.getListProducts()) {
                returnProductToStock(p, p.getQuantity());
            }
            purchaseDao.deleteById(id);
            return true;
        }
        return false;
    }

    public void addProductToPurchase(Purchase purchase, Product stockProduct, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (quantity > stockProduct.getQuantity()) {
            throw new IllegalArgumentException("Invalid quantity! Only " + stockProduct.getQuantity() + " units available.");
        }

        Product existingProduct = purchase.getProductById(stockProduct.getId());

        if (existingProduct != null) {
            existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
        } else {
            Product purchasedProduct = new Product(
                    stockProduct.getId(),
                    stockProduct.getName(),
                    quantity,
                    stockProduct.getPrice()
            );
            purchase.addProduct(purchasedProduct);
        }

        stockProduct.setQuantity(stockProduct.getQuantity() - quantity);
        productDao.update(stockProduct);

        if (purchase.getId() != null) {
            purchaseDao.update(purchase);
        }
    }

    public void removeProductFromPurchase(Purchase purchase, Product productInPurchase, int quantity) {
        if (quantity <= 0 || (productInPurchase != null && quantity > productInPurchase.getQuantity())) {
            throw new IllegalArgumentException("Invalid quantity.");
        }

        purchase.removeProduct(productInPurchase, quantity);
        returnProductToStock(productInPurchase, quantity);

        if (purchase.getId() != null) {
            purchaseDao.update(purchase);
        }
    }

    private void returnProductToStock(Product product, int quantity) {
        Product stockProduct = productDao.findById(product.getId());
        if (stockProduct != null) {
            stockProduct.setQuantity(stockProduct.getQuantity() + quantity);
            productDao.update(stockProduct);
        } else {
            // If for some reason the product was deleted from the store, we re-insert it
            Product newProduct = new Product(product.getId(), product.getName(), quantity, product.getPrice());
            productDao.insert(newProduct);
        }
    }
}
