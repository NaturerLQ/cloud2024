package com.atguigu.cloud.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultData<T> {

    private String code;
    private String message;
    private T data;
    private long timeStamp;

    //接口调用时间
    public ResultData() {
        this.timeStamp = System.currentTimeMillis();
    }

    //和泛型有关的基础知识
    public static <T> ResultData<T> success(T data) {
        ResultData resultData = new ResultData<>();

        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMessage(ReturnCodeEnum.RC200.getMessage());
        resultData.setData(data);

        return resultData;
    }

    public static <T> ResultData<T> fail(String code, String message) {
        ResultData resultData = new ResultData<>();

        resultData.setCode(code);
        resultData.setMessage(message);
        resultData.setData(null);

        return resultData;
    }
}
