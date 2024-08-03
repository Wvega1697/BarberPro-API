package com.wvega.barberproapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListData {

    private int page;
    private int size;
    private long total;
    private List<Object> data;

}
