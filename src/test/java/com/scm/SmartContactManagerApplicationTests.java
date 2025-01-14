package com.scm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.services.EmailService;

@SpringBootTest
class SmartContactManagerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService service;
	@Test
	void sendEmailTest(){
		service.sendEmail("monishgiri46@gmail.com","Just testing email","This is scm project working on email service");
	}

}
