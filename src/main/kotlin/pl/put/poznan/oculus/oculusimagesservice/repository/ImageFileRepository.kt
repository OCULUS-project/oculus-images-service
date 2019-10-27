package pl.put.poznan.oculus.oculusimagesservice.repository

import org.springframework.data.mongodb.repository.MongoRepository
import pl.put.poznan.oculus.oculusimagesservice.model.ImageFile

interface ImageFileRepository : MongoRepository<ImageFile, String> {
    fun findAllByPatient(patientId: String): List<ImageFile>
}
