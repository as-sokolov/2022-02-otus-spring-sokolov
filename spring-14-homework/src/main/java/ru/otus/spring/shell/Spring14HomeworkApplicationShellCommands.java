package ru.otus.spring.shell;

import static ru.otus.spring.config.JobConfig.JOB_NAME;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.config.JobConfig;

@ShellComponent
public class Spring14HomeworkApplicationShellCommands {

    private final JobConfig jobConfig;
    private final JobExplorer jobExplorer;
    private final JobOperator jobOperator;

    public Spring14HomeworkApplicationShellCommands(JobConfig jobConfig, JobLauncher migationJobLauncher, @Qualifier("myJobExplorer") JobExplorer jobExplorer,
                                                    JobOperator jobOperator) {
        this.jobConfig = jobConfig;
        this.jobExplorer = jobExplorer;
        this.jobOperator = jobOperator;
    }

    @ShellMethod(value = "migrate database", key = {"md"})
    public void migrateDatabase() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobException, JobParametersNotFoundException {
        jobOperator.startNextInstance(JOB_NAME);
    }

    @ShellMethod(value = "restart migrate database", key = {"rmd"})
    public void restartMigrateDatabase() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException {

        JobInstance jobInstance = jobExplorer.getLastJobInstance(JOB_NAME);
        if (jobInstance == null) {
            System.out.println("Не найден джоб для продолжения, используйте команду md");
            return;
        }
        JobExecution jobExecution = jobExplorer.getLastJobExecution(jobInstance);
        if (jobExecution == null) {
            System.out.println("Не найден джоб для продолжения, используйте команду md");
            return;
        }
        jobOperator.restart(jobExecution.getId());
    }

    @ShellMethod(value = "toggle crushJob", key = {"tg"})
    public void toggleCrushJob() {
        jobConfig.setCrushJob(!jobConfig.isCrushJob());
        System.out.println("JobConfig.isCrushJob was set to " + jobConfig.isCrushJob());
    }
}
