package scrat98.github

import mu.KLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct
import kotlin.system.measureNanoTime
import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import java.io.File

@SpringBootApplication
class AlgorithmComparator(private val algorithms: Set<Algorithm>) : Runnable {

  companion object : KLogging()

  private val statistics = mutableMapOf<String, Pair<Double, MutableList<Statistic>>>()

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
        ) { algorithm.epsilon to mutableListOf() }.second.add(statistic)
      }
    }
    printStatistics()
  }

  private fun printStatistics() {
    logger.info("Writing statistics in files")
    statistics.forEach { (algorithm, statistics) ->
      val at = AsciiTable()
      at.addRule()
      at.addRow(
          "stepNumber",
          "initSection",
          "newSection",
          "reduction",
          "points",
          "values",
          "time")
      at.addRule()
      statistics.second.forEach { stat ->
        at.addRow(
            "${stat.stepNumber}",
            "${stat.initSection}",
            "${stat.newSection}",
            "${stat.reduction}",
            "${stat.points}",
            "${stat.values}",
            "${stat.time}")
            .setTextAlignment(TextAlignment.LEFT)
        at.addRule()
      }
      val epsilon = statistics.first
      val time: Long = statistics.second.fold<Statistic, Long>(0) { acc, it -> acc + it.time }
      val steps = statistics.second.last().stepNumber
      File("results/lab1/$algorithm.txt").also { it.parentFile.mkdirs() }.printWriter().use { out ->
        out.println("epsilon $epsilon steps $steps time $time ns")
        out.println(at.render())
      }
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(AlgorithmComparator::class.java, *args)
}
