package pl.put.poznan.oculus.oculusimagesservice.rest.model

import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import pl.put.poznan.oculus.oculusimagesservice.rest.ImageFilesController
import pl.put.poznan.oculus.oculusimagesservice.rest.ImagesController

data class ImagesFromFileResponse(
        val imageFileId: String,
        val images: List<ImageModel>
) : RepresentationModel<ImagesFromFileResponse>() {
    init {
        add(linkTo(methodOn(ImagesController::class.java).getImagesFromFile(imageFileId)).withSelfRel())
        add(linkTo(methodOn(ImageFilesController::class.java).getFileById(imageFileId)).withRel(IanaLinkRelations.COLLECTION))
    }
}

data class PatientsImageFilesResponse(
        val patientId: String,
        val imageFiles: List<ImageFileModel>
) : RepresentationModel<PatientsImageFilesResponse>() {
    init {
        add(linkTo(methodOn(ImageFilesController::class.java).getFilesByPatient(patientId)).withSelfRel())
    }
}
