package pl.put.poznan.oculus.oculusimagesservice.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pl.put.poznan.oculus.oculusimagesservice.model.ScaledImage
import pl.put.poznan.oculus.oculusimagesservice.model.ScaledImageSize
import java.io.File
import javax.imageio.ImageIO

internal class ScalingServiceTest {

    private val service = ScalingService()
    private val originalImagePath = this.javaClass.getResource("/test_img.jpg").file

    @Test
    fun `should scale image to given size`() {
        // given
        val size = ScaledImageSize.S

        // when
        val result = service.scaleImage(originalImagePath, size)

        // then
        assertScaledImage(result, size)
    }

    @Test
    fun `should scale image to all sizes`() {
        // when
        val result = service.scaleImage(originalImagePath)

        // then
        assertIterableEquals(ScaledImageSize.values().toList(), result.map { it.size })
        result.forEach { assertScaledImage(it, it.size) }
    }

    private fun assertScaledImage(scaled: ScaledImage, desiredSize: ScaledImageSize) {
        assertTrue(scaled.path.endsWith("test_img_${desiredSize.name.toLowerCase()}.jpg"))
        File(scaled.path).let { resultFile ->
            ImageIO.read(resultFile).let {
                assertEquals(desiredSize.width, it.width)
                assertEquals(desiredSize.height, it.height)
            }
            resultFile.delete()
        }
    }
}
