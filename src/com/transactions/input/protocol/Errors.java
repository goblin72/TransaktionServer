package com.transactions.input.protocol;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 19.04.13
 * Time: 20:24
 */
public enum Errors 
{
    UNKNOWN(1,"Техническая ошибка"),
    UNKNOWN_POINT(2,"Неизвестный иденфтикатор устройства"),
    UNKNOWN_COMPANY(3,"Не верный код компании"),
    UNKNOWN_PAYMENT_ID(4,"Не верный идентификатор платежа"),
    BANK_ERROR(5,"Банковский шлюз недоступен."),
    BANK_REJECT(6,"Платеж отклонен банком"),
    BAD_COMAPNY(7,"Прием платежей в пользу данной компнии не возможен"),
    BAD_POINT(8,"Прием платежей c данной точки не возможен");

    private int code;
    private String message;
    
    Errors(int i, String s) {
        code=i;
        message=s;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
