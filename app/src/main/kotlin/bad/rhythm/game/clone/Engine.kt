package bad.rhythm.game.clone

import java.nio.*
import org.lwjgl.*
import org.lwjgl.glfw.*
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.*
import org.lwjgl.system.MemoryStack.*
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

        GL.createCapabilities()
        glClearColor(0f, 0f, 0f, 0f)

        while (!glfwWindowShouldClose(window!!)) {

            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            render()
            glfwSwapBuffers(window!!)
            glfwPollEvents()
        }
    }

    private fun render() {
        // Draw  roads
        glColor3f(1f, 1f, 1f)

        glBegin(GL_LINES)
        glVertex2f(0.0f, 1.0f)
        glVertex2f(0.0f, -1.0f)

        glVertex2f(0.2f, 1.0f)
        glVertex2f(0.8f, -1.0f)

        glVertex2f(0.1f, 1.0f)
        glVertex2f(0.4f, -1.0f)

        glVertex2f(-0.2f, 1.0f)
        glVertex2f(-0.8f, -1.0f)

        glVertex2f(-0.1f, 1.0f)
        glVertex2f(-0.4f, -1.0f)

        glVertex2f(-1.0f, -0.8f)
        glVertex2f(1.0f, -0.8f)
        glEnd()

        // Draw highlighted
        val curserPos: Pair<Double, Double> = getCursorPos()

        if (curserPos != Pair(-1.0, -1.0)) {

            glColor4f(1f, 1f, 1f, 0.5f)

            glBegin(GL_POLYGON)
        }

        glColor4f(1f, 1f, 1f, 0.5f)

        glBegin(GL_POLYGON)

        glEnd()
    }

    fun drawNote(distance: Double, column: Int) {

        


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

        if (glfwGetMouseButton(window!!, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {

            val xBuffer: DoubleBuffer = BufferUtils.createDoubleBuffer(1)
            val yBuffer: DoubleBuffer = BufferUtils.createDoubleBuffer(1)

            glfwGetCursorPos(window!!, xBuffer, yBuffer)

            return Pair(xBuffer.get(0), yBuffer.get(0))
        }
        return Pair(-1.0, -1.0)
    }
}
