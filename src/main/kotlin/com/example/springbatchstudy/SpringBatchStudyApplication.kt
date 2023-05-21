package com.example.springbatchstudy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example", "jakarta.persistence"])
class SpringBatchStudyApplication

fun main(args: Array<String>) {
    runApplication<SpringBatchStudyApplication>(*args)
}
