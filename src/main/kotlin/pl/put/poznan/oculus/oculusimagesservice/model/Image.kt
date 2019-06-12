package pl.put.poznan.oculus.oculusimagesservice.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
@ApiModel(description = "details of image")
data class Image (
        @Id
        @ApiModelProperty(notes = "id of image")
        val id: String? = null,
        @ApiModelProperty(notes = "path to image in the file system")
        val path: String = "",
        @ApiModelProperty(notes = "date of creation")
        val date: Instant,
        @ApiModelProperty(notes = "notes")
        val notes: String = ""
) {
        constructor() : this(date=Instant.now())
}
