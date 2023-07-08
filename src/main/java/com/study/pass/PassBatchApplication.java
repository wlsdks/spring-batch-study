package com.study.pass;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PassBatchApplication {

	// springBoot3.x.x 부터는 아래의 injection이 안먹힌다.
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public PassBatchApplication(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	// step 선언
	@Bean
	public Step passStep() {
		return this.stepBuilderFactory.get("passStep")
				.tasklet(new Tasklet() {
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Execute PassStep");
						return RepeatStatus.FINISHED; //종료
					}
				}).build();
	}

	// 만든 step을 사용하여 job 구성
	@Bean
	public Job passJob() {
		return this.jobBuilderFactory.get("passJob")
				.start(passStep())
				.build();
	}


	public static void main(String[] args) {
		SpringApplication.run(PassBatchApplication.class, args);
	}

}
