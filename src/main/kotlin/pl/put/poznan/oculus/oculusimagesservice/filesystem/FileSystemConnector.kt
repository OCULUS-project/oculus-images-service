package pl.put.poznan.oculus.oculusimagesservice.filesystem

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class FileSystemConnector internal constructor(
        @Value("\${img.root}")
        private val rootPath: String
) {

    fun getFile(path: String): File {
        val file = File(path)
        return if (file.exists()) file
        else throw Exception("no such file: $path")
    }

    fun createDirectory(name: String): String {
        val path = path(name)
        val directory = File(path)
        if (directory.exists()) throw Exception("directory already exists $path")
        directory.mkdirs()
        return path
    }

    fun createFile(directory: String, name: String, content: ByteArray): String {
        val directoryPath = path(directory)
        if (!File(directoryPath).exists()) throw Exception("invalid directory $directoryPath")

        val filePath = path(name, directoryPath)
        val file = File(filePath)
        if (file.exists()) throw Exception("file already exists $filePath")

        file.writeBytes(content)
        file.createNewFile()
        return filePath
    }

    fun deleteFile(path: String) = File("$rootPath$path").delete()

    private fun path(name: String, root: String = rootPath) =
            root +
            (if (!root.endsWith('/')) "/" else "") +
            name
}