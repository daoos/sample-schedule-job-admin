package iisi.batch.config;

import java.io.File;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import iisi.batch.util.Utils;

@Configuration
public class WebMvc extends WebMvcConfigurerAdapter implements EmbeddedServletContainerCustomizer {

	@Bean
	public DataSource getDataSource() {
		return DataSourceBuilder
	            .create()
	            .driverClassName("org.hsqldb.jdbcDriver")
	            .url("jdbc:hsqldb:file:" + new File(Utils.getBase(), "db").getPath() + "/store")
	            .username("iisi")
	            .password("")
	            .build();
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		 registry.addViewController("/").setViewName("forward:/index.html");
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		if (container instanceof TomcatEmbeddedServletContainerFactory) {
			TomcatEmbeddedServletContainerFactory factory = (TomcatEmbeddedServletContainerFactory) container;
			factory.setBaseDirectory(new File(Utils.getBase(), Utils.runJar() ? "batch-work" : "target"));
		}
	}

}
