package com.example.springbatchstudy.config

import com.example.springbatchstudy.domin.Member
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class MyBatchConfig(
    private val entityManagerFactory: EntityManagerFactory,
): DefaultBatchConfiguration() {

    companion object {
        const val chuckSize = 3
        const val JOB_NAME = "MY_JOB"
        const val STEP_NAME = "MY_STEP"
    }

    @Bean(name = [JOB_NAME])
    fun myJob(jobRepository: JobRepository, @Qualifier("MY_STEP") step: Step): Job {
        return JobBuilder("myJob", jobRepository)
            .start(step)
            .build()
    }

    @Bean(name = [STEP_NAME])
    fun myStep(jobRepository: JobRepository, transactionManager: PlatformTransactionManager, entityManagerFactory: EntityManagerFactory): Step {
        return StepBuilder("myStep", jobRepository)
            .chunk<Member, String>(chuckSize, transactionManager)
            .reader(reader(null))
            .processor(processor(null))
            .writer(writer(null))
            .build()
    }

    @Bean
    @StepScope
    fun reader(@Value("#{jobParameters[requestDate]}") requestDate: String?): JpaPagingItemReader<Member> {
        println("==> reader: $requestDate")
        return JpaPagingItemReaderBuilder<Member>()
            .name("reader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(chuckSize)
            .queryString("SELECT m FROM Member m")
            .build()
    }

    @Bean
    @StepScope
    fun processor(@Value("#{jobParameters[requestDate]}") requestDate: String?): ItemProcessor <Member, String> {
        println("==> processor: $requestDate")
        return ItemProcessor<Member, String> { item: Member ->
            item.name
        }
    }

    @Bean
    @StepScope
    fun writer(@Value("#{jobParameters[requestDate]}") requestDate: String?): ItemWriter<String> {
        println("==> writer: $requestDate")
        return ItemWriter<String> { items ->
            for (item in items) {
                println("name: $item")
            }
        }
    }

}