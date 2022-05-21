package data

interface Node {
    fun <T: Node> getNeighbours(graph: Graph<T>): List<T>
}