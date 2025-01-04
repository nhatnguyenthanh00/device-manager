package com.example.server;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
//		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//		String secretString = Base64.getEncoder().encodeToString(key.getEncoded());
//		System.out.println(secretString);
	}

}
