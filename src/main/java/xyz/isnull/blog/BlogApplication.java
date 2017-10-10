package xyz.isnull.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import xyz.isnull.blog.core.config.security.SecuritySettings;
import xyz.isnull.blog.core.repository.expand.ExpandJpaRepositoryFactoryBean;

@EnableCaching
@ComponentScan("xyz.isnull.blog")
@SpringBootApplication
@EnableConfigurationProperties(SecuritySettings.class)
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}
