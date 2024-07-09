package com.example.agro_commerce.DAO;

import com.example.agro_commerce.model.Stock;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockDAOImpl implements StockDAO {
    private final String jdbcURL;
    private final String jdbcUsername;
    private final String jdbcPassword;
    private Connection jdbcConnection;

    public StockDAOImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("DRIVE_CLASS_NAME");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    @Override
    public boolean insertStock(Stock stock) throws SQLException {
        String sql = "INSERT INTO stock (product_id, seller_id, amount) VALUES (?, ?, ?)";
        boolean rowInserted = false;

        connect();

        try (PreparedStatement statement = jdbcConnection.prepareStatement(sql)) {
            statement.setInt(1, stock.getProductId());
            statement.setInt(2, stock.getSellerId());
            statement.setInt(3, stock.getAmount());

            rowInserted = statement.executeUpdate() > 0;
        } finally {
            disconnect();
        }

        return rowInserted;
    }

    @Override
    public List<Stock> listAllStocks() throws SQLException {
        List<Stock> listStocks = new ArrayList<>();
        String sql = "SELECT * FROM stock";

        connect();

        try (Statement statement = jdbcConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int stockId = resultSet.getInt("stock_id");
                int productId = resultSet.getInt("product_id");
                int sellerId = resultSet.getInt("seller_id");
                int amount = resultSet.getInt("amount");

                Stock stock = new Stock(stockId, productId, sellerId, amount);
                listStocks.add(stock);
            }
        } finally {
            disconnect();
        }

        return listStocks;
    }

    @Override
    public boolean updateStock(Stock stock) throws SQLException {
        String sql = "UPDATE stock SET product_id = ?, seller_id = ?, amount = ? WHERE stock_id = ?";
        boolean rowUpdated = false;

        connect();

        try (PreparedStatement statement = jdbcConnection.prepareStatement(sql)) {
            statement.setInt(1, stock.getProductId());
            statement.setInt(2, stock.getSellerId());
            statement.setInt(3, stock.getAmount());
            statement.setInt(4, stock.getStockId());

            rowUpdated = statement.executeUpdate() > 0;
        } finally {
            disconnect();
        }

        return rowUpdated;
    }

    @Override
    public boolean deleteStock(Stock stock) throws SQLException {
        String sql = "DELETE FROM stock WHERE stock_id = ?";
        boolean rowDeleted = false;

        connect();

        try (PreparedStatement statement = jdbcConnection.prepareStatement(sql)) {
            statement.setInt(1, stock.getStockId());

            rowDeleted = statement.executeUpdate() > 0;
        } finally {
            disconnect();
        }

        return rowDeleted;
    }

    @Override
    public Stock getStock(int stockId) throws SQLException {
        Stock stock = null;
        String sql = "SELECT * FROM stock WHERE stock_id = ?";

        connect();

        try (PreparedStatement statement = jdbcConnection.prepareStatement(sql)) {
            statement.setInt(1, stockId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int productId = resultSet.getInt("product_id");
                    int sellerId = resultSet.getInt("seller_id");
                    int amount = resultSet.getInt("amount");

                    stock = new Stock(stockId, productId, sellerId, amount);
                }
            }
        } finally {
            disconnect();
        }

        return stock;
    }
}
