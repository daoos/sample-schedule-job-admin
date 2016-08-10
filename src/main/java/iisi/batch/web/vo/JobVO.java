package iisi.batch.web.vo;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class JobVO {

	@NotEmpty(message = "資料集ID必須輸入")
	@Size(max = 100, message = "資料集ID長度不可超過100")
	private String dataSetId;
	@NotEmpty(message = "JDBC驅動名稱必須輸入")
	private String driverClassName;
	@NotEmpty(message = "JDBC連線字串必須輸入")
	private String url;
	@NotEmpty(message = "JDBC使用者名稱必須輸入")
	private String username;
	@NotEmpty(message = "JDBC使用者密碼必須輸入")
	private String password;
	@NotEmpty(message = "查詢SQL必須輸入")
	private String sql;
	@Future(message = "開始執行時間必須大於現在時間")
	@DateTimeFormat(pattern = "yyyy/MM/dd-HH:mm")
	private Date startTime;
	private Integer intervalDay;
	@Size(max = 125, message = "描述內容長度不可超過125")
	private String description;
	
	public JobVO () {}

	public String getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(String dataSetId) {
		this.dataSetId = dataSetId;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getIntervalDay() {
		return intervalDay;
	}

	public void setIntervalDay(Integer intervalDay) {
		this.intervalDay = intervalDay;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
