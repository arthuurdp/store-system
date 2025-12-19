package dao;

import db.DB;
import db.DbException;
import entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {
    private Connection conn = null;

    public ProductDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Product p) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO products (name, quantity, price) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, p.getName());
            st.setInt(2, p.getQuantity());
            st.setDouble(3, p.getPrice());

            int rows = st.executeUpdate();

            if (rows == 0) {
                throw new DbException("Unexpected error!");
            } else {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Product p) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "UPDATE products SET name = ?, quantity = ?, price = ? WHERE id = ?");

            st.setString(1, p.getName());
            st.setInt(2, p.getQuantity());
            st.setDouble(3, p.getPrice());
            st.setInt(4, p.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);

            st = conn.prepareStatement("DELETE FROM purchase_items WHERE product_id = ?");
            st.setInt(1, id);
            st.executeUpdate();
            DB.closeStatement(st);

            st = conn.prepareStatement("DELETE FROM products WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new DbException(ex.getMessage());
            }
            throw new DbException(e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
            DB.closeStatement(st);
        }
    }

    @Override
    public Product findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM products WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateProduct(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM products ORDER BY name");
            rs = st.executeQuery();

            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                list.add(instantiateProduct(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Product instantiateProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("quantity"),
                rs.getDouble("price")
        );
    }
}
