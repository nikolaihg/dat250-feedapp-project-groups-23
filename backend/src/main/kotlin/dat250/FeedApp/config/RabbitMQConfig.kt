package dat250.FeedApp.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableRabbit
class RabbitMQConfig(
        @Value("\${feedapp.messaging.exchange}") private val exchangeName: String,
        @Value("\${feedapp.messaging.poll-created.queue}") private val pollCreatedQueueName: String,
        @Value("\${feedapp.messaging.poll-created.routing-key}") private val pollCreatedRoutingKey: String,
        @Value("\${feedapp.messaging.vote-cast.queue}") private val voteCastQueueName: String,
        @Value("\${feedapp.messaging.vote-cast.routing-key}") private val voteCastRoutingKey: String
) {

    @Bean
    fun feedAppExchange(): TopicExchange = TopicExchange(exchangeName, true, false)

    @Bean
    fun pollCreatedQueue(): Queue = QueueBuilder.durable(pollCreatedQueueName).build()

    @Bean
    fun voteCastQueue(): Queue = QueueBuilder.durable(voteCastQueueName).build()

    @Bean
    fun pollCreatedBinding(): Binding =
            BindingBuilder.bind(pollCreatedQueue())
                    .to(feedAppExchange())
                    .with(pollCreatedRoutingKey)

    @Bean
    fun voteCastBinding(): Binding =
            BindingBuilder.bind(voteCastQueue()).to(feedAppExchange()).with(voteCastRoutingKey)

    @Bean
    fun jacksonMessageConverter(): Jackson2JsonMessageConverter = Jackson2JsonMessageConverter()

    @Bean
    fun rabbitTemplate(
            connectionFactory: ConnectionFactory,
            messageConverter: Jackson2JsonMessageConverter
    ): RabbitTemplate =
            RabbitTemplate(connectionFactory).apply { setMessageConverter(messageConverter) }
}
