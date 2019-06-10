package pl.put.poznan.oculus.oculusimagesservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.put.poznan.oculus.oculusimagesservice.model.Image
import pl.put.poznan.oculus.oculusimagesservice.repository.FileSystemRepository
import pl.put.poznan.oculus.oculusimagesservice.repository.ImageFileRepository
import pl.put.poznan.oculus.oculusimagesservice.repository.ImageRepository

@Service
class ImagesService (
        private val imageRepository: ImageRepository,
        private val imageFileRepository: ImageFileRepository,
        private val fileSystemRepository: FileSystemRepository,
        @Value("\${img.path}")
        val imgPath: String
) {

    fun getImagePath(id: String): String {
        val image = imageRepository.findByIdOrNull(id)!!
        return getImagePath(image)
    }

    private fun getImagePath(image: Image) =
            if (image.path.isNotBlank()) "$imgPath/${image.path}"
            else ""

    fun createImage(imageFileId: String, content: ByteArray) =
        imageRepository.save(Image())
                .also {
                    imageFileRepository.addNewImageToFile(it.id!!, imageFileId)

                    val path = it.saveToFileSystem(imageFileId, content)
                    val updatedImage = Image(it.id, path, it.date)
                    return imageRepository.save(updatedImage)
                }

    private fun Image.saveToFileSystem(imageFileId: String, content: ByteArray) =
        fileSystemRepository.saveImage(imageFileId, id!!, content)
}
