package com.vrubayka.restapi_sample.DatabaseConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        // Check if the user table exists
        String checkTableExistenceSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'user'";
        int count = jdbcTemplate.queryForObject(checkTableExistenceSql, Integer.class);

        if (count == 0) {
            // Table does not exist, create the table
            createTable();
            // Now insert initial data
            insertInitialData();
        } else {
            // Table already exists, do nothing
            System.out.println("User table already exists, no data inserted.");
        }
    }

    private void createTable() {
        // Create the table if it does not exist
        String createTableSql = "CREATE TABLE \"user\" ("
                 + "id SERIAL PRIMARY KEY, "
                 + "name VARCHAR(255), "
                 + "email VARCHAR(255) UNIQUE NOT NULL"
                 + ");";


        jdbcTemplate.execute(createTableSql);
        System.out.println("User table created.");
    }

    private void insertInitialData() {
        // Insert default data only if the table was just created
        String insertDataSql = "INSERT INTO \"user\" (name, email) VALUES "
                 + "('Krosh', 'krosh@gmail.com'), "
                 + "('Ezhik', 'ezhik@hotmail.com'), "
                 + "('Barash', 'barash@rambler.ru'), "
                 + "('Njusha', 'njusha@yahoo.com');";

        
        // Execute insert
        jdbcTemplate.execute(insertDataSql);
        System.out.println("Initial data inserted into user table.");
    }
}
