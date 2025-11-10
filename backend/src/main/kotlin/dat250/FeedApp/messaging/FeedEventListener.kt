package dat250.FeedApp.messaging

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class FeedEventListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["\${feedapp.messaging.poll-created.queue}"])
    fun handlePollCreated(event: PollCreatedEvent) {
        logger.info("Received poll created event: {}", event)
    }

    @RabbitListener(queues = ["\${feedapp.messaging.vote-cast.queue}"])
    fun handleVoteCast(event: VoteCastEvent) {
        logger.info("Received vote cast event: {}", event)
    }
}
