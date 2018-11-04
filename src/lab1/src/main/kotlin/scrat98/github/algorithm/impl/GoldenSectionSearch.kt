package scrat98.github.algorithm.impl

import org.springframework.stereotype.Component
import scrat98.github.Algorithm
import scrat98.github.Section
import scrat98.github.length
import kotlin.math.sqrt

@Component
class GoldenSectionSearch(function: (Double) -> Double,
                          section: Section,
                          epsilon: Double) : Algorithm(function, section, epsilon) {
  private val phi = (1 + sqrt(5.0)) / 2

  private val delta
    get() = section.length() / phi

  private var preCalculate = {}

  override fun calculateNewSectionImpl(): Section {
    preCalculate()
    updatePoints()
    return choseSection()
  }

  private fun updatePoints() {
    if (x1.isNaN()) {
      x1 = section.x2 - delta
      `fun(x1)` = function(x1)
    }

    if (x2.isNaN()) {
      x2 = section.x1 + delta
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
    return "GoldenSectionSearch"
  }
}