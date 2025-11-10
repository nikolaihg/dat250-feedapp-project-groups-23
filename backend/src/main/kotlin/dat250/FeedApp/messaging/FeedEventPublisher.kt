package dat250.FeedApp.messaging

import java.time.Instant
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FeedEventPublisher(
        private val rabbitTemplate: RabbitTemplate,
        @Value("\${feedapp.messaging.exchange}") private val exchange: String,
        @Value("\${feedapp.messaging.poll-created.routing-key}")
        private val pollCreatedRoutingKey: String,
        @Value("\${feedapp.messaging.vote-cast.routing-key}")
        private val voteCastRoutingKey: String
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun publishPollCreated(event: PollCreatedEvent) {
        rabbitTemplate.convertAndSend(exchange, pollCreatedRoutingKey, event)
        logger.info("Published poll created event for poll {}", event.pollId)
    }

    fun publishVoteCast(event: VoteCastEvent) {
        rabbitTemplate.convertAndSend(exchange, voteCastRoutingKey, event)
        logger.info("Published vote cast event for poll {}", event.pollId)
    }
}

data class PollCreatedEvent(
        val pollId: Long,
        val question: String,
        val publishedAt: Instant,
        val validUntil: Instant,
        val creator: String,
        val voteOptions: List<String>
)

data class VoteCastEvent(
        val pollId: Long,
        val voteOption: String,
        val voter: String,
        val publishedAt: Instant
)
