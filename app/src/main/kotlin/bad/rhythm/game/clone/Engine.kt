package bad.rhythm.game.clone

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.graphics.use
import java.time.Instant
import kotlin.math.absoluteValue
import kotlin.math.pow

class Engine : KtxApplicationAdapter {

    private lateinit var renderer: ShapeRenderer
    private lateinit var game: Game

    // memory
    private var selected: Int? = null
    private var lastMouseTime: Long = 0
    private var lastMouseState: MouseState = MouseState.UP

    // hardcoded for now
    private val screen: Pair<Float, Float> = Pair(1280f, 720f)
    private val width: Float = 240f
    private val vanishing: Float = 1220f
    private val judgement: Float = 80f
    private val threshold: Long = 200

    // runtime constants
    private val center: Pair<Float, Float> = Pair(screen.first / 2, screen.second / 2)
    private val bottoms: Array<Float> = arrayOf(2 * width, width, 0f, - width, - 2 * width)

    override fun create() {
        game = Game(FileReader.ReadFile("test.json"))

        val song = Gdx.audio.newMusic(Gdx.files.internal("audio.mp3"))
        song.isLooping = false
        song.play()

        renderer = ShapeRenderer()
        game.start()
    }

    override fun render() {
        handleInput()
        logic()
        draw()
    }

    private fun handleInput() {
        val mouseX = Gdx.input.x - center.first
        val mouseY = screen.second - Gdx.input.y

        selected = if (mouseY < judgement && mouseX.absoluteValue < bottoms.first()) {
            when {
                mouseX > bottoms[1] -> 0
                mouseX > bottoms[2] -> 1
                mouseX > bottoms[3] -> 2
                else -> 3
            }
        } else {
            null
        }

        // determine mouse state
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (lastMouseState == MouseState.UP) {
                lastMouseTime = Instant.now().toEpochMilli()
                lastMouseState = MouseState.DOWN
            } else if (Instant.now().toEpochMilli() - lastMouseTime > threshold) {
                lastMouseState = MouseState.HOLD
            }
        } else {
            lastMouseState = if (lastMouseState == MouseState.DOWN) MouseState.CLICK else MouseState.UP
        }

        if (lastMouseState == MouseState.CLICK) {

        }
    }

    private fun logic() {

    }

    private fun draw() {
        // clear screen
        clearScreen(0f, 0f, 0f, 0f)

        //draw highlighted
        if (selected != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
            renderer.use(ShapeRenderer.ShapeType.Filled) {
                renderer.setColor(1f, 1f, 1f, 0.2f)
                renderer.triangle(center.first, vanishing, bottoms[selected!!] + center.first, 0f, bottoms[selected!! + 1] + center.first, 0f)
            }
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }

        // draw background
        renderer.use(ShapeRenderer.ShapeType.Line) {
            renderer.color = Color.WHITE
            renderer.line(0f, judgement, screen.first, judgement)
            bottoms.forEach {
                renderer.line(center.first, vanishing, it + center.first, 0f)
            }
        }

        // draw notes
        renderer.use(ShapeRenderer.ShapeType.Line) {
            renderer.color = Color.GREEN
            game.calculate().forEach {
                val column = it.first
                val y = vanishing - (vanishing - judgement) * it.second.pow(3f)
                val x = (y - vanishing) / (vanishing)

                renderer.line(x * bottoms[column] + center.first, y, x * bottoms[column + 1] + center.first, y)
            }
        }
    }

    private enum class AppState {
        TITLE, MENU, GAME
    }

    private enum class MouseState {
        UP, DOWN, CLICK, HOLD
    }
}
