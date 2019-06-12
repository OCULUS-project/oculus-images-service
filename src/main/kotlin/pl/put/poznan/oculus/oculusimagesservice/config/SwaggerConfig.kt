package pl.put.poznan.oculus.oculusimagesservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/** Annotated controller is exposed to the public api */
@Target(AnnotationTarget.CLASS)
annotation class PublicAPI

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(PublicAPI::class.java))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(info())
    }

    companion object {
        private fun info(): ApiInfo {
            return ApiInfoBuilder().title("oculus-images-service")
                    .description("Stores and manages images in OCULUS")
                    .contact(Contact("Jakub Riegel", "www.cie.put.poznan.pl", "jakub.riegel@student.put.poznan.pl"))
                    .license("MIT")
                    .licenseUrl("#")
                    .version("1.0.0")
                    .build()
        }
    }
}