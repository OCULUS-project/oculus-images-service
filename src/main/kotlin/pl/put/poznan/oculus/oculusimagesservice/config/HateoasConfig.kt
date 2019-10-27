package pl.put.poznan.oculus.oculusimagesservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ForwardedHeaderFilter

@Configuration
class HateoasConfig {
    @Bean
    fun forwardedHeaderFilter() = ForwardedHeaderFilter()
}
