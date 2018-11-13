package scrat98.github

abstract class Algorithm(val function: (Double) -> Double,
                         initSection: Section,
                         val epsilon: Double) {

  var section = initSection
    private set

  // Data for calculated section
  var calculatedSection: Section = initSection
    private set

  var x1: Double = Double.NaN
    protected set

  var x2: Double = Double.NaN
    protected set

  var `fun(x1)`: Double = Double.NaN
    protected set

  var `fun(x2)`: Double = Double.NaN
    protected set

  val points: Pair<Double, Double>
    get() = Pair(x1, x2)

  val values: Pair<Double, Double>
    get() = Pair(`fun(x1)`, `fun(x2)`)

  protected abstract fun calculateNewSectionImpl(): Section?

  fun calculateNewSection(): Section = calculateNewSectionImpl()
      ?.also {
        calculatedSection = section
        section = it
      } ?: section

  fun calculateFunctionMinimum(): Double {
    while (!isConverged()) {
      calculateNewSection()
    }
    return getCurrentValue()
  }

  fun getCurrentArgument() = section.center()

  fun getCurrentValue() = function(getCurrentArgument())

  fun isConverged() = section.isClosed(epsilon)
}

data class Section(
    val x1: Double,
    val x2: Double
)

fun Section.center() = (x1 + x2) / 2

fun Section.isClosed(epsilon: Double): Boolean = length() < epsilon

fun Section.length() = x2 - x1

data class Statistic(
    val stepNumber: Int,
    val initSection: Section,
    val newSection: Section,
    val reduction: Double,
    val points: Pair<Double, Double>,
    val values: Pair<Double, Double>,
    val time: Long
)