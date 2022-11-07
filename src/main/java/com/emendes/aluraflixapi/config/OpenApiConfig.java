package com.emendes.aluraflixapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    Contact contact = new Contact();
    contact.name("Edson Mendes").email("edson.luiz.mendes@hotmail.com").url("https://github.com/Edson-Mendes");

    return new OpenAPI()
        .info(new Info().title("Alura Flix API")
            .description("Alura Challenge Backend 5 ed")
            .version("0.2-semana2").contact(contact));
  }

}
