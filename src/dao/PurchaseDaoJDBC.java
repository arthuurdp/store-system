package dao;

import db.DB;
import db.DbException;
import entities.Product;
import entities.Purchase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDaoJDBC implements PurchaseDao {
    private Connection conn;

    public PurchaseDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Purchase p) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);

            st = conn.prepareStatement(
                    "INSERT INTO purchases (total_price, total_with_icms) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setDouble(1, p.total());
            st.setDouble(2, p.calculateIcms());

            int rows = st.executeUpdate();

            if (rows == 0) {
                conn.rollback();
                throw new DbException("Unexpected error!");
            } else {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                    insertItems(p.getId(), p.getListProducts());
                }
                conn.commit();
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new DbException(e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Purchase p) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);

            st = conn.prepareStatement(
                    "UPDATE purchases SET total_price = ?, total_with_icms = ? WHERE id = ?");

            st.setDouble(1, p.total());
            st.setDouble(2, p.calculateIcms());
            st.setInt(3, p.getId());
            st.executeUpdate();

            PreparedStatement stDelete = null;
            try {
                stDelete = conn.prepareStatement("DELETE FROM purchase_items WHERE purchase_id = ?");
                stDelete.setInt(1, p.getId());
                stDelete.executeUpdate();
            } finally {
                DB.closeStatement(stDelete);
            }

            insertItems(p.getId(), p.getListProducts());

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new DbException(e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM purchases WHERE id = ?");
            st.setInt(1, id);

            int rows = st.executeUpdate();

            if (rows == 0) {
                throw new DbException("Purchase not found!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Purchase findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT p.*, pi.product_id, pi.quantity as item_quantity, pi.unit_price, prod.name as product_name " +
                            "FROM purchases p " +
                            "JOIN purchase_items pi ON p.id = pi.purchase_id " +
                            "JOIN products prod ON pi.product_id = prod.id " +
                            "WHERE p.id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();

            Purchase purchase = null;
            while (rs.next()) {
                if (purchase == null) {
                    purchase = new Purchase(rs.getInt("id"));
                }
                Product prod = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("item_quantity"),
                        rs.getDouble("unit_price")
                );
                purchase.addProduct(prod);
            }
            return purchase;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Purchase> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT p.*, pi.product_id, pi.quantity as item_quantity, pi.unit_price, prod.name as product_name " +
                            "FROM purchases p " +
                            "LEFT JOIN purchase_items pi ON p.id = pi.purchase_id " +
                            "LEFT JOIN products prod ON pi.product_id = prod.id " +
                            "ORDER BY p.id DESC");
            rs = st.executeQuery();

            List<Purchase> list = new ArrayList<>();
            java.util.Map<Integer, Purchase> map = new java.util.HashMap<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                Purchase p = map.get(id);

                if (p == null) {
                    p = new Purchase(id);
                    map.put(id, p);
                    list.add(p);
                }

                if (rs.getObject("product_id") != null) {
                    Product prod = new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("item_quantity"),
                            rs.getDouble("unit_price")
                    );
                    p.addProduct(prod);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private void insertItems(int purchaseId, List<Product> products) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO purchase_items (purchase_id, product_id, quantity, unit_price) " +
                            "VALUES (?, ?, ?, ?)");

            for (Product p : products) {
                st.setInt(1, purchaseId);
                st.setInt(2, p.getId());
                st.setInt(3, p.getQuantity());
                st.setDouble(4, p.getPrice());
                st.addBatch();
            }
            st.executeBatch();
        } finally {
            DB.closeStatement(st);
        }
    }
}
