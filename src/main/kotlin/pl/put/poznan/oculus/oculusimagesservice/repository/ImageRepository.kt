package pl.put.poznan.oculus.oculusimagesservice.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pl.put.poznan.oculus.oculusimagesservice.model.Image
import pl.put.poznan.oculus.oculusimagesservice.model.ScaledImage

interface ImageRepository : MongoRepository<Image, String>, ImageRepositoryCustom {
    fun findAllByFileId(fileId: String): List<Image>
}

interface ImageRepositoryCustom {
    fun saveScaledImages(imageId: String, scaled: List<ScaledImage>): Image
}

@Repository
class ImageRepositoryCustomImpl (
        private val mongoTemplate: MongoTemplate
) : ImageRepositoryCustom {
    override fun saveScaledImages(imageId: String, scaled: List<ScaledImage>): Image {
        val query = Query().addCriteria(Criteria.where("_id").`is`(imageId))
        val update = Update().set("scaled", scaled)
        mongoTemplate.findAndModify(query, update, Image::class.java)!!
        return mongoTemplate.findById(imageId)!!
    }
}
