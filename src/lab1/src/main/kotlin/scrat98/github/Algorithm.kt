package scrat98.github

interface Algorithm {
  fun getNextSection(function: (Double) -> Double, section: Section): Section
}

data class Section(
    val x1: Double,
    val x2: Double
)

fun Section.isClosed(epsilon: Double): Boolean = x2 - x1 < epsilon