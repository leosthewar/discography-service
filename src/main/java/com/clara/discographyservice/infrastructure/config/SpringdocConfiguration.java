package com.clara.discographyservice.infrastructure.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SpringdocConfiguration {

    @Value("${application.version}")
    private String version;

    @Bean
    public OpenAPI litesampleOpenAPI() {
        return new OpenAPI().info(swaggerInfo()).externalDocs(swaggerExternalDoc());
    }

    private Info swaggerInfo() {
        return new Info()
                .title("Discography Service API")
                .description("Discography Service API")
                .version(version);
    }

    private ExternalDocumentation swaggerExternalDoc() {
        return new ExternalDocumentation().description("Project Documentation");
    }

    @Bean
    public GroupedOpenApi litesampleAllOpenAPI() {
        return GroupedOpenApi.builder().group("all").pathsToMatch("/v1/**").build();
    }
}
