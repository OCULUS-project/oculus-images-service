package pl.put.poznan.oculus.oculusimagesservice.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.put.poznan.oculus.oculusimagesservice.model.ImageFile
import pl.put.poznan.oculus.oculusimagesservice.repository.FileSystemRepository
import pl.put.poznan.oculus.oculusimagesservice.repository.ImageFileRepository

@Service
class ImageFilesService (
        private val imageFileRepository: ImageFileRepository,
        private val fileSystemRepository: FileSystemRepository,
        private val imagesService: ImagesService
) {
    fun getFileById(id: String) = imageFileRepository.findByIdOrNull(id)

    fun getFileByPatient(patientId: String) = imageFileRepository.findAllByPatient(patientId)

    fun createFile(patientId: String, authorId: String, notes: String) = ImageFile(
            patient = patientId,
            author = authorId,
            notes = notes
    ).let { imageFileRepository.save(it).apply { saveToFileSystem() } }

    fun deleteImageFile(id: String) {
        val file = getFileById(id)!!
        deleteAllImagesOfFile(file)
        fileSystemRepository.deleteImageFile(id)
        imageFileRepository.delete(file)
    }

    private fun deleteAllImagesOfFile(file: ImageFile) = imagesService.deleteImagesFromFile(file.id!!)

    private fun ImageFile.saveToFileSystem() {
        fileSystemRepository.saveImageFile(id!!)
    }
}
