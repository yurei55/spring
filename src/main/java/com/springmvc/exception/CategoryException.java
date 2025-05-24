package com.springmvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="요청한 도서 분야를 찾을 수 없습니다.")
public class CategoryException extends RuntimeException {
	public CategoryException() {
		super();
	}

}
