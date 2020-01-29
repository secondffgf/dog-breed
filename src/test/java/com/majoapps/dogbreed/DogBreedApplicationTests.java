package com.majoapps.dogbreed;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class DogBreedApplicationTests {
	
	@Test
	void contextLoads() {
	}
	
	@TestConfiguration
	public static class S3Configuration {
		@Bean
		public AmazonS3 s3client() {
			final String serviceEndpoint = String.format("http://%s:%s", "127.0.0.1", "8081");
            return AmazonS3Client.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, "us-west-2"))
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
		}
	}
}