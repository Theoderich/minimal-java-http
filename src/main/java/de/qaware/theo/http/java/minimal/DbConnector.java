package de.qaware.theo.http.java.minimal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnector implements AutoCloseable {

    private final Connection conn;

    public DbConnector() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> result = new ArrayList<>();
        try (
                PreparedStatement getAllCustomersStatement = conn.prepareStatement("select * from customer");
                ResultSet resultSet = getAllCustomersStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                Customer customer = new Customer(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("age"));
                result.add(customer);
            }
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        conn.close();
    }
}
