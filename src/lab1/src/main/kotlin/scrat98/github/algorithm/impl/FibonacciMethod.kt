package scrat98.github.algorithm.impl

import org.springframework.stereotype.Component
import scrat98.github.Algorithm
import scrat98.github.Section
import scrat98.github.length

@Component
class FibonacciMethod(function: (Double) -> Double,
                      section: Section,
                      epsilon: Double) : Algorithm(function, section, epsilon) {
  private fun fibonacciSequence(): Sequence<Long> {
    return generateSequence(Pair<Long, Long>(1, 1)) {
      Pair(it.second, it.first + it.second)
    }.map { it.first }
  }

  private val fibonacciArray = {
    val lastIndex = fibonacciSequence().indexOfFirst { it > section.length() / epsilon }
    fibonacciSequence().take(lastIndex + 1).toList()
  }()

  private var k = 1

  private val n = fibonacciArray.size - 1

  private val deltaX1
    get() = fibonacciArray[n - k - 1].toDouble() / fibonacciArray[n - k + 1].toDouble() * section.length()

  private val deltaX2
    get() = fibonacciArray[n - k].toDouble() / fibonacciArray[n - k + 1].toDouble() * section.length()

  private var preCalculate = {}

  override fun calculateNewSectionImpl(): Section? {
    if (k > n - 1) return null
    preCalculate()
    updatePoints()
    return choseSection().also { k++ }
  }

  private fun updatePoints() {
    if (x1.isNaN()) {
      x1 = section.x1 + deltaX1
      `fun(x1)` = function(x1)
    }

    if (x2.isNaN()) {
      x2 = section.x1 + deltaX2
      `fun(x2)` = function(x2)
    }
  }

  private fun choseSection(): Section {
    return if (`fun(x1)` > `fun(x2)`) {
      preCalculate = resetX2
      Section(x1, section.x2)
    } else {
      preCalculate = resetX1
      Section(section.x1, x2)
    }
  }

  private val resetX1 = {
    x2 = x1
    `fun(x2)` = `fun(x1)`
    x1 = Double.NaN
    `fun(x1)` = Double.NaN
  }

  private val resetX2 = {
    x1 = x2
    `fun(x1)` = `fun(x2)`
    x2 = Double.NaN
    `fun(x2)` = Double.NaN
  }

  override fun toString(): String {
    return "FibonacciMethod"
  }
}