package com.kantarix.user_service.api.events

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("USER_DELETED")
data class UserDeletedDomainEvent(
    val userId: Int,
) : DomainEvent(type = DomainEventType.USER_DELETED)