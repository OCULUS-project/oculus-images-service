package pl.put.poznan.oculus.oculusimagesservice.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.updateFirst
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pl.put.poznan.oculus.oculusimagesservice.model.ImageFile


interface ImageFileRepository : MongoRepository<ImageFile, String>, ImageFileRepositoryCustom {
    fun findAllByPatient(patientId: String): List<ImageFile>
}

interface ImageFileRepositoryCustom {
    fun addNewImageToFile(imageId: String, fileId: String)
    fun removeImageFromFile(imageId: String, fileId: String)
}

@Repository
class ImageFileRepositoryCustomImpl (
        val mongoTemplate: MongoTemplate
) : ImageFileRepositoryCustom {

    override fun addNewImageToFile(imageId: String, fileId: String) {
        val update = Update().addToSet("images", imageId)
        mongoTemplate.updateFirst(queryById(fileId), update, ImageFile::class)
    }

    override fun removeImageFromFile(imageId: String, fileId: String) {
        val update = Update().pull("images", imageId)
        mongoTemplate.updateFirst(queryById(fileId), update, ImageFile::class)
    }

    companion object {
        fun queryById(fileId: String) = Query(Criteria.where("id").`is`(fileId))
    }

}


