package scrat98.github.algorithm.impl

import org.springframework.stereotype.Component
import scrat98.github.Algorithm
import scrat98.github.Section

@Component
class GoldenSectionSearch : Algorithm {
  override fun getNextSection(function: (Double) -> Double, section: Section): Section {
    return Section(0.0, 0.0)
  }
}