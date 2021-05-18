package bad.rhythm.game.clone

import kotlin.math.*
import kotlin.system.*
import org.lwjgl.*
import org.lwjgl.glfw.*
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.*
import org.lwjgl.system.MemoryStack.*
import org.lwjgl.system.MemoryUtil.*

class Game(var map: Beatmap) {

    private var startTime: Double = 0.0

    private var vanishing: Float = 1.5f
    private var width: Float = 0.8f
    private var approachRate: Float = 7f

    private var judgementLine: Float = -0.8f

    private var bottoms: FloatArray = floatArrayOf(width, width / 2f, 0f, -width / 2f, -width)

    public fun start() {
        startTime = glfwGetTime()
    }

    public fun tick() {}

    public fun draw() {


        drawRoad()
        drawAllNotes()
    }

    fun drawAllNotes() {

        var ibps: Float = 60.0f / map.sections[0].bpm
        var time: Float = glfwGetTime().toFloat()

        map.sections[0].hitObjects.forEach {

            drawNote((time - ibps * it.time / 24f - map.sections[0].offset).coerceIn(0f, 2f), it.column)

        }
    }

    fun drawNote(time: Float, column: Int) {

        assert(column in 0..4)

        var y = vanishing - (vanishing - judgementLine) * time.pow(approachRate)
        var x = (y - vanishing) / (vanishing + 1f)

        glBegin(GL_LINES)

        glVertex2f(x * bottoms[column], y)
        glVertex2f(x * bottoms[column + 1], y)

        glEnd()
    }

    fun drawRoad() {

        glColor3f(1f, 1f, 1f)

        glBegin(GL_LINES)

        glVertex2f(-1f, judgementLine)
        glVertex2f(1f, judgementLine)

        for (i in bottoms.indices) {

            glVertex2f(0f, vanishing)
            glVertex2f(bottoms[i], -1f)
        }

        glEnd()
    }
}
