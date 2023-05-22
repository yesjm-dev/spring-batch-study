package com.example.springbatchstudy.scheduler

import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@EnableScheduling
@Component
class MyScheduler(
    private val jobRegistry: JobRegistry,
    private val jobLauncher: JobLauncher
) {
    @Bean
    fun jobRegistryBeanPostProcessor(): JobRegistryBeanPostProcessor {
        val postProcessor = JobRegistryBeanPostProcessor()
        postProcessor.setJobRegistry(jobRegistry)
        return postProcessor
    }

    @Scheduled(fixedDelay = 30000)
    fun startMyJob() {
        println("======= my job ========")
        val parameter = mapOf(
            "requestDate" to JobParameter(OffsetDateTime.now().toString(), String::class.java),
            "jobName" to JobParameter("myJob", String::class.java)
        )
        val jobParameters = JobParameters(parameter)
        jobLauncher.run(jobRegistry.getJob("myJob"), jobParameters)
    }

    @Scheduled(fixedDelay = 50000)
    fun startOtherJob() {
        println("======= other job ========")
        val parameter = mapOf(
            "requestDate" to JobParameter(OffsetDateTime.now().toString(), String::class.java),
            "jobName" to JobParameter("otherJob", String::class.java)
        )
        val jobParameters = JobParameters(parameter)
        jobLauncher.run(jobRegistry.getJob("otherJob"), jobParameters)
    }

}