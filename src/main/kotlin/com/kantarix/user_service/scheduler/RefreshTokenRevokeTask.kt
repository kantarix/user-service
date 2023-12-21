package com.kantarix.user_service.scheduler

import com.kantarix.user_service.api.services.RefreshTokenService
import mu.KotlinLogging
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RefreshTokenRevokeTask(
    val refreshTokenService: RefreshTokenService,
) {

    private val log = KotlinLogging.logger {  }

    @Scheduled(cron = "0 0 0 * * *")
    @SchedulerLock(name = "RefreshTokenRevokeTask")
    fun refreshTokenRevokeTask() {
        refreshTokenService.deleteAllExpiredTokens()
        log.info { "RefreshTokenRevokeTask executed." }
    }

}