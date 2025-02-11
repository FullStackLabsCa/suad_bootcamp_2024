package io.reactivestax.active_life_canada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
//@EnableCaching
//@EnableRedisHttpSession
public class ActiveLifeCanadaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActiveLifeCanadaApplication.class, args);
	}

}
