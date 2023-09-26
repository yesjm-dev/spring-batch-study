package com.example.springbatchstudy

import com.example.springbatchstudy.config.MyBatchConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import java.time.OffsetDateTime

@SpringBatchTest
@SpringBootTest(
    classes = [MyBatchConfig::class],
    properties = ["spring.profiles.active=test"],
)
@EnableAutoConfiguration
class SpringBatchStudyApplicationTests(
    @Autowired
    private val jobLauncherTestUtils: JobLauncherTestUtils, // launchJob, laucnStep()과 같은 스프링 배치 테스트에 필요한 유틸성 메서드 지원
) {

    @Test
    fun `myJob 실행 테스트`() {
        // given
        val jobParameters = JobParametersBuilder()
            .addString("requestDate", OffsetDateTime.now().toString())
            .addString("jobName", "test")
            .toJobParameters()

        // when
        val jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        // then
        assertThat(jobExecution.status).isEqualTo(BatchStatus.COMPLETED)
        assertThat(jobExecution.exitStatus.exitCode).isEqualTo(BatchStatus.COMPLETED.toString())
    }

}
