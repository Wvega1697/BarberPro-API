package com.wvega.barberproapi.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWS {
    private int code;
    private boolean success;
    private String message;
    private Object data;

    public ResponseWS successResponse(String message, Object data) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.code = 200;
        return this;
    }

    public ResponseWS errorResponse(String message) {
        this.success = false;
        this.message = message;
        this.code = 500;
        return this;
    }

    public ResponseWS failResponse(String message, Object data) {
        this.success = false;
        this.message = message;
        this.data = data;
        this.code = 400;
        return this;
    }

}
