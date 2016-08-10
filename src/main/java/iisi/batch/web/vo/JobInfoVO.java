package iisi.batch.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JobInfoVO {

	private String dataSetId;
	private String description;
	@JsonFormat(pattern = "yyyy/MM/dd-HH:mm", timezone = "Asia/Taipei")
	private Date nextExcuteTime;
	
	public JobInfoVO(String dataSteId, String description, Date nextExcuteTime) {
		super();
		this.dataSetId = dataSteId;
		this.description = description;
		this.nextExcuteTime = nextExcuteTime;
	}

	public String getDataSetId() {
		return dataSetId;
	}

	public String getDescription() {
		return description;
	}

	public Date getNextExcuteTime() {
		return nextExcuteTime;
	}

}
