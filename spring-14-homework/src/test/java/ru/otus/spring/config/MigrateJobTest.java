package ru.otus.spring.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static ru.otus.spring.config.JobConfig.JOB_NAME;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.models.mongo.Author;
import ru.otus.spring.models.mongo.Book;
import ru.otus.spring.models.mongo.Genre;


@SpringBootTest
@SpringBatchTest
class MigrateJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobConfig jobConfig;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("Миграция библиотеки должна работать")
    @Test
    void migrateLibrary() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(JOB_NAME);


        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        Assert.assertEquals(2, mongoTemplate.findAll(Genre.class, "genres").size());
        Assert.assertEquals(3, mongoTemplate.findAll(Author.class, "authors").size());
        Assert.assertEquals(1, mongoTemplate.findAll(Book.class, "books").size());
    }

    @DisplayName("Миграция библиотеки должна ломаться при включенном crushJob")
    @Test
    void migrateWithCrushJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(JOB_NAME);
        jobConfig.setCrushJob(true);
        jobLauncherTestUtils.launchJob();
        Assert.assertEquals(0, mongoTemplate.findAll(Genre.class, "genres").size());
        Assert.assertEquals(0, mongoTemplate.findAll(Author.class, "authors").size());
        Assert.assertEquals(0, mongoTemplate.findAll(Book.class, "books").size());

        jobConfig.setCrushJob(false);
        jobLauncherTestUtils.launchJob();

        Assert.assertEquals(2, mongoTemplate.findAll(Genre.class, "genres").size());
        Assert.assertEquals(3, mongoTemplate.findAll(Author.class, "authors").size());
        Assert.assertEquals(1, mongoTemplate.findAll(Book.class, "books").size());


    }

}
