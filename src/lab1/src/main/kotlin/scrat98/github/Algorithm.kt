package scrat98.github

abstract class Algorithm {
  abstract fun getNextSection(function: (Double) -> Double, section: Section): Section

  protected fun choseSection(function: (Double) -> Double, section: Section, x1: Double,
                             x2: Double): Section {
    val `fun(x1)` = function(x1)
    val `fun(x2)` = function(x2)
    if (`fun(x1)` < `fun(x2)`) return Section(section.x1, x2)
    if (`fun(x1)` > `fun(x2)`) return Section(x1, section.x2)
    return Section(x1, x2)
  }
}

data class Section(
    val x1: Double,
    val x2: Double
)

fun Section.center() = (x1 + x2) / 2

fun Section.isClosed(epsilon: Double): Boolean = length() < epsilon

fun Section.length() = x2 - x1

data class Statistic(
    val time: Long,
    val stepNumber: Int,
    val newSection: Section
)