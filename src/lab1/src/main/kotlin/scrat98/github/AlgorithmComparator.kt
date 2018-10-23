package scrat98.github

import mu.KLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct
import kotlin.system.measureTimeMillis

@SpringBootApplication
class AlgorithmComparator(private val algorithms: Set<Algorithm>,
                          private val initSection: Section,
                          private val epsilon: Double,
                          private val function: (Double) -> Double) : Runnable {

  companion object : KLogging()

  private val statistics = mutableMapOf<String, MutableList<Statistic>>()

  @PostConstruct
  override fun run() {
    logger.warn("Start comparing algorithms")
    algorithms.forEach {
      val algorithmName = it.javaClass.toString()
      logger.warn("Calculate $algorithmName algorithm")
      var currentSection = initSection
      var stepNumber = 0
      while (!currentSection.isClosed(epsilon)) {
        stepNumber++
        val time = measureTimeMillis {
          currentSection = it.getNextSection(function, initSection)
        }
        val statistic = Statistic(time, stepNumber, currentSection)
        statistics[algorithmName]?.add(statistic) ?: statistics.putIfAbsent(algorithmName,
            mutableListOf(statistic))
      }
    }
    logger.warn(statistics.toString())
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(AlgorithmComparator::class.java, *args)
}
