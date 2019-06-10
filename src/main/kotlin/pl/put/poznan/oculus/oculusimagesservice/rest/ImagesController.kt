package pl.put.poznan.oculus.oculusimagesservice.rest

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import pl.put.poznan.oculus.oculusimagesservice.config.PublicAPI
import pl.put.poznan.oculus.oculusimagesservice.service.ImagesService
import java.net.URI

@PublicAPI
@RestController
@RequestMapping("img")
class ImagesController (
        val service: ImagesService
) {
    @GetMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getImagePath(@PathVariable id: String): ResponseEntity<Unit> {
        val path = service.getImagePath(id)
        return if (path.isNotBlank()) ResponseEntity
                .ok()
                .location(URI(path))
                .build()
        else ResponseEntity
                .noContent()
                .build()
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addImage(
            @RequestParam imageFileId: String,
            @RequestParam content: MultipartFile
    ): ResponseEntity<Unit> {
        val created = service.createImage(imageFileId, content.bytes)
        return ResponseEntity
                .created(URI(created.path))
                .build()
    }
}
