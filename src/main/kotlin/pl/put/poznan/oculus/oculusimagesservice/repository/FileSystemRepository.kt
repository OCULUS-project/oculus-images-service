package pl.put.poznan.oculus.oculusimagesservice.repository

import org.springframework.stereotype.Repository
import pl.put.poznan.oculus.oculusimagesservice.filesystem.FileSystemConnector

@Repository
class FileSystemRepository (
        private val fileSystemConnector: FileSystemConnector
) {
    fun saveImageFile(id: String): String {
        val dirName = imageFileName(id)
        return fileSystemConnector.createDirectory(dirName)
    }

    fun saveImage(imageFileId: String, imageId: String, content: ByteArray): String {
        val fileName = imageFileName(imageFileId)
        val imageName = imageName(imageId)
        return fileSystemConnector.createFile(fileName, imageName, content)
    }

    fun deleteImageFile(imageFileId: String) = deleteFileOrDirectory(imageFileName(imageFileId))
    fun deleteFileOrDirectory(path: String) = fileSystemConnector.deleteFile(path)

    companion object {
        private const val IMAGE_FILE_PREFIX = "f_"
        private const val IMAGE_PREFIX = "img_"
        private const val IMAGE_EXTENSION = ".jpg"

        private fun imageFileName(id: String) = "$IMAGE_FILE_PREFIX$id"
        private fun imageName(id: String) = "$IMAGE_PREFIX$id$IMAGE_EXTENSION"
    }
}