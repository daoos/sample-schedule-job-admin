package iisi.batch.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class Quartz {
	
	@Bean
    public JobFactory jobFactory() {
        return new SpringBeanJobFactory();
    }

	@Bean(name = "batch")
    public SchedulerFactoryBean schedulerFactoryBean(
    		DataSource dataSource, 
    		JobFactory jobFactory) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        
        Properties props = new Properties();
        props.setProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, "batch");
        props.setProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_ID, "auto");
        props.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.HSQLDBDelegate");
        props.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
//        factory.setQuartzProperties(props);
//        factory.setTriggers(sampleJobTrigger);

        return factory;
    }
	
}
