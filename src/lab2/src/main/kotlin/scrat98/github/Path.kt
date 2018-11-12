package scrat98.github

class Path(val graph: Graph) {

  private val verticesCount = graph.verticesCount

  private val weightTable = IntArray(verticesCount, { Int.MAX_VALUE / 2 })

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