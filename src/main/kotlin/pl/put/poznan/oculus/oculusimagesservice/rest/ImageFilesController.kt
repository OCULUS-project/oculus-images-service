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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.oculus.boot.config.PublicAPI
import pl.put.poznan.oculus.oculusimagesservice.rest.model.ImageFileCreateRequest
import pl.put.poznan.oculus.oculusimagesservice.rest.model.ImageFileModel
import pl.put.poznan.oculus.oculusimagesservice.rest.model.PatientsImageFilesResponse
import pl.put.poznan.oculus.oculusimagesservice.rest.model.toModel
import pl.put.poznan.oculus.oculusimagesservice.service.ImageFilesService

@RestController
@RequestMapping("/img/files")
@PublicAPI
@Api(value = "manage image files", description = "Create, delete and retrieve files")
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
    ): ResponseEntity<ImageFileModel> {
        val file = service.getFileById(id)
        return if (file != null) ResponseEntity.ok(file.toModel())
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
    fun getFilesByPatient(
            @RequestParam @ApiParam(value = "id of patient to look for", required = true) patientId: String
    ): ResponseEntity<PatientsImageFilesResponse> {
        val files = service.getFileByPatient(patientId)
        return if (files.isNotEmpty()) {
            val response = PatientsImageFilesResponse(patientId, files.map { it.toModel() })
            ResponseEntity.ok(response)
        }
        else ResponseEntity
                .noContent()
                .build()
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE, "application/prs.hal-forms+json"], produces = [MediaType.APPLICATION_JSON_VALUE, "application/prs.hal-forms+json"])
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "create new image file with empty image list")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "image file created successfully")
    ])
    fun createFile(
            @RequestBody @ApiParam(value = "desired image file data", required = true) request: ImageFileCreateRequest
    ): ResponseEntity<ImageFileModel> {
        val file = service.createFile(request.patient, request.author, request.notes).toModel()
        return ResponseEntity
                .created(file.getRequiredLink("self").toUri())
                .body(file)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "create new image file with empty image list")
    @ApiResponses(value = [
        ApiResponse(code = 204, message = "image file deleted successfully or did not exist")
    ])
    fun deleteImageFile(
            @PathVariable @ApiParam(value = "id of image file to delete", required = true) id: String
    ): ResponseEntity<Unit> {
        service.deleteImageFile(id)
        return ResponseEntity
                .noContent()
                .build()
    }
}
