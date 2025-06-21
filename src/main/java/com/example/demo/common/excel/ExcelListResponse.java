package com.example.demo.common.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ExcelListResponse<T> {

    private int countProperties;
    private List<T> data;

}
