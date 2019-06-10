package pl.put.poznan.oculus.oculusimagesservice.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class ImageFile (
        @Id
        val id: String?,
        val patient: String,
        val author: String,
        val images: List<String>,
        val date: Instant,
        val notes: String
) {
        constructor(patient: String, author: String, notes: String)
                : this(null, patient, author, emptyList(), Instant.now(), notes)
}