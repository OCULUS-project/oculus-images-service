package pl.put.poznan.oculus.oculusimagesservice.model

import com.fasterxml.jackson.annotation.JsonFormat
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
        @ApiModelProperty(notes = "id of file")
        val fileId: String,
        @ApiModelProperty(notes = "path to image in the file system")
        val path: String = "",
        @ApiModelProperty(notes = "date of creation")
        val date: Instant = Instant.now(),
        @ApiModelProperty(notes = "list of scaled versions of the image")
        val scaled: List<ScaledImage> = emptyList(),
        @ApiModelProperty(notes = "notes")
        val notes: String = ""
)

data class ScaledImage (
        val path: String,
        val size: ScaledImageSize
)

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ScaledImageSize (
        val width: Int,
        val height: Int = width
) {
    S(64),
    M(300),
    L(640);

    @Suppress("unused")
    val type = name
}
