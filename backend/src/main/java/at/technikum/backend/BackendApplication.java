package at.technikum.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EntityScan(basePackages = { "at.technikum.common.DAL.models" })
@EnableJpaRepositories(basePackages = { "at.technikum.backend" })
public class BackendApplication {

  public static void main(final String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }

}
