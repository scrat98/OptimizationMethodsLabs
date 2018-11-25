package scrat98.github.config

import net.objecthunter.exp4j.ExpressionBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.yaml")
class AppConfig(
    @Value("\${function}") private val functionString: String,
    @Value("\${gradX1}") private val gradX1: String,
    @Value("\${gradX2}") private val gradX2: String,
    @Value("\${epsilon}") private val epsilon: Double,
    @Value("\${maxIterations}") private val maxIterations: Int
) {

  private fun expressionBuilder(function: String) = ExpressionBuilder(function)
      .variables("x1", "x2")
      .build()

  private fun functionBuilder(function: String): (Double, Double) -> Double =
      { x1: Double, x2: Double ->
        expressionBuilder(function).setVariables(mapOf("x1" to x1, "x2" to x2)).evaluate()
      }

  @Bean
  fun epsilon() = epsilon

  @Bean
  fun maxIterations() = maxIterations

  @Bean
  fun function(): (Double, Double) -> Double = functionBuilder(functionString)

  @Bean
  fun gradX1(): (Double, Double) -> Double = functionBuilder(gradX1)

  @Bean
  fun gradX2(): (Double, Double) -> Double = functionBuilder(gradX2)
}