package com.demo.configserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
		"encrypt.key=unit-test-encrypt-key",
		"spring.cloud.config.server.encrypt.enabled=true",
		"spring.cloud.config.server.git.clone-on-start=false"
})
class ConfigEncryptionTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	TextEncryptor textEncryptor;

	@Test
	void encryptAndDecryptEndpointsRoundTrip() throws Exception {
		String cipher = mockMvc.perform(post("/encrypt")
						.contentType(MediaType.TEXT_PLAIN)
						.content("ecommerce"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		assertThat(cipher).isNotBlank().isNotEqualTo("ecommerce");

		mockMvc.perform(post("/decrypt")
						.contentType(MediaType.TEXT_PLAIN)
						.content(cipher))
				.andExpect(status().isOk())
				.andExpect(content().string("ecommerce"));
	}

	@Test
	void textEncryptorDecryptsCipherValues() {
		String cipher = textEncryptor.encrypt("mongo-password");
		assertThat(textEncryptor.decrypt(cipher)).isEqualTo("mongo-password");
	}
}
