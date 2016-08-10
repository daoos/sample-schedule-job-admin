package iisi.batch.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iisi.batch.job.CsvJob;
import iisi.batch.job.JobConstant;
import iisi.batch.web.vo.JobInfoVO;
import iisi.batch.web.vo.JobVO;
import iisi.batch.web.vo.ResultVO;

@RestController
@RequestMapping(path = "/jobs")
public class JobsRestful {

	@Autowired
	private Scheduler scheduler;

	@RequestMapping(method = RequestMethod.GET)
	public List<JobInfoVO> getJobs() throws SchedulerException {
		List<JobInfoVO> result = new ArrayList<JobInfoVO>();
		for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyJobGroup())) {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			result.add(new JobInfoVO(
					jobKey.getName(), 
					jobDetail.getDescription(), 
					triggers.isEmpty() ? null : triggers.get(0).getNextFireTime()));
		}
		return result;
	}

	@RequestMapping(path = "{dataSetId}", method = RequestMethod.DELETE)
	public boolean deleteJobs(@PathVariable String dataSetId) throws SchedulerException {
		return scheduler.deleteJob(new JobKey(dataSetId, CsvJob.GROUP));
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResultVO newJob(@Valid JobVO jobVO, BindingResult bResult) throws SchedulerException {
		ResultVO result = new ResultVO();
		if (bResult.hasErrors()) {
			for (FieldError fieldError : bResult.getFieldErrors()) {
				result.addError(fieldError);
			}
			return result;
		} else if (scheduler.getJobDetail(new JobKey(jobVO.getDataSetId(), CsvJob.GROUP)) != null) {
			result.addError("job 已存在");
			return result;
		} else if (!validateSQL(jobVO)) {
			result.addError("無法執行 sql");
			return result;
		}

		JobDataMap map = new JobDataMap();
		map.put(JobConstant.DATABASE_DRIVER_CLASS_NAME, jobVO.getDriverClassName());
		map.put(JobConstant.DATABASE_URL, jobVO.getUrl());
		map.put(JobConstant.DATABASE_USERNAME, jobVO.getUsername());
		map.put(JobConstant.DATABASE_PASSWORD, jobVO.getPassword());
		map.put(JobConstant.SQL, jobVO.getSql());

		JobDetail job = JobBuilder.newJob(CsvJob.class).withIdentity(jobVO.getDataSetId(), CsvJob.GROUP)
				.withDescription(jobVO.getDescription()).setJobData(map).build();

		TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().forJob(job);
		if (jobVO.getIntervalDay() != null) {
			triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(jobVO.getIntervalDay() * 24));
		}
		if (jobVO.getStartTime() != null) {
			triggerBuilder.startAt(jobVO.getStartTime());
		} else {
			triggerBuilder.startNow();
		}

		scheduler.scheduleJob(job, triggerBuilder.build());

		return result;
	}

	private boolean validateSQL(JobVO jobVO) {
		SingleConnectionDataSource ds = new SingleConnectionDataSource();
		try {
			ds.setDriverClassName(jobVO.getDriverClassName());
			ds.setUrl(jobVO.getUrl());
			ds.setUsername(jobVO.getUsername());
			ds.setPassword(jobVO.getPassword());
			JdbcTemplate template = new JdbcTemplate(ds);
			template.execute(jobVO.getSql());
			return true;
		} catch (Throwable t) {
			return false;
		} finally {
			ds.destroy();
		}
	}

}
