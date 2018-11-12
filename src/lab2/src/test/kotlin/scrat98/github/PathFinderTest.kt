package scrat98.github

import mu.KLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.math.min

class PathFinderTest {

  companion object : KLogging()

  private fun bellmanFord(graphFile: String): Int {
    val graph = createGraphFromFile(graphFile)

    val weightTable = IntArray(graph.verticesCount, { Int.MAX_VALUE / 2 })
    weightTable[0] = 0

    (0..graph.verticesCount).forEach {
      graph.getEdges().forEach { (from, to, weight) ->
        weightTable[to] = min(weightTable[to], weightTable[from] + weight)
      }
    }

    return weightTable[graph.verticesCount - 1]
  }

  @ParameterizedTest
  @ValueSource(strings = ["graph1", "graph2", "graph3", "graph4", "graph5"])
  fun `ensure correct finding minimum path`(graphFile: String) {
    val expected = bellmanFord(graphFile)
    val actual = PathFinder().findPath(graphFile).get(0)
    logger.info("""
      actual: $actual
      expected: $expected
    """.trimIndent())
    Assertions.assertEquals(expected, actual)
  }
}