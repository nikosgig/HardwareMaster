package hardwaremaster.com.data

data class Gpu(
        val avgFps1080p: Long? = null,
        val avgFps2k: Long? = null,
        val avgFps4k: Long? = null,
        val firestrike: Long? = null,
        val gpuClock: Long? = null,
        val graphicsRamSize: Long? = null,
        val graphicsRamType: String? = null,
        val memoryBus: String? = null,
        val memoryClock: Long? = null,
        val model: String? = null,
        val passmark: Long? = null,
        val releaseDate: String? = null
)