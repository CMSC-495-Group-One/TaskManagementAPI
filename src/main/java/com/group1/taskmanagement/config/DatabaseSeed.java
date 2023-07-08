package com.group1.taskmanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseSeed implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final WebClient webClient;
    private final Environment env;

    public DatabaseSeed(JdbcTemplate jdbcTemplate, WebClient.Builder webClientBuilder, Environment env) {
        this.jdbcTemplate = jdbcTemplate;
        this.webClient = webClientBuilder.baseUrl(env.getProperty("app.base-url")).build();
        this.env = env;
    }

    public static void signUpUsers(JdbcTemplate jdbcTemplate, WebClient webClient) {
        String sql = "SELECT COUNT(*) FROM app_users WHERE username = ?";

        Integer count1 = jdbcTemplate.queryForObject(sql, new Object[]{"admin"}, Integer.class);
        if(count1 == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", "admin");
            map.put("password", "password");
            map.put("email", "admin@email.com");
            webClient.post()
                    .uri("/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(map))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }

        Integer count2 = jdbcTemplate.queryForObject(sql, new Object[]{"john"}, Integer.class);
        if(count2 == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", "john");
            map.put("password", "password");
            map.put("email", "john@email.com");
            webClient.post()
                    .uri("/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(map))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }
    }

    @Override
    public void run(String... args) {
        // Insert ADMIN and USER role
        jdbcTemplate.execute("INSERT INTO roles (name) SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN')");
        jdbcTemplate.execute("INSERT INTO roles (name) SELECT 'USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER')");

        // Sign two users up
        signUpUsers(jdbcTemplate, webClient);

        // Assign ADMIN to admin user
        String assignAdminRoleSql = "INSERT INTO user_roles (user_id, role_id) " +
                "SELECT app_users.id, roles.id " +
                "FROM app_users " +
                "JOIN roles ON roles.name = 'ADMIN' " +
                "LEFT JOIN user_roles ON user_roles.user_id = app_users.id " +
                "AND user_roles.role_id = roles.id " +
                "WHERE app_users.username = 'admin' AND user_roles.user_id IS NULL";
        jdbcTemplate.execute(assignAdminRoleSql);

        // Create two tasks and assign to admin and john
        String task1Sql = "INSERT INTO tasks (title, description, user_id) " +
                "SELECT 'Task 1', 'Description of Task 1', app_users.id " +
                "FROM app_users " +
                "LEFT JOIN tasks ON tasks.user_id = app_users.id AND tasks.title = 'Task 1' " +
                "WHERE app_users.username = 'admin' AND tasks.id IS NULL";
        jdbcTemplate.execute(task1Sql);
        String task2Sql = "INSERT INTO tasks (title, description, user_id) " +
                "SELECT 'Task 2', 'Description of Task 2', app_users.id " +
                "FROM app_users " +
                "LEFT JOIN tasks ON tasks.user_id = app_users.id AND tasks.title = 'Task 2' " +
                "WHERE app_users.username = 'john' AND tasks.id IS NULL";
        jdbcTemplate.execute(task2Sql);
    }
}


