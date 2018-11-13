package scrat98.github.algorithm.impl

import mu.KLogging
import net.objecthunter.exp4j.ExpressionBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import scrat98.github.*
import kotlin.math.PI
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AlgorithmsTestFactory {

  companion object : KLogging()

  private val epsilonRange = {
    val startEpsilon = 0.001
    val endEpsilon = 0.01
    val epsilonStep = 0.001
    val epsilonCount = ((endEpsilon - startEpsilon) / epsilonStep).toInt()
    generateSequence(startEpsilon) { it + epsilonStep }.take(epsilonCount).toList()
  }()

  private val algorithms = listOf(
      Dichotomy::class,
      GoldenSectionSearch::class,
      FibonacciMethod::class
  )

  private val functions = setOf(
      "sin(x)" to Section(-PI / 2, PI / 2),
      "cos(x)" to Section(0.0, PI),
      "(x-2)^2" to Section(-2.0, 20.0),
      "(x-15)^2 + 5" to Section(2.0, 200.0),
      "(x+5)^4" to Section(-10.0, 15.0),
      "exp(x)" to Section(0.0, 100.0),
      "x^2 + 2x - 4" to Section(-10.0, 20.0),
      "x^3 - x" to Section(0.0, 1.0))

  private fun findMinimum(function: (Double) -> Double, section: Section,
                          epsilon: Double): Double {
    val step = epsilon / 2
    val argumentsCount = ((section.x2 - section.x1) / step).toInt()
    return generateSequence(section.x1) { it + step }.take(argumentsCount).map(function).min()
        ?: Double.NaN
  }

  private fun parseFunction(functionString: String): (Double) -> Double {
    val expressionBuilder = ExpressionBuilder(functionString)
        .variables("x")
        .build()

    return { x -> expressionBuilder.setVariable("x", x).evaluate() }
  }

  private fun testAlgorithms(algorithm: Algorithm, expected: Double) {
    val actual = algorithm.calculateFunctionMinimum()
    logger.info("""
      Actual: $actual
      Expected: $expected
      epsilon: ${algorithm.epsilon}
    """)
    assertEquals(expected, actual, algorithm.epsilon)
  }

  private fun generateDynamicTest(algorithm: KClass<out Algorithm>,
                                  function: String,
                                  section: Section,
                                  epsilon: Double): DynamicTest {
    val parsedFunction = parseFunction(function)
    val algorithmInstance = algorithm.primaryConstructor!!.call(parsedFunction, section,
        epsilon)
    return DynamicTest.dynamicTest(
        "$algorithmInstance with $function on $section epsilon $epsilon") {
      testAlgorithms(algorithmInstance, findMinimum(parsedFunction, section, epsilon))
    }
  }

  @TestFactory
  fun testAlgorithmsDynamically(): List<DynamicTest> =
      algorithms.flatMap { algorithm ->
        functions.flatMap { (function, section) ->
          epsilonRange.map { epsilon ->
            generateDynamicTest(algorithm, function, section, epsilon)
          }
        }
      }
}