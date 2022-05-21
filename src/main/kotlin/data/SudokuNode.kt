package data

import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.FontImageMap
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.writer
import java.util.SortedSet

data class SudokuNode(
    val id: String,
    val position: Pair<Int, Int>,
    var value: SudokuValues?,
    var possibleValues: MutableSet<SudokuValues>,
    var neighbours: List<SudokuNode>,
): Node {

    val entropy: Int
        get() = possibleValues.size

    @Suppress("UNCHECKED_CAST")
    override fun <T: Node> getNeighbours(graph: Graph<T>): List<T> {
        fun getColumnNeighbours(graph: Graph<SudokuNode>, columnPos: Int): List<Node> {
            return graph.toMatrix().mapNotNull { row -> row[columnPos].takeIf { node -> node.id != this.id } }
        }

        fun getRowNeighbours(graph: Graph<SudokuNode>, rowPos: Int): List<Node> {
            return graph.toMatrix()[rowPos].mapNotNull { node -> node.takeIf { it.id != this.id }}
        }

        fun getSurroundingNeighbours(graph: Graph<SudokuNode>, rowPos: Int, colPos: Int): List<SudokuNode> {
            return if (graph is SudokuGraph) {
                val subMatrices = graph.getSubMatrices()
                val subMatricesSize = graph.getSubMatricesSize()
                val localRowPos = rowPos.div(subMatricesSize)
                val localColPos = colPos.div(subMatricesSize)

                return subMatrices[localColPos.mod(subMatricesSize) * subMatricesSize + localRowPos.mod(subMatricesSize)]
                    .mapNotNull { node -> node.takeIf { it.id != this.id } }
            } else {
                emptyList()
            }
        }

        return buildList {
            val (row, column) = this@SudokuNode.position

            val columnNeighbours = getColumnNeighbours(graph as Graph<SudokuNode>, column)
            val rowNeighbours = getRowNeighbours(graph, row)
            val surroundingNeighbours = getSurroundingNeighbours(graph, row, column)

            addAll(columnNeighbours)
            addAll(rowNeighbours)
            addAll(surroundingNeighbours)
        } as List<T>
    }

    fun draw(program: Program, position: Vector2, nodeSize: Double) {
        program.extend {
            drawer.fill = value?.color ?: ColorRGBa.BLACK
            drawer.stroke = null
            drawer.rectangle(
                corner = position,
                width = nodeSize,
                height = nodeSize,
            )
        }
    }
}
