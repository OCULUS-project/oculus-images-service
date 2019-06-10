package pl.put.poznan.oculus.oculusimagesservice.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class Image (
        @Id
        val id: String? = null,
        val path: String = "",
        val date: Instant,
        val notes: String = ""
) {
        constructor() : this(date=Instant.now())
}
