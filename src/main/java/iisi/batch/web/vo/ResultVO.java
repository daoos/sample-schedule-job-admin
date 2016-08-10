package iisi.batch.web.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

public class ResultVO {
	
	private List<String> errors;
	
	public ResultVO() {
		this.errors = new ArrayList<>();
	}

	public boolean isSuccess() {
		return errors.isEmpty();
	}
	
	public void addError(String error) {
		this.errors.add(error);
	}
	
	public void addError(FieldError error) {
		this.errors.add(error.getDefaultMessage());
	}

	public List<String> getErrors() {
		return errors;
	}

}
