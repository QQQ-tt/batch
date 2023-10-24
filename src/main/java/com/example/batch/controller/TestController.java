package com.example.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * @author qtx
 * @since 2023/10/18
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private final JobLauncher jobLauncher;

    private final Job job;

    public TestController(JobLauncher jobLauncher, @Qualifier("footballJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @RequestMapping("/handle")
    public void handle() {
        JobParameters parameters =
                new JobParametersBuilder().addLocalDateTime("time", LocalDateTime.now())
//                        .addString("name", "Qtx")
                        .toJobParameters();

        CompletableFuture.runAsync(()-> {
            try {
                jobLauncher.run(job, parameters);
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                     JobParametersInvalidException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
