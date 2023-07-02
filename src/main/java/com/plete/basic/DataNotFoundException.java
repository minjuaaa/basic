package com.plete.basic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    //DataNotFoundException 클래스가 직렬화되는 동안에 필요한 필드
    //명시적으로 선언하여 클래스의 버전 관리를 제어할 수 있으며,
    //이를 통해 역직렬화 시의 호환성을 보장할 수 있음

    public DataNotFoundException(String message) {
        super(message);

    }
}
