package pl.put.poznan.oculus.oculusimagesservice.rest

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.annotations.ResponseHeader
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pl.put.poznan.oculus.oculusimagesservice.config.PublicAPI
import pl.put.poznan.oculus.oculusimagesservice.model.Image
import pl.put.poznan.oculus.oculusimagesservice.service.ImagesService
import java.net.URI

@RestController
@RequestMapping("img")
@PublicAPI
@Api(value = "manage individual images", description = "Create and retrieve images")
class ImagesController (
        val service: ImagesService
) {

    @GetMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "retrieve single image data including static path")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "image exists", responseHeaders = [ResponseHeader(name = "Location", description = "static path to given image", response = String::class)]),
        ApiResponse(code = 204, message = "image does not exist", response = Unit::class    )
    ])
    fun getImage(
            @PathVariable @ApiParam(value = "id of image to retrieve", required = true) id: String
    ): ResponseEntity<Image> {
        val image = service.getImage(id)
        return if (image != null) ResponseEntity
                .ok()
                .location(URI(image.path))
                .body(image)
        else ResponseEntity
                .noContent()
                .build()
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "create new image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "image created", responseHeaders = [ResponseHeader(name = "Location", description = "static path to given image", response = String::class)])
    ])
    fun addImage(
            @RequestParam @ApiParam(value = "id image file to hold new image", required = true) imageFileId: String,
            @RequestParam @ApiParam(value = "image in .jpg", required = true) content: MultipartFile
    ): ResponseEntity<Image> {
        val created = service.createImage(imageFileId, content.bytes)
        return ResponseEntity
                .created(URI(created.path))
                .body(created)
    }
}
