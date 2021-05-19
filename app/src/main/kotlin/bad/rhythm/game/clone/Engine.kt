package bad.rhythm.game.clone

import com.beust.klaxon.*
import java.io.File
import java.nio.*

import org.lwjgl.opengl.GL11.*
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.*

class Engine {

    companion object {

        val WINDOW_SIZE = Pair(1280, 720)
    }

    private var error: GLFWErrorCallback? = null
    private var window: Long? = null

    private fun init() {

        error = glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))

        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

        window = glfwCreateWindow(WINDOW_SIZE.first, WINDOW_SIZE.second, "Yum Game", NULL, NULL)
        if (window == NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        glfwMakeContextCurrent(window!!)
        glfwSwapInterval(1)
        glfwShowWindow(window!!)
    }

    private fun loop() {

        var beatmap: Beatmap = Klaxon().parse<Beatmap>(File("test.json").readText(Charsets.UTF_8))!!
        var game: Game = Game(beatmap)
        game.start()

        GL.createCapabilities()
        glClearColor(0f, 0f, 0f, 0f)

        while (!glfwWindowShouldClose(window!!)) {

            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            render(game)
            glfwSwapBuffers(window!!)
            glfwPollEvents()
        }
    }

    private fun render(game: Game) {

        game.draw()
    }

    fun run() {

        try {

            init()
            loop()

            glfwDestroyWindow(window!!)
        } finally {

            glfwTerminate()
            error?.free()
        }
    }

    private fun getCursorPos(): Pair<Double, Double> {

        val xBuffer: DoubleBuffer = BufferUtils.createDoubleBuffer(1)
        val yBuffer: DoubleBuffer = BufferUtils.createDoubleBuffer(1)

        glfwGetCursorPos(window!!, xBuffer, yBuffer)

        return Pair(xBuffer.get(0), yBuffer.get(0))
    }
}
