package ru.otus.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.spring.models.rdbms.Author;
import ru.otus.spring.models.rdbms.Book;
import ru.otus.spring.models.rdbms.Genre;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@Slf4j
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    public static final String JOB_NAME = "databaseMigrationJob";

    private boolean crushJob;

    private final int batchSize;

    public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, @Value("${batchsize}") int batchSize,
                     @Value("${crushJob}") boolean crushJob) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.batchSize = batchSize;
        this.crushJob = crushJob;
    }

    @Bean
    public Job migrationJob(Step cleanDBStep, Step migrateGenresStep, Step migrateAuthorsStep, Step migrateBooksStep, Step crushJob) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(cleanDBStep)
                .next(crushJob)
                .next(migrateGenresStep)
                .next(migrateAuthorsStep)
                .next(migrateBooksStep)
                .build();
    }

    @Bean
    public Step cleanDBStep(MongoTemplate mongoTemplate) {
        return stepBuilderFactory.get("cleanDBStep")
                .tasklet((stepContribution, chunkContext) -> {
                    mongoTemplate.getDb().drop();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    //фейковый шаг для тестирования рестарта джоба
    @Bean
    public Step crushJob() {
        return stepBuilderFactory.get("crushJobStep")
                .tasklet((stepContribution, chunkContext) -> {
                    if (crushJob) {
                        throw new RuntimeException("Джоб поломался");
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


    @Bean
    public Step migrateGenresStep(ItemReader<Genre> genreReader, ItemProcessor<Genre, ru.otus.spring.models.mongo.Genre> genreProcessor, ItemWriter<ru.otus.spring.models.mongo.Genre> genreWriter) {
        return stepBuilderFactory.get("genreStep")
                .<Genre, ru.otus.spring.models.mongo.Genre>chunk(batchSize)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Genre> genreReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreReader")
                .pageSize(batchSize)
                .entityManagerFactory(entityManagerFactory)
                .queryString("from Genre")
                .build();
    }

    @Bean
    public ItemProcessor<Genre, ru.otus.spring.models.mongo.Genre> genreProcessor() {
        return (rdbmsGenre) -> new ModelMapper().map(rdbmsGenre, ru.otus.spring.models.mongo.Genre.class);
    }

    @Bean
    public MongoItemWriter<ru.otus.spring.models.mongo.Genre> genreWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<ru.otus.spring.models.mongo.Genre>()
                .collection("genres")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step migrateAuthorsStep(ItemReader<Author> authorReader, ItemProcessor<Author, ru.otus.spring.models.mongo.Author> authorProcessor,
                                   ItemWriter<ru.otus.spring.models.mongo.Author> authorWriter) {
        return stepBuilderFactory.get("authorStep")
                .<Author, ru.otus.spring.models.mongo.Author>chunk(batchSize)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Author> authorReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Author>()
                .name("authorReader")
                .pageSize(batchSize)
                .entityManagerFactory(entityManagerFactory)
                .queryString("from Author")
                .build();
    }

    @Bean
    public ItemProcessor<Author, ru.otus.spring.models.mongo.Author> authorProcessor() {
        return (rdbmsAuthor) -> new ModelMapper().map(rdbmsAuthor, ru.otus.spring.models.mongo.Author.class);
    }

    @Bean
    public MongoItemWriter<ru.otus.spring.models.mongo.Author> authorWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<ru.otus.spring.models.mongo.Author>()
                .collection("authors")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step migrateBooksStep(ItemReader<Book> bookReader, ItemProcessor<Book, ru.otus.spring.models.mongo.Book> bookProcessor,
                                 ItemWriter<ru.otus.spring.models.mongo.Book> bookWriter) {
        return stepBuilderFactory.get("bookStep")
                .<Book, ru.otus.spring.models.mongo.Book>chunk(batchSize)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Book> bookReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .pageSize(batchSize)
                .entityManagerFactory(entityManagerFactory)
                .queryString("from Book")
                .build();
    }

    @Bean
    public ItemProcessor<Book, ru.otus.spring.models.mongo.Book> bookProcessor() {
        return (rdbmsBook) -> new ModelMapper().map(rdbmsBook, ru.otus.spring.models.mongo.Book.class);
    }

    @Bean
    public MongoItemWriter<ru.otus.spring.models.mongo.Book> bookWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<ru.otus.spring.models.mongo.Book>()
                .collection("books")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    @Qualifier("myJobExplorer")
    public JobExplorer myJobExplorer(final DataSource dataSource) throws Exception {
        JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setDataSource(dataSource);
        jobExplorerFactoryBean.setTablePrefix("BATCH_");
        jobExplorerFactoryBean.setJdbcOperations(new JdbcTemplate(dataSource));
        jobExplorerFactoryBean.afterPropertiesSet();
        return jobExplorerFactoryBean.getObject();
    }

    @Bean
    public JobOperator jobOperator(final JobLauncher jobLauncher, final JobRepository jobRepository,
                                   final JobRegistry jobRegistry, final JobExplorer jobExplorer) {
        final SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.setJobExplorer(jobExplorer);
        return jobOperator;
    }

    public boolean isCrushJob() {
        return crushJob;
    }

    public void setCrushJob(boolean crushJob) {
        this.crushJob = crushJob;
    }


}
