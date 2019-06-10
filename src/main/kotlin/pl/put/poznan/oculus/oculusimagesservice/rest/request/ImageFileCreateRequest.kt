package pl.put.poznan.oculus.oculusimagesservice.rest.request

import java.time.Instant

data class ImageFileCreateRequest (
        val patient: String,
        val author: String,
        val date: Instant,
        val notes: String
)