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
public class UserSeeder implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final WebClient webClient;
    private final Environment env;

    public UserSeeder(JdbcTemplate jdbcTemplate, WebClient.Builder webClientBuilder, Environment env) {
        this.jdbcTemplate = jdbcTemplate;
        this.webClient = webClientBuilder.baseUrl(env.getProperty("app.base-url")).build();
        this.env = env;
    }

    @Override
    public void run(String... args) throws Exception {
        jdbcTemplate.execute("INSERT INTO roles (name) SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN')");
        jdbcTemplate.execute("INSERT INTO roles (name) SELECT 'USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER')");

        String sql1 = "SELECT COUNT(*) FROM app_users WHERE username = ?";
        Integer count1 = jdbcTemplate.queryForObject(sql1, new Object[]{"admin"}, Integer.class);
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

        String sql2 = "SELECT COUNT(*) FROM app_users WHERE username = ?";
        Integer count2 = jdbcTemplate.queryForObject(sql2, new Object[]{"john"}, Integer.class);
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
}


