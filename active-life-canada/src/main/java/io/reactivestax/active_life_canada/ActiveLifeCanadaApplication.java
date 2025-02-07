package io.reactivestax.active_life_canada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ActiveLifeCanadaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActiveLifeCanadaApplication.class, args);
	}

}
