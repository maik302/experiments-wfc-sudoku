package logic

import data.*
import ext.exceptions.ContradictionException
import org.openrndr.Program
import org.openrndr.math.Vector2
import kotlin.random.Random

/**
 * Based on https://youtu.be/2SuvO4Gi7uY 😊
 */
class SudokuWFC(
    private val program: Program,
    private val position: Vector2,
    private val width: Int,
    private val height: Int,
    private val subMatricesSize: Int,
    private val nodeSize: Double = 20.0
) : WFCLogic {

    private lateinit var graph: Graph<SudokuNode>
    private lateinit var unCollapsedNodes: MutableList<SudokuNode>

    override fun run() {
        //TODO: Add a system that handles contradiction states (when there's no possible value to choose from)
        try {
            initGraphData()

            while (unCollapsedNodes.isNotEmpty()) {
                val randomUnCollapsedNode = unCollapsedNodes.popRandomUnCollapsedNode()
                randomUnCollapsedNode?.collapse()
            }
            (graph as SudokuGraph).draw(program, position, nodeSize)
        } catch (e: ContradictionException) {
            println("A contradiction was found!")
            run()
        }
    }

    private fun initGraphData() {
        graph = SudokuGraph(width, height, subMatricesSize)
        unCollapsedNodes = graph.toMatrix().flatten().toMutableList()
    }

    private fun MutableList<SudokuNode>.popRandomUnCollapsedNode(): SudokuNode? {
        return if (isNotEmpty()) {
            var nextMinEntropy: Int
            this.sortedBy { it.entropy }
                .also { nextMinEntropy = it.first().entropy }
                .filter { node -> node.entropy == nextMinEntropy }
                .let { minEntropyNodes ->
                    val randomPosition = Random.nextInt(until = minEntropyNodes.size)
                    minEntropyNodes.elementAt(randomPosition)
                }
                .also { remove(it) }
        } else {
            null
        }
    }

    override fun <T : Node> T.collapse() {
        fun SudokuNode.propagate(collapsedValue: SudokuValues) {
            val neighbours = getNeighbours(graph)
            neighbours.onEach { neighbour ->
                neighbour.possibleValues.remove(collapsedValue)
            }
        }

        with(this as SudokuNode) {
            val collapsedValue = possibleValues.firstOrNull() ?: throw ContradictionException()
            value = collapsedValue
            possibleValues = mutableSetOf()
            propagate(collapsedValue)
        }
    }

    fun step() {
        if (!this::graph.isInitialized)
            initGraphData()

        if (unCollapsedNodes.isNotEmpty()) {
            try {
                val randomUnCollapsedNode = unCollapsedNodes.popRandomUnCollapsedNode()
                randomUnCollapsedNode?.collapse()
                (graph as SudokuGraph).draw(program, position, nodeSize)
            } catch (e: ContradictionException) {
                println("A contradiction was found!")
                initGraphData()
            }
        }
    }
}