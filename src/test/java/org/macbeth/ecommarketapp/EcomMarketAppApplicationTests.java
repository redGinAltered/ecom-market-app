package org.macbeth.ecommarketapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.macbeth.ecommarketapp.controller.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@SpringBootTest
class EcomMarketAppApplicationTests {

	@Autowired
	MainController controller;

	@Autowired
	private WebApplicationContext wac;

	@Value("${rate-limit-connection}")
	String connectionLimit;

	@Value("${rate-limit-period-min}")
	String period;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		this.mockMvc = webAppContextSetup (this.wac).build();
	}

	@Execution(ExecutionMode.CONCURRENT)
	@RepeatedTest(20)
	void testDifferentAddress() throws Exception {
		Random r = new Random();
		String address = String.format("%s.%s.%s.%s", r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256)) ;

		mockMvc.perform(get("/").with(remoteAddr(address)))
				.andExpect(status().isOk());
	}

	@Test
	void testSameAddress() throws Exception {
		String address = "127.32.1.24";
		int limit = Integer.parseInt(connectionLimit);
		int sleepMins = Integer.parseInt(period) * 65;

		for (int i = 1 ; i < limit ; i++) {
			mockMvc.perform(get("/").with(remoteAddr(address)))
					.andExpect(status().isOk());
		}
		mockMvc.perform(get("/").with(remoteAddr(address)))
				.andExpect(status().isBadGateway());

		Thread.sleep(sleepMins * 1000L);

		mockMvc.perform(get("/").with(remoteAddr(address)))
				.andExpect(status().isOk());
	}

	private static RequestPostProcessor remoteAddr(final String remoteAddr) {
		return request -> {
			request.setRemoteAddr(remoteAddr);
			return request;
		};
	}

}
