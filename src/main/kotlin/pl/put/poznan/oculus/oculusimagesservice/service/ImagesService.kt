package pl.put.poznan.oculus.oculusimagesservice.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.put.poznan.oculus.oculusimagesservice.model.Image
import pl.put.poznan.oculus.oculusimagesservice.repository.FileSystemRepository
import pl.put.poznan.oculus.oculusimagesservice.repository.ImageRepository

@Service
class ImagesService (
        private val imageRepository: ImageRepository,
        private val fileSystemRepository: FileSystemRepository
) {

    fun getImage(id: String) = imageRepository.findByIdOrNull(id)

    fun getImagesFromFile(fileId: String) = imageRepository.findAllByFileId(fileId)

    fun createImage(imageFileId: String, content: ByteArray) =
            imageRepository.save(Image(fileId = imageFileId)).let {
                imageRepository.save(Image(it.id, imageFileId, it.saveToFileSystem(imageFileId, content), it.date))
            }

    private fun Image.saveToFileSystem(imageFileId: String, content: ByteArray) =
        fileSystemRepository.saveImage(imageFileId, id!!, content)

    fun deleteImage(id: String) {
        getImage(id).let {
            fileSystemRepository.deleteImage(it!!.id!!, it.fileId)
            imageRepository.delete(it)
        }
    }

    fun deleteImagesFromFile(fileId: String) = getImagesFromFile(fileId).forEach { deleteImage(it.id!!) }
}
