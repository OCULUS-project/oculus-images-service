package pl.put.poznan.oculus.oculusimagesservice.rest.model

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import pl.put.poznan.oculus.oculusimagesservice.model.Image
import pl.put.poznan.oculus.oculusimagesservice.model.ImageFile
import pl.put.poznan.oculus.oculusimagesservice.rest.ImageFilesController
import pl.put.poznan.oculus.oculusimagesservice.rest.ImagesController

class ImageModel(content: Image) : EntityModel<Image>(content)

fun Image.toModel() = ImageModel(this).apply {
    add(linkTo(methodOn(ImagesController::class.java).getImage(id!!)).withSelfRel())
    add(linkTo(ImagesController::class.java).slash("static").slash(path.substringAfter("/img")).withRel(IanaLinkRelations.CANONICAL))
    add(linkTo(methodOn(ImageFilesController::class.java).getFileById(fileId)).withRel(IanaLinkRelations.COLLECTION))
    add(scaled.map {
        linkTo(ImagesController::class.java)
                .slash("static")
                .slash(it.path.substringAfter("/img"))
                .withRel("${IanaLinkRelations.CANONICAL}Scaled${it.size}")
    })
}

class ImageFileModel(content: ImageFile) : EntityModel<ImageFile>(content)

fun ImageFile.toModel() = ImageFileModel(this).apply {
    add(linkTo(methodOn(ImageFilesController::class.java).getFileById(id!!)).withSelfRel())
    add(linkTo(methodOn(ImageFilesController::class.java).getFilesByPatient(patient)).withRel("patientsFiles"))
    add(linkTo(methodOn(ImagesController::class.java).getImagesFromFile(id)).withRel(IanaLinkRelations.CONTENTS))
    add(linkTo(ImagesController::class.java).slash(id).withRel("addImage"))
    add(linkTo(methodOn(ImageFilesController::class.java).deleteImageFile(id)).withRel(IanaLinkRelations.EDIT))
}
