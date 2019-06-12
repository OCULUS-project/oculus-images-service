package pl.put.poznan.oculus.oculusimagesservice.rest

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import pl.put.poznan.oculus.oculusimagesservice.config.PublicAPI
import pl.put.poznan.oculus.oculusimagesservice.model.ImageFile
import pl.put.poznan.oculus.oculusimagesservice.rest.ImageFilesController.Companion.PATH
import pl.put.poznan.oculus.oculusimagesservice.rest.request.ImageFileCreateRequest
import pl.put.poznan.oculus.oculusimagesservice.service.ImageFilesService

@RestController
@RequestMapping(PATH)
@PublicAPI
@Api(value = "manage image files", description = "create, delete and retrieve files")
class ImageFilesController (
        val service: ImageFilesService
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE], params = ["id"])
    @ApiOperation(value = "retrieve image file by its id")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "image file exists"),
        ApiResponse(code = 204, message = "image file does not exist", response = Unit::class)
    ])
    fun getFileById(
            @RequestParam @ApiParam(value = "id of image file to retrieve", required = true) id: String
    ): ResponseEntity<ImageFile> {
        val file = service.getFileById(id)
        return if (file != null) ResponseEntity.ok(file)
        else ResponseEntity
                .noContent()
                .build()
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE], params = ["patientId"])
    @ApiOperation(value = "retrieve list of image files, that belong to specified patient")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "specified patient has image files"),
        ApiResponse(code = 204, message = "specified patient does not have image files", response = Unit::class)
    ])
    fun getFileByPatient(
            @RequestParam @ApiParam(value = "id of patient to look for", required = true) patientId: String
    ): ResponseEntity<List<ImageFile>> {
        val file = service.getFileByPatient(patientId)
        return if (file.isNotEmpty()) ResponseEntity.ok(file)
        else ResponseEntity
                .noContent()
                .build()
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "create new image file with empty image list")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "image file created successfully")
    ])
    fun createFile(
            @RequestBody @ApiParam(value = "desired image file data", required = true) request: ImageFileCreateRequest
    ): ResponseEntity<ImageFile> {
        val file = service.createFile(request.patient, request.author, request.notes)
        return ResponseEntity
                .created(file.getUri())
                .body(file)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "create new image file with empty image list")
    @ApiResponses(value = [
        ApiResponse(code = 204, message = "image file deleted successfully or did not exist")
    ])
    fun deleteImageFile(
            @RequestParam @ApiParam(value = "id of image file to delete", required = true) id: String
    ) = service.deleteImageFile(id)

    companion object {

        const val PATH = "img/files"

        private fun ImageFile.getUri()  = UriComponentsBuilder
                .newInstance()
                .path(PATH)
                .queryParam("id", id)
                .build()
                .toUri()
    }
}