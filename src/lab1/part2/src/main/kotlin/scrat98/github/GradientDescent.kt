package scrat98.github

import mu.KLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct

@SpringBootApplication
class GradientDescent(
    private val function: (Double, Double) -> Double,
    private val gradX1: (Double, Double) -> Double,
    private val gradX2: (Double, Double) -> Double,
    private val epsilon: Double,
    private val maxIterations: Int
) : Runnable {
  companion object : KLogging()

  private var x1 = 0.0

  private var x2 = 0.0

  private var gradX1Value = gradX1(x1, x2)

  private var gradX2Value = gradX2(x1, x2)

  private var currentMin = function(x1, x2)

  private var currentIteration = 0

  private var isConverged = false

  private val deltaFun = { delta: Double ->
    function(x1 - delta * gradX1Value, x2 - delta * gradX2Value)
  }

  @PostConstruct
  override fun run() {
    (0..maxIterations).forEach {
      currentIteration = it
      currentMin = function(x1, x2)
      gradX1Value = gradX1(x1, x2)
      gradX2Value = gradX2(x1, x2)
      printState()

      if (hasConverged()) {
        logger.info("Found a solution after $currentIteration iterations")
        return
      }

      val delta = findNextDelta()
      logger.info("Delta($currentIteration) = $delta")
      x1 -= gradX1Value * delta
      x2 -= gradX2Value * delta
    }

    logger.info("Couldn't find a solution after $maxIterations iterations")
  }

  private fun hasConverged(): Boolean {
    val length = Math.sqrt(gradX1Value * gradX1Value + gradX2Value * gradX2Value)
    isConverged = length < epsilon

    logger.info("""
      Check on converged:
      $length < $epsilon = $isConverged
      """.trimIndent())
    return isConverged
  }

  private fun printState() {
    logger.info("""
      Iteration $currentIteration
      x$currentIteration: [$x1, $x2]
      function: $currentMin
      Grad(x$currentIteration): [$gradX1Value, $gradX2Value]
      """.trimIndent())
  }

  private fun findNextDelta(): Double {
    return 0.005
  }

}

fun main(args: Array<String>) {
  SpringApplication.run(GradientDescent::class.java, *args)
}