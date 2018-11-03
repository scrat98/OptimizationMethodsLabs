package scrat98.github.algorithm.impl

import mu.KLogging
import org.junit.jupiter.api.Assertions.assertEquals
import scrat98.github.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class AlgorithmsTestFactory {

  companion object : KLogging()

  data class TestFunction(
      val function: (Double) -> Double,
      val alias: String
  )

  private val epsilon = 0.01

  private val algorithms = listOf(
      Dichotomy(epsilon),
      FibonacciMethod(),
      GoldenSectionSearch()
  )

  private val testFunctions = setOf(
      TestFunction({ x: Double -> sin(x) }, "sin(x)") to Section(-PI / 2, PI / 2),
      TestFunction({ x: Double -> cos(x) }, "cos(x)") to Section(0.0, PI),
      TestFunction({ x: Double -> (x - 2) * (x - 2) }, "(x-2)^2") to Section(-2.0, 20.0),
      TestFunction({ x: Double -> (x - 15) * (x - 15) + 5 }, "(x-15)^2 + 5") to Section(2.0, 200.0),
      TestFunction({ x: Double -> Math.pow((x + 5), 4.0) }, "(x+5)^4") to Section(-10.0, 15.0),
      TestFunction({ x: Double -> Math.exp(x) }, "exp(x)") to Section(0.0, 100.0),
      TestFunction({ x: Double -> x * x + 2 * x - 4 }, "x^2 + 2x - 4") to Section(-10.0, 20.0),
      TestFunction({ x: Double -> x * x * x - x }, "x^3 - x") to Section(0.0, 1.0))

  private fun findMinimum(function: (Double) -> Double, section: Section,
                          epsilon: Double): Double {
    val step = epsilon / 2
    val argumentsCount = ((section.x2 - section.x1) / step).toInt()
    return generateSequence(section.x1) { it + step }.take(argumentsCount).map(function).min()
        ?: Double.NaN
  }

  private fun testAlgorithms(algorithm: Algorithm, function: (Double) -> Double, section: Section,
                             epsilon: Double) {
    var currentSection = section
    while (!currentSection.isClosed(epsilon)) {
      currentSection = algorithm.getNextSection(function, currentSection)
    }
    val actual = function(currentSection.center())
    val expected = findMinimum(function, section, epsilon)
    logger.info("""
      Actual: $actual
      Expected: $expected
    """)
    assertEquals(expected, actual, epsilon)
  }

  @TestFactory
  fun testAlgorithmsDynamically(): List<DynamicTest> = algorithms.flatMap { algorithm ->
    testFunctions.map { (testFunction, section) ->
      DynamicTest.dynamicTest("$algorithm with ${testFunction.alias} on $section") {
        testAlgorithms(algorithm, testFunction.function, section, epsilon)
      }
    }
  }
}