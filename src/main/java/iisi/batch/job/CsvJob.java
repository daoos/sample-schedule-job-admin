package iisi.batch.job;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.opencsv.CSVWriter;

import iisi.batch.util.Utils;

public class CsvJob implements Job {
	
	public final static String GROUP = "batch";
	
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getMergedJobDataMap();
		SingleConnectionDataSource ds = new SingleConnectionDataSource();
		final File root = Utils.getOutputFolder(context.getJobDetail().getKey().getName());
		final String randomName = randomName();
		final File csv = new File(root, randomName + ".csv");
		try {
			ds.setDriverClassName(map.getString(JobConstant.DATABASE_DRIVER_CLASS_NAME));
			ds.setUrl(map.getString(JobConstant.DATABASE_URL));
			ds.setUsername(map.getString(JobConstant.DATABASE_USERNAME));
			ds.setPassword(map.getString(JobConstant.DATABASE_PASSWORD));
			JdbcTemplate template = new JdbcTemplate(ds);
			template.query(map.getString(JobConstant.SQL), new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet resultSet) throws SQLException {
					try (CSVWriter writer = new CSVWriter(
							new BufferedWriter(new OutputStreamWriter(
									new FileOutputStream(csv, false), 
									Charset.forName("UTF-8").newEncoder())) {
								{
									super.write('\ufeff');//add UTF-8 BOM
								}
							}, 
							',')) {
						writer.writeAll(resultSet, true);
					} catch (IOException e) {
						throw new RuntimeException(e); 
					}
				}
			});
		} catch (Throwable t) {
			FileUtils.deleteQuietly(csv);
			try {
				FileUtils.writeStringToFile(new File(root, randomName + ".err"), ExceptionUtils.getStackTrace(t), "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		ds.destroy();
	}
	
	private String randomName() {
		return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
	}

}
