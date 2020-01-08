package pl.put.poznan.oculus.oculusimagesservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

@Configuration
class FileSystemConfig (
        @Value("\${img.root}")
        val imgRoot: String,
        @Value("\${img.path}")
        val imgPath: String
) : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
                .addResourceHandler("$imgPath/**")
                .addResourceLocations("file:$imgRoot/")
                .setCachePeriod(36000)
                .resourceChain(true)
                .addResolver(PathResourceResolver())
    }
}
