package pl.put.poznan.oculus.oculusimagesservice.rest

import org.springframework.beans.factory.annotation.Autowired
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
import org.springframework.web.util.UriComponentsBuilder
import pl.put.poznan.oculus.oculusimagesservice.config.PublicAPI
import pl.put.poznan.oculus.oculusimagesservice.model.ImageFile
import pl.put.poznan.oculus.oculusimagesservice.rest.ImageFilesController.Companion.PATH
import pl.put.poznan.oculus.oculusimagesservice.rest.request.ImageFileCreateRequest
import pl.put.poznan.oculus.oculusimagesservice.service.ImageFilesService

@PublicAPI
@RestController
@RequestMapping(PATH)
class ImageFilesController (
        @Autowired
        val service: ImageFilesService
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE], params = ["id"])
    fun getFileById(@RequestParam id: String): ResponseEntity<ImageFile> {
        val file = service.getFileById(id)
        return if (file != null) ResponseEntity.ok(file)
        else ResponseEntity
                .noContent()
                .build()
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE], params = ["patientId"])
    fun getFileByPatient(@RequestParam patientId: String): ResponseEntity<List<ImageFile>> {
        val file = service.getFileByPatient(patientId)
        return if (file.isNotEmpty()) ResponseEntity.ok(file)
        else ResponseEntity
                .noContent()
                .build()
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createFile(@RequestBody request: ImageFileCreateRequest): ResponseEntity<ImageFile> {
        val file = service.createFile(request.patient, request.author, request.notes)
        return ResponseEntity
                .created(file.getUri())
                .body(file)
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteImageFile(@RequestParam id: String) = service.deleteImageFile(id)

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