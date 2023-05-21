package com.example.springbatchstudy.scheduler

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@EnableScheduling
@Component
class MyScheduler {
    @Autowired lateinit var jobLauncher: JobLauncher
    @Autowired lateinit var job: Job

    @Scheduled(fixedDelay = 30000)
    fun startJob() {
        val jobParameterMap = mapOf("requestDate" to JobParameter(OffsetDateTime.now().toString(), String::class.java))
        val jobParameters = JobParameters(jobParameterMap)
        val jobExecution: JobExecution = jobLauncher.run(job, jobParameters)

        while (jobExecution.isRunning) {
            println("isRunning....")
        }
    }
}