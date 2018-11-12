package scrat98.github

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