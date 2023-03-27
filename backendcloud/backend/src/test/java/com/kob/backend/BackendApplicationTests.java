package com.kob.backend;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.pojo.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private RecordMapper recordMapper;

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("pxzt"));
        System.out.println(passwordEncoder.matches("pxzt", "$2a$10$G1B1/SDUUsKzavaLcbd7kuxY57ZUwEi516wn9zAeJxXltnAczf0AC"));
    }

    @Test
    void testRecord() {
        Record record = new Record(
                null,
                1,
                11,
                1,
                2,
                1,
                12,
                "00001",
                "22220",
                "11111111111111110000010000011000000000000110010000001001100000000000011010100000100111011000011011100100000101011000000000000110010000001001100000000000011000001000001111111111111111",
                "B",
                new Date()
        );
        recordMapper.insert(record);
    }

}
