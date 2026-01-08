package io.github.olakouns.springdemov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(PiSpiProperties.class)
@SpringBootApplication
public class SpringDemoV3Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringDemoV3Application.class, args);
	}

}
