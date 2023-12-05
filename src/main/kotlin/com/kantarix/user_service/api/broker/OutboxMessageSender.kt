package com.kantarix.user_service.api.broker

import com.kantarix.user_service.api.repositories.OutboxMessageRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class OutboxMessageSender(
    private val outboxMessageRepository: OutboxMessageRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    @Scheduled(fixedRate = 3000)
    @Transactional
    fun send() {
        val sentMessageIds = outboxMessageRepository.findAll().map {
            kafkaTemplate.send(it.topic, it.message)
            it.id
        }
        outboxMessageRepository.deleteAllById(sentMessageIds)
    }
}