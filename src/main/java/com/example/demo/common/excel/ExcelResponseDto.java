package com.example.demo.common.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelResponseDto<T> {

    private List<T> excelData;

    public static <T> ExcelResponseDto<T> from(List<T> data) {
        return new ExcelResponseDto<>(data);
    }
}