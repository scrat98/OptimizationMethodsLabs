package scrat98.github

import mu.KLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct
import kotlin.system.measureNanoTime

@SpringBootApplication
class AlgorithmComparator(private val algorithms: Set<Algorithm>) : Runnable {

  companion object : KLogging()

  private val statistics = mutableMapOf<String, MutableList<Statistic>>()

  @PostConstruct
  override fun run() {
    logger.warn("Start comparing algorithms")
    algorithms.forEach { algorithm ->
      logger.warn("Calculate $algorithm algorithm")
      var stepNumber = 0
      while (!algorithm.isConverged()) {
        stepNumber++
        val time = measureNanoTime {
          algorithm.calculateNewSection()
        }
        val statistic = Statistic(
            stepNumber = stepNumber,
            initSection = algorithm.calculatedSection,
            newSection = algorithm.section,
            reduction = algorithm.calculatedSection.length() / algorithm.section.length(),
            points = algorithm.points,
            values = algorithm.values,
            time = time)
        statistics.computeIfAbsent(algorithm.toString()
        ) { mutableListOf() }.add(statistic)
      }
    }
    logger.warn(statistics.toString())
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(AlgorithmComparator::class.java, *args)
}
