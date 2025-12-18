package services;

import dao.DaoFactory;
import dao.ProductDao;
import entities.Product;

import java.util.List;

public class ProductService {

    private final ProductDao productDao = DaoFactory.createProductDao();

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findById(Integer id) {
        return productDao.findById(id);
    }

    public void insert(Product obj) {
        productDao.insert(obj);
    }

    public void update(Product obj) {
        productDao.update(obj);
    }

    public boolean deleteById(Integer id) {
        Product p = productDao.findById(id);
        if (p != null) {
            productDao.deleteById(id);
            return true;
        }
        return false;
    }
}
