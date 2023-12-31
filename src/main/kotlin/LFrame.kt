import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import java.util.Random
import javax.swing.Timer
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextField

class LFrame : JFrame() {
    var pointField: JTextField?
    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Little Dice Game"
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        location = Point(
            (screenSize.width - gameTableSize.width) / 2,
            (screenSize.height - gameTableSize.height - navigatorSize.height) / 2
        )

        val gamePanel = JPanel()
        gamePanel.preferredSize = gameTableSize
        gamePanel.layout = null
        for (y in 0..<gameTableSize.height / (gameTableSize.height / btnCount)) {
            for (x in 0..<gameTableSize.width / (gameTableSize.width / btnCount)) {
                val posLin = y * 600 / (gameTableSize.width / btnCount) + x
                btnArray.add(LButton(x % 6 + 1, posLin, true))
                btnArray[posLin].size = Dimension((gameTableSize.width / btnCount), (gameTableSize.width / btnCount))
                btnArray[posLin].location =
                    Point(x * (gameTableSize.width / btnCount), y * (gameTableSize.width / btnCount))

                btnArray[posLin].addActionListener {
                    if (btnArray[posLin].covered != null && !pause) {
                        if (!btnArray[posLin].covered!!) {
                            btnArray[posLin].covered = null
                            btnArray[posLin].text = ""

                            selectedBtn.add(btnArray[posLin])

                            selectBtn[actionCounter].text = btnArray[posLin].diceValue.toString()

                            repaintPanel()
                            actionCounter++
                        }
                    }
                    if (actionCounter >= 3) {
                        val t = Timer(500) {
                            endRound()
                            pause = false
                        }
                        pause = true
                        t.isRepeats = false
                        t.start()
                    }
                }
                gamePanel.add(btnArray[posLin])
            }
        }
        randomizePanel()
        repaintPanel()


        val navigatorPanel = JPanel()
        navigatorPanel.preferredSize = navigatorSize
        navigatorPanel.layout = null
        for (i in selectBtn) {
            i.size = Dimension(200 / 2, 200 / 2)
            i.location = Point(selectBtn.indexOf(i) * 200 + 50, 50)

            navigatorPanel.add(i)
        }

        pointField = JTextField()
        pointField?.location = Point(0,0)
        pointField?.size = Dimension(100,25)
        pointField?.isEditable = false
        pointField?.text = "Points: $points"

        navigatorPanel.add(pointField)

        val cp = contentPane
        cp.add(gamePanel, BorderLayout.CENTER)
        cp.add(navigatorPanel, BorderLayout.SOUTH)

        pack()
        isVisible = true
    }

    fun repaintPanel() {
        for (i in btnArray) {
            var y = i.position / btnCount
            val x = i.position % btnCount
            y++

            val posLin = if (y < btnCount) y * 600 / (gameTableSize.width / btnCount) + x else -1

            var lastbtn = true
            if (posLin != -1) {
                if (btnArray[posLin].covered != null) {
                    lastbtn = false
                }
            }
            if (posLin == -1 || lastbtn) {
                i.text = if (i.covered != null) {
                    i.diceValue.toString()
                } else {
                    ""
                }
                i.covered = if (i.covered != null) {
                    false
                } else {
                    null
                }
            }

        }
    }

    fun endRound() {
        actionCounter = 0

        if (selectBtn[0].text == selectBtn[1].text && selectBtn[1].text == selectBtn[2].text) {
            points += 3
            pointField?.text = "Points: $points"
        } else {
            for (i in selectedBtn.reversed()) {
                var posLin = i.position
                btnArray[posLin].text = i.diceValue.toString()
                btnArray[posLin].covered = false

                var y = i.position / btnCount
                val x = i.position % btnCount
                y--

                posLin = if (y >= 0) y * 600 / (gameTableSize.width / btnCount) + x else -1
                if (posLin != -1) {
                    btnArray[posLin].text = ""
                    btnArray[posLin].covered = true
                }
            }

            points -= 1
            pointField?.text = "Points: $points"
        }
        for (i in selectBtn) {
            i.text = ""
        }
        selectedBtn.clear()

        var allFinished = true
        for (i in btnArray) {
            if (i.covered != null) {
                allFinished = false
            }
        }
        if (allFinished) {
            dispose()
            println("You have won with $points points!")
        }
    }

    fun randomizePanel() {
        for (i in 0..400) {
            val rnd = Random()
            val index1 = rnd.nextInt(btnArray.size - 1)
            val index2 = rnd.nextInt(btnArray.size - 1)

            val diceValue1 = btnArray[index1].diceValue
            btnArray[index1].diceValue = btnArray[index2].diceValue
            btnArray[index2].diceValue = diceValue1
        }
    }
}