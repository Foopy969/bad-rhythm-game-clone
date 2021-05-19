package bad.rhythm.game.clone

import com.beust.klaxon.Klaxon
import java.io.File

class FileReader {
    companion object {
        fun ReadFile(pathname: String) : Beatmap = Klaxon().parse<Beatmap>(File(pathname).readText(Charsets.UTF_8))!!
    }
}