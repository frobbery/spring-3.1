package com.example.spring31;


import com.example.spring31.config.YamlPropertySourceFactory;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
