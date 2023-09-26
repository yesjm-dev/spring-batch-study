package com.example.springbatchstudy

import com.example.springbatchstudy.config.MyBatchConfig
import com.example.springbatchstudy.domin.Member
import com.example.springbatchstudy.domin.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.StepExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import java.time.OffsetDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBatchTest
@SpringBootTest(
    classes = [MyBatchConfig::class],
    properties = ["spring.profiles.active=test"],
)
@EnableAutoConfiguration
class SpringBatchStudyApplicationTests(
    @Autowired
    private val jobLauncherTestUtils: JobLauncherTestUtils, // launchJob, laucnStep()과 같은 스프링 배치 테스트에 필요한 유틸성 메서드 지원
    @Autowired
    private val memberRepository: MemberRepository
) {
    @BeforeAll
    fun setUp() {
        memberRepository.saveAll(
            listOf(
                Member(name = "김민지", age = 14),
                Member(name = "김민수", age = 14),
                Member(name = "김민주", age = 14),
                Member(name = "김민석", age = 14),
                Member(name = "김민정", age = 14),
            )
        )
    }

    @Test
    fun `myJob 실행 테스트`() {
        // given
        val jobParameters = JobParametersBuilder()
            .addString("requestDate", OffsetDateTime.now().toString())
            .addString("jobName", "myJob")
            .toJobParameters()

        // when
        val jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        // then
        assertThat(jobExecution.status).isEqualTo(BatchStatus.COMPLETED)
        assertThat(jobExecution.exitStatus.exitCode).isEqualTo(BatchStatus.COMPLETED.toString())
    }

    @Test
    fun `myStep 실행 테스트`() {
        // given
        val jobExecution = jobLauncherTestUtils.launchStep("myStep")

        // when
        val stepExecution: StepExecution = (jobExecution.stepExecutions as List<*>)[0] as StepExecution

        // then
        assertThat(stepExecution.status).isEqualTo(BatchStatus.COMPLETED)
        assertThat(stepExecution.exitStatus.exitCode).isEqualTo(BatchStatus.COMPLETED.toString())
    }

}
