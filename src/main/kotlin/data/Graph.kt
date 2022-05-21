package data

typealias GraphMatrix<T> = List<List<T>>

interface Graph<T : Node> {
    fun toMatrix(): GraphMatrix<T>
}