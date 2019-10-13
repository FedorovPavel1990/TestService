package org.example.enums;

public enum ResultCodes {

    FindNumber_00("00.Result.OK"),
    FindNumber_01("01.Result.NotFound", "Число не найдено ни в одном файле"),
    FindNumber_02("02.Result.Error", "Возникла техническая ошибка");

    private String code;
    private String error;

    private ResultCodes(String code, String error) {
        this.code = code;
        this.error = error;
    }

    private ResultCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

}
