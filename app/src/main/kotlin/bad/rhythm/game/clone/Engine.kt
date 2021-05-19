package bad.rhythm.game.clone

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.graphics.use
import kotlin.math.pow

class Engine() : KtxApplicationAdapter {

    private lateinit var renderer: ShapeRenderer
    private lateinit var game: Game

    private var selected: Int? = null

    // hardcoded for now
    private val screen: Pair<Float, Float> = Pair(1280f, 720f)
    private val width: Float = 240f
    private val vanishing: Float = 1220f
    private val judgement: Float = 80f

    // runtime constants
    private val center: Pair<Float, Float> = Pair(screen.first / 2, screen.second / 2)
    private val bottoms: Array<Float> = arrayOf(2 * width, width, 0f, - width, - 2 * width)

    override fun create() {
        game = Game(FileReader.ReadFile("test.json"))

        var song = Gdx.audio.newMusic(Gdx.files.internal("audio.mp3"));
        song.setLooping(false);
        song.play();

        renderer = ShapeRenderer()
        game.start()
    }

    override fun render() {
        handleInput()
        logic()
        draw()
    }

    private fun handleInput() {
        var mouseX = Gdx.input.x - center.first
        var mouseY = screen.second - Gdx.input.y

        print("$mouseY \n")

        if (mouseY < judgement && mouseX < bottoms.first() && mouseX > bottoms.last()) {
            when {
                mouseX > bottoms[1] -> selected = 0
                mouseX > bottoms[2] -> selected = 1
                mouseX > bottoms[3] -> selected = 2
                else -> selected = 3
            }
        } else {
            selected = null
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
                var column = it.first
                var y = vanishing - (vanishing - judgement) * it.second.pow(3f)
                var x = (y - vanishing) / (vanishing)

                renderer.line(x * bottoms[column] + center.first, y, x * bottoms[column + 1] + center.first, y)
            }
        }
    }

    private enum class State {
        TITLE, MENU, GAME
    }
}
