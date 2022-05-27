package data

import org.openrndr.Program
import org.openrndr.math.Vector2

class SudokuGraph(
    private val width: Int,
    private val height: Int,
    private val subMatricesSize: Int,
) : Graph<SudokuNode> {

    private val matrixRepresentation: GraphMatrix<SudokuNode>
    private val subMatricesRepresentation: List<List<SudokuNode>>

    init {
        assert(width % subMatricesSize == 0 && height % subMatricesSize == 0) { "Width and Height must be powers of $subMatricesSize" }
        matrixRepresentation = initMatrixRepresentation()
        subMatricesRepresentation = initSubMatrices()
    }

    override fun toMatrix(): GraphMatrix<SudokuNode> {
        return matrixRepresentation.ifEmpty { initMatrixRepresentation() }
    }

    private fun initMatrixRepresentation(): GraphMatrix<SudokuNode> {
        return buildList {
            for (row in 0 until height) {
                add(
                    buildList {
                        for (colum in 0 until width) {
                            add(
                                SudokuNode(
                                    id = "WFCNode($row,$colum)",
                                    position = Pair(row, colum),
                                    value = null,
                                    possibleValues = SudokuValues.values().toSortedSet(compareBy { it.value }),
                                    neighbours = emptyList()
                                )
                            )
                        }
                    }
                )
            }
        }
    }

    private fun initSubMatrices(): List<List<SudokuNode>> {
        return buildList {
            val rowsDividedBySection = matrixRepresentation.map { row -> row.chunked(subMatricesSize) }
            val subMatricesElementCount = subMatricesSize * subMatricesSize

            for (section in 0 until subMatricesSize) {
                val rowSection = rowsDividedBySection.flatMap { row -> row[section] }.chunked(subMatricesElementCount)
                addAll(rowSection)
            }
        }
    }

    fun getSubMatricesSize() = this.subMatricesSize

    fun getSubMatrices(): List<List<SudokuNode>> = subMatricesRepresentation

    fun draw(program: Program, position: Vector2, nodeSize: Double) {
        for (row in 0 until height) {
            for (column  in 0 until width) {
                matrixRepresentation[row][column].draw(program, position, nodeSize)
            }
        }
    }
}