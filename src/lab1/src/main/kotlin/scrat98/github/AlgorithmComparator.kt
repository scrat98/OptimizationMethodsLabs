package scrat98.github

import mu.KLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct

@SpringBootApplication
class AlgorithmComparator(private val algorithms: Set<Algorithm>,
                          private val initSection: Section,
                          private val epsilon: Double,
                          private val function: (Double) -> Double) : Runnable {

  companion object : KLogging()

  @PostConstruct
  override fun run() {
    logger.warn("Start comparing algorithms")
    algorithms.forEach {
      logger.warn("Calculate ${it.javaClass} algorithm")
      var currentSection = initSection
      while (!currentSection.isClosed(epsilon)) {
        currentSection = it.getNextSection(function, initSection)
      }
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(AlgorithmComparator::class.java, *args)
}
