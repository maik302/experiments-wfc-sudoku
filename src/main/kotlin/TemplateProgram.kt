import kotlinx.coroutines.delay
import logic.SudokuWFC
import org.openrndr.KEY_SPACEBAR
import org.openrndr.PresentationMode
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.launch
import org.openrndr.math.Vector2

fun main() = application {
    configure {
        width = 768
        height = 576
    }

    program {
//        window.presentationMode = PresentationMode.MANUAL
//        val frameRate = 1L

        val sudokuBoardSize = 9
        val sudokuCellSize = 54.0

        val sudokuWFC = SudokuWFC(
            program = this,
            position = Vector2(
                x = (width / 2.0) - (sudokuBoardSize * sudokuCellSize) / 2,
                y = (height / 2.0) - (sudokuBoardSize * sudokuCellSize) / 2
            ),
            width = sudokuBoardSize,
            height = sudokuBoardSize,
            subMatricesSize = sudokuBoardSize / 3,
            nodeSize = sudokuCellSize,
        )

//        keyboard.keyDown.listen {
//            if (it.key == KEY_SPACEBAR) window.requestDraw()
//        }

        extend {
//            launch {
//                delay(1000L / frameRate)
//                window.requestDraw()
//            }

            drawer.clear(ColorRGBa.BLACK)
            sudokuWFC.step()
        }
    }
}
