package scrat98.github.algorithm.impl

import org.springframework.stereotype.Component
import scrat98.github.Algorithm
import scrat98.github.Section
import scrat98.github.center

@Component
class Dichotomy(private val epsilon: Double) : Algorithm() {
  override fun getNextSection(function: (Double) -> Double, section: Section): Section {
    val delta = epsilon.div(4)
    val x1 = section.center() - delta
    val x2 = section.center() + delta
    return choseSection(function, section, x1, x2)
  }

  override fun toString(): String {
    return "Dichotomy"
  }
}