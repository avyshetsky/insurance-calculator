package com.scale.global.insurance.app.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Customer Insurance Service",
                version = "2.0",
                description = "REST API for insurance premium calculation and customer management",
                contact = @Contact(name = "Insurance Team", url = "http://www.global-scale.com/", email = "info@global-scale.com"),
                license = @License(name = "Apache License 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
        )
)
public class SwaggerConfig {
}
