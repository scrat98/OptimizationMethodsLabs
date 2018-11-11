package scrat98.github

import java.io.File
import java.util.*

class Graph(val verticesCount: Int) {

  private val outgoingVertices = Array(verticesCount, { mutableListOf<Int>() })

  private val weightTable = Array(verticesCount, { IntArray(verticesCount, { Int.MAX_VALUE }) })

  private val incomingVertices = Array(verticesCount, { mutableListOf<Int>() })

  fun add(from: Int, to: Int, weight: Int) {
    outgoingVertices[from].add(to)
    incomingVertices[to].add(from)
    weightTable[from][to] = weight
  }

  fun getIncomingVertices(vertex: Int): List<Int> = incomingVertices[vertex]

  fun getOutgoingVertices(vertex: Int): List<Int> = outgoingVertices[vertex]

  fun getWeight(from: Int, to: Int): Int = weightTable[from][to]
}

class Path(private val graph: Graph) {

  private val verticesCount = graph.verticesCount

  private val weightTable = IntArray(verticesCount, { Int.MAX_VALUE })

  private val path = IntArray(verticesCount, { -1 })

  operator fun get(vertex: Int) = weightTable[vertex]

  fun updateDistance(from: Int, to: Int, newWeight: Int) {
    if (newWeight < weightTable[from]) {
      weightTable[from] = newWeight
      path[from] = to
    }
  }

  fun showPath(from: Int = 1, to: Int = verticesCount): String {
    var current = from - 1
    var resultPath = ""
    var resultSum = ""
    while (current != to - 1) {
      val next = path[current]
      resultPath += "${current + 1} -> "
      resultSum += "${graph.getWeight(current, next)}"
      current = next
      if (current != to - 1) resultSum += " + "
    }

    resultPath += "${current + 1}"
    resultSum += " = ${weightTable[from - 1]}"

    return """
      $resultPath
      $resultSum
    """.trimIndent()
  }
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

fun main(args: Array<String>) {
  val graphFile = File(ClassLoader.getSystemResource("graph").file)
  val scanner = Scanner(graphFile)

  val verticesCount = scanner.nextInt()
  val graph = Graph(verticesCount)
  while (scanner.hasNextLine()) {
    val from = scanner.nextInt() - 1
    val to = scanner.nextInt() - 1
    val weight = scanner.nextInt()
    graph.add(from, to, weight)
  }

  val path = findPath(graph)

  File("results/lab2/graphSolution").also { it.parentFile.mkdirs() }.printWriter().use { out ->
    out.println(path.showPath())
  }
}