package pl.put.poznan.oculus.oculusimagesservice.service

import org.springframework.stereotype.Service
import pl.put.poznan.oculus.oculusimagesservice.model.ScaledImage
import pl.put.poznan.oculus.oculusimagesservice.model.ScaledImageSize
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

@Service
class ScalingService {

    fun scaleImage(path: String) = ScaledImageSize.values().map { scaleImage(path, it) }

    fun scaleImage(path: String, size: ScaledImageSize) = scaleImage(File(path), size)

    fun scaleImage(source: File, size: ScaledImageSize) = ScaledImage(
            scaleImage(source, File(scaledPath(source, size)), size.width, size.height).absolutePath
                    .map { if(it == '\\') '/' else it } // windows fix
                    .joinToString(""),
            size
    )

    private fun scaleImage(source: File, result: File, width: Int, height: Int) = result.apply {
        ImageIO.read(source).let { original ->
            BufferedImage(width, height, BufferedImage.SCALE_DEFAULT).apply {
                createGraphics().drawImage(original, 0, 0, width, height, null)
            }.let { ImageIO.write(it, source.extension, this) }
        }
    }

    companion object {
        private fun scaledPath(source: File, size: ScaledImageSize) =
                "${source.parent}/${source.nameWithoutExtension}_${size.toString().toLowerCase()}.${source.extension}"
    }
}
