package pl.put.poznan.oculus.oculusimagesservice.repository

import org.springframework.data.mongodb.repository.MongoRepository
import pl.put.poznan.oculus.oculusimagesservice.model.Image

interface ImageRepository : MongoRepository<Image, String> {
    fun findAllByFileId(fileId: String): List<Image>
}
