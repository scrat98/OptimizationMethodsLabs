package scrat98.github.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import scrat98.github.Algorithm
import scrat98.github.Section
import net.objecthunter.exp4j.ExpressionBuilder

@Configuration
@PropertySource("classpath:application.yaml")
class AppConfig(
    private val context: ApplicationContext,
    @Value("\${algorithms}") private val algorithmsNames: List<String>,
    @Value("\${initSection.x1}") private val initSectionX1: Double,
    @Value("\${initSection.x2}") private val initSectionX2: Double,
    @Value("\${epsilon}") private val epsilon: Double,
    @Value("\${function}") private val functionString: String
) {

  private val expressionBuilder = ExpressionBuilder(functionString)
      .variables("x")
      .build()

  @Bean
  fun epsilon() = epsilon

  @Bean
  fun initSection() = Section(initSectionX1, initSectionX2)

  @Bean
  fun algorithms(): Set<Algorithm> = context.getBeansOfType(Algorithm::class.java)
      .filter { (name, clazz) -> algorithmsNames.any { it == name } }
      .map { it.value }
      .toSet()

  @Bean
  fun function(): (Double) -> Double = { x -> expressionBuilder.setVariable("x", x).evaluate() }

}
