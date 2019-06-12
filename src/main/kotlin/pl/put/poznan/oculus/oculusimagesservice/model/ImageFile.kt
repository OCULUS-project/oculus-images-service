package pl.put.poznan.oculus.oculusimagesservice.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@ApiModel(description = "details of image file")
data class ImageFile (
        @Id
        @ApiModelProperty(notes = "id of image")
        val id: String?,
        @ApiModelProperty(notes = "id of patient")
        val patient: String,
        @ApiModelProperty(notes = "id of doctor")
        val author: String,
        @ApiModelProperty(notes = "list of images ids")
        val images: List<String>,
        @ApiModelProperty(notes = "date of creation")
        val date: Instant,
        @ApiModelProperty(notes = "notes")
        val notes: String
) {
        constructor(patient: String, author: String, notes: String)
                : this(null, patient, author, emptyList(), Instant.now(), notes)
}