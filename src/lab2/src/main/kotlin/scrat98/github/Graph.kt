package scrat98.github

import java.io.File
import java.util.*

data class Edge(
    val from: Int,
    val to: Int,
    val weight: Int
)

class Graph(val verticesCount: Int) {

  private val outgoingVertices = Array(verticesCount, { mutableListOf<Int>() })

  private val weightTable = Array(verticesCount, { IntArray(verticesCount, { Int.MAX_VALUE / 2 }) })

  private val incomingVertices = Array(verticesCount, { mutableListOf<Int>() })

  private val edges = mutableListOf<Edge>()

  fun add(from: Int, to: Int, weight: Int) {
    outgoingVertices[from].add(to)
    incomingVertices[to].add(from)
    weightTable[from][to] = weight
    edges.add(Edge(from, to, weight))
  }

  fun getEdges(): List<Edge> = edges.toList()

  fun getIncomingVertices(vertex: Int): List<Int> = incomingVertices[vertex]

  fun getOutgoingVertices(vertex: Int): List<Int> = outgoingVertices[vertex]

  fun getWeight(from: Int, to: Int): Int = weightTable[from][to]
}

fun createGraphFromFile(fileName: String): Graph {
  val graphFile = File(ClassLoader.getSystemResource(fileName).file)
  val scanner = Scanner(graphFile)

  val verticesCount = scanner.nextInt()
  val graph = Graph(verticesCount)
  while (scanner.hasNextLine()) {
    val from = scanner.nextInt() - 1
    val to = scanner.nextInt() - 1
    val weight = scanner.nextInt()
    graph.add(from, to, weight)
  }

  return graph
}