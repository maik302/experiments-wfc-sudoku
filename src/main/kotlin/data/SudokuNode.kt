package data

import org.openrndr.Program
import org.openrndr.math.Vector2

data class SudokuNode(
    val id: String,
    val position: Pair<Int, Int>,
    var value: SudokuValues?,
    var possibleValues: MutableSet<SudokuValues>,
    var neighbours: List<SudokuNode>,
): Node {

    companion object {
        private const val POSSIBLE_NODE_SIZE_SCALE_FACTOR = 1.0
    }

    val entropy: Int
        get() = possibleValues.size

    private val possibleValuesCount: Int = possibleValues.size

    @Suppress("UNCHECKED_CAST")
    override fun <T: Node> getNeighbours(graph: Graph<T>): List<T> {
        fun getColumnNeighbours(graph: Graph<SudokuNode>, columnPos: Int): List<Node> {
            return graph.toMatrix().mapNotNull { row -> row[columnPos].takeIf { it.id != this.id && it.entropy > 0 } }
        }

        fun getRowNeighbours(graph: Graph<SudokuNode>, rowPos: Int): List<Node> {
            return graph.toMatrix()[rowPos].mapNotNull { node -> node.takeIf { it.id != this.id && it.entropy > 0 } }
        }

        fun getSurroundingNeighbours(graph: Graph<SudokuNode>, rowPos: Int, colPos: Int): List<SudokuNode> {
            return if (graph is SudokuGraph) {
                val subMatrices = graph.getSubMatrices()
                val subMatricesSize = graph.getSubMatricesSize()
                val localRowPos = rowPos.div(subMatricesSize)
                val localColPos = colPos.div(subMatricesSize)

                return subMatrices[localColPos.mod(subMatricesSize) * subMatricesSize + localRowPos.mod(subMatricesSize)]
                    .mapNotNull { node -> node.takeIf { it.id != this.id && it.entropy > 0} }
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

    fun draw(program: Program, worldPosition: Vector2, nodeSize: Double) {
        val localPosition = Vector2(position.first.toDouble(), position.second.toDouble())
        val relativePosition = Vector2(
            x = worldPosition.x + (localPosition.x * nodeSize),
            y = worldPosition.y + (localPosition.y * nodeSize)
        )

        if (entropy > 0) {
            drawUnCollapsedNode(program, relativePosition, nodeSize)
        } else {
            drawCollapsedNode(program, relativePosition, nodeSize)
        }
    }

    private fun drawCollapsedNode(program: Program, position: Vector2, nodeSize: Double) {
        program.extend {
            drawer.fill = value?.color
            drawer.stroke = null
            drawer.rectangle(
                corner = position,
                width = nodeSize,
                height = nodeSize,
            )
        }
    }

    private fun drawUnCollapsedNode(program: Program, position: Vector2, nodeSize: Double) {
        val possibleNodesSize = (nodeSize / possibleValuesCount) * POSSIBLE_NODE_SIZE_SCALE_FACTOR
        val lastAvailablePosition = position.x + nodeSize

        program.extend {
            var xPosIndex = 0
            var yPosIndex = 0
            possibleValues.forEach { value ->
                val possibleNodePositionX : Double
                val possibleNodePositionY : Double
                if (position.x + (xPosIndex * possibleNodesSize) >= lastAvailablePosition) {
                    possibleNodePositionX = position.x
                    possibleNodePositionY = position.y + (yPosIndex * possibleNodesSize)
                    xPosIndex = 0
                    yPosIndex += 1
                } else {
                    possibleNodePositionX = position.x + (xPosIndex * possibleNodesSize)
                    possibleNodePositionY = position.y + (yPosIndex * possibleNodesSize)
                    xPosIndex += 1
                }

                drawer.fill = value.color
                drawer.stroke = null
                drawer.rectangle(
                    corner = Vector2(possibleNodePositionX, possibleNodePositionY),
                    width = possibleNodesSize,
                    height = possibleNodesSize,
                )
            }
        }
    }
}
