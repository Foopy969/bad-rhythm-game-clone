package bad.rhythm.game.clone

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

public fun main(args: Array<String>) {

    val config = LwjglApplicationConfiguration().apply {
        title = "Yum Game"
        width = 1280
        height = 720
    }

    LwjglApplication(Engine(), config)
}
