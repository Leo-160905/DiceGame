import java.awt.Dimension
import javax.swing.JButton

val gameTableSize = Dimension(600,600)
val navigatorSize = Dimension(600,200)
val btnCount = 12

var points = 0

var pause = false

var actionCounter = 0

val btnArray: ArrayList<LButton> = ArrayList()
val selectedBtn : ArrayList<LButton> = ArrayList()

val selectBtn: Array<JButton> = arrayOf(JButton(), JButton(), JButton())

fun main() {
    LFrame()
}