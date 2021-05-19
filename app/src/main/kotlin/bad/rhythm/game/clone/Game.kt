package bad.rhythm.game.clone

import java.time.Instant

class Game(var map: Beatmap) {

    private var startTime: Long = 0
    private var approachRate: Float = 0.9f
    private var currentSection: Int = 0

    fun start() {
        startTime = Instant.now().toEpochMilli()
    }

    fun calculate(): Array<Pair<Int, Float>> {
        var output: ArrayList<Pair<Int, Float>> = arrayListOf()

        var currentTime: Float = (Instant.now().toEpochMilli() - startTime - map.sections[currentSection].offset) / 1000f
        var currentSection = map.sections[0]

        currentSection.hitObjects.forEach {
            output.add(Pair(it.column, (approachRate * (currentTime - 10f * it.time / currentSection.bpm)).coerceIn(-1f, 1f) + 1f))
        }

        return output.toTypedArray()
    }

    fun verify(column: Int, time: Long) {

    }
}
