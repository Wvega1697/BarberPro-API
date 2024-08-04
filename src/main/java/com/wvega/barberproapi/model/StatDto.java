package com.wvega.barberproapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

import static com.wvega.barberproapi.utils.Constants.*;

@Data
@AllArgsConstructor
public class StatDto {

    private String methodName;
    private Long executionTime;
    private String processId;
    private Long dateMillis;
    private String status;

    public static StatDto fromMap(Map<String, Object> map) {
        return new StatDto(
                map.get(METHOD_NAME).toString(),
                Long.parseLong(map.get(EXECUTION_TIME).toString()),
                map.get(PROCESSID).toString(),
                Long.parseLong(map.get(DATEMILLIS).toString()),
                map.get(STATUS).toString()
        );
    }

}
