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
        @ApiModelProperty(notes = "id of file")
        val id: String? = null,
        @ApiModelProperty(notes = "id of patient")
        val patient: String,
        @ApiModelProperty(notes = "id of doctor")
        val author: String,
        @ApiModelProperty(notes = "date of creation")
        val date: Instant = Instant.now(),
        @ApiModelProperty(notes = "notes")
        val notes: String = ""
)
