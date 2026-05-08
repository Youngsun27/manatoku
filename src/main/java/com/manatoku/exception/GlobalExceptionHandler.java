package com.manatoku.exception;

import com.manatoku.serviceModel.ServiceResult;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ServiceResult<Void> handleUserNotFound(MemberNotFoundException e) {
        return ServiceResult.fail(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ServiceResult<Void> handleDB(DataAccessException e) {
        e.printStackTrace();
        return ServiceResult.fail("500","データの取得に失敗しました。");
    }

    @ExceptionHandler(Exception.class)
    public ServiceResult<Void> handleException(Exception e) {
        e.printStackTrace();
        return ServiceResult.fail("500","エラーが発生しました。");
    }
}
