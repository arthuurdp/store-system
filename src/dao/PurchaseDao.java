package dao;

import entities.Purchase;
import java.util.List;

public interface PurchaseDao {
    void insert(Purchase obj);
    void update(Purchase obj);
    void deleteById(Integer id);
    Purchase findById(Integer id);
    List<Purchase> findAll();
}
