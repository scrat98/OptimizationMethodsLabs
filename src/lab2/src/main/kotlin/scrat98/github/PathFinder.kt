package scrat98.github

import java.io.File

class PathFinder {
  fun findPath(graphFile: String): Path {
    val graph = createGraphFromFile(graphFile)
    return findPath(graph)
  }

  fun findPath(graph: Graph): Path {
    val lastVertex = graph.verticesCount - 1

    val path = Path(graph)
    path.updateDistance(lastVertex, lastVertex, 0)

    val verticesQueue = mutableSetOf<Int>()
    verticesQueue.addAll(graph.getIncomingVertices(lastVertex))

    while (!verticesQueue.isEmpty()) {
      val currentVertex = verticesQueue.first()
      verticesQueue.remove(currentVertex)

      graph.getOutgoingVertices(currentVertex).forEach { dstVertex ->
        val newDistance = path[dstVertex] + graph.getWeight(currentVertex, dstVertex)
        path.updateDistance(currentVertex, dstVertex, newDistance)
      }

      verticesQueue.addAll(graph.getIncomingVertices(currentVertex))
    }

    return path
  }
}

fun main(args: Array<String>) {
  val path = PathFinder().findPath("graph")

  File("results/lab2/graphSolution").also { it.parentFile.mkdirs() }.printWriter().use { out ->
    out.println(path.showPath())
  }
}