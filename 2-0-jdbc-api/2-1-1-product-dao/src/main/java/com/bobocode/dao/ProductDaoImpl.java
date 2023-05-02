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
            throw new DaoOperationException(e.getMessage());
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
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String producer = resultSet.getString("producer");
                BigDecimal price = resultSet.getBigDecimal("price");
                LocalDate date =
                        LocalDate.ofInstant(resultSet.getDate("expiration_date").toInstant(),
                                ZoneId.systemDefault());
                LocalDateTime time =
                        LocalDateTime.ofInstant(resultSet.getDate("creation_time").toInstant(),
                                ZoneId.systemDefault());
                Product product = new Product(id, name, producer, price, date, time);
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            throw new DaoOperationException("Can't find products");
        }
    }

    @Override
    public Product findOne(Long id) {
        throw new ExerciseNotCompletedException();// todo
    }

    @Override
    public void update(Product product) {
        throw new ExerciseNotCompletedException();// todo
    }

    @Override
    public void remove(Product product) {
        throw new ExerciseNotCompletedException();// todo
    }

}
