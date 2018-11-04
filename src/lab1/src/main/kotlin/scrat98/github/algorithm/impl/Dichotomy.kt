package scrat98.github.algorithm.impl

import org.springframework.stereotype.Component
import scrat98.github.Algorithm
import scrat98.github.Section
import scrat98.github.center

@Component
class Dichotomy(function: (Double) -> Double,
                section: Section,
                epsilon: Double) : Algorithm(function, section, epsilon) {

  private val delta = epsilon.div(4)

  override fun calculateNewSectionImpl(): Section {
    x1 = section.center() - delta
    x2 = section.center() + delta
    return chooseSection()
  }

  private fun chooseSection(): Section {
    `fun(x1)` = function(x1)
    `fun(x2)` = function(x2)
    if (`fun(x1)` < `fun(x2)`) return Section(section.x1, x2)
    if (`fun(x1)` > `fun(x2)`) return Section(x1, section.x2)
    return Section(x1, x2)
  }

  override fun toString(): String {
    return "Dichotomy"
  }
}