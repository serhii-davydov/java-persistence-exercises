package com.bobocode.dao;

import com.bobocode.exception.DaoOperationException;
import com.bobocode.model.Product;
import com.bobocode.util.ExerciseNotCompletedException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class ProductDaoImpl implements ProductDao {

    private final DataSource dataSource;

    public ProductDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Product product) {
        try (Connection connection = dataSource.getConnection()) {
            String query =
                    "INSERT INTO products(name, producer, price, expiration_date)"
                            + " VALUES(?,?,?,?);";
            PreparedStatement statement =
                    connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getName());
            statement.setString(2, product.getProducer());
            statement.setBigDecimal(3, product.getPrice());
            statement.setDate(4, Date.valueOf(product.getExpirationDate()));
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                product.setId(generatedKeys.getLong(1));
            }
        } catch (Exception e) {
            throw new DaoOperationException("Error saving product: " + product.toString());
        }
    }

    @Override
    public List<Product> findAll() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM products";
            ResultSet resultSet = statement.executeQuery(query);
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Long productId = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String producer = resultSet.getString("producer");
                BigDecimal price = resultSet.getBigDecimal("price");
                LocalDate date = resultSet.getDate("expiration_date").toLocalDate();
                LocalDateTime time =resultSet.getTimestamp("creation_time").toLocalDateTime();
                Product product = new Product(productId, name, producer, price, date, time);
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            throw new DaoOperationException("Can't find products");
        }
    }

    @Override
    public Product findOne(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM products WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = null;
            if (resultSet.next()) {
                Long productId = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String producer = resultSet.getString("producer");
                BigDecimal price = resultSet.getBigDecimal("price");
                LocalDate date = resultSet.getDate("expiration_date").toLocalDate();
                LocalDateTime time =resultSet.getTimestamp("creation_time").toLocalDateTime();
                product = new Product(productId, name, producer, price, date, time);
                return product;
            } else {
                throw new DaoOperationException("Can't find product by id: " + id);
            }
        } catch (Exception e) {
            throw new DaoOperationException("Can't find product by id: " + id);
        }
    }

    @Override
    public void update(Product product) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "UPDATE products SET name = ?, producer = ?, price = ?, expiration_date = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, product.getName());
            statement.setString(2, product.getProducer());
            statement.setBigDecimal(3, product.getPrice());
            statement.setDate(4, Date.valueOf(product.getExpirationDate()));
            statement.setLong(5, product.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new DaoOperationException("Can't update product: " + product.toString());
        }
    }

    @Override
    public void remove(Product product) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "DELETE FROM products WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, product.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new DaoOperationException("Can't remove product: " + product.toString());
        }
    }

}
