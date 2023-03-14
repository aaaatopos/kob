package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("pxzt"));
        System.out.println(passwordEncoder.matches("pxzt", "$2a$10$G1B1/SDUUsKzavaLcbd7kuxY57ZUwEi516wn9zAeJxXltnAczf0AC"));
    }

}
