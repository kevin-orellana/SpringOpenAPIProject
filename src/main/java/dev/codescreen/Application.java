package dev.codescreen;

import dev.codescreen.Utils.BootstrapUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/*
* Main application entry point
* */
@SpringBootApplication
public class Application {

  /**
   * Application main method.
   */
  public static void main(String[] args) {
    if (System.getenv("DevMode") != null){
      BootstrapUtil.bootstrapDev();
    }
    BootstrapUtil.mainSetup();

    SpringApplication.run(Application.class, args);
  }

  /*
  * Display available Spring Beans.
  * */
  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      String[] beanNames = ctx.getBeanDefinitionNames();
      for (String beanName : beanNames) { System.out.println("Bean: " + beanName); }
    };
  }
}
