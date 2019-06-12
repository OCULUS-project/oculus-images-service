package pl.put.poznan.oculus.oculusimagesservice.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.put.poznan.oculus.oculusimagesservice.model.ImageFile
import pl.put.poznan.oculus.oculusimagesservice.repository.FileSystemRepository
import pl.put.poznan.oculus.oculusimagesservice.repository.ImageFileRepository
import pl.put.poznan.oculus.oculusimagesservice.repository.ImageRepository

@Service
class ImageFilesService (
        private val imageFileRepository: ImageFileRepository,
        private val fileSystemRepository: FileSystemRepository,
        private val imageRepository: ImageRepository
) {
    fun getFileById(id: String) = imageFileRepository.findByIdOrNull(id)

    fun getFileByPatient(patientId: String) = imageFileRepository.findAllByPatient(patientId)

    fun createFile(patientId: String, authorId: String, notes: String): ImageFile {
        val file = ImageFile(patientId, authorId, notes)
        return imageFileRepository.save(file)
                .also { it.saveToFileSystem() }
    }

    fun deleteImageFile(id: String) {
        val file = getFileById(id)!!
        deleteAllImagesOfFile(file)
        fileSystemRepository.deleteImageFile(id)
        imageFileRepository.delete(file)
    }

    private fun deleteAllImagesOfFile(file: ImageFile)  =
            file.images
                .map { imageRepository.findByIdOrNull(it)!! }
                .onEach { fileSystemRepository.deleteFileOrDirectory(it.path) }
                .also { imageRepository.deleteAll(it) }

    private fun ImageFile.saveToFileSystem() {
        fileSystemRepository.saveImageFile(id!!)
    }
}