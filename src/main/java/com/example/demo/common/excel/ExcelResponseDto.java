package com.example.demo.common.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExcelResponseDto<T> {

    private final List<T> excelData;

    public ExcelResponseDto(List<T> excelData) {
        this.excelData = excelData == null ? null : new ArrayList<>(excelData);
    }

    public List<T> getExcelData() {
        return excelData == null ? null : new ArrayList<>(excelData);
    }

    public static <T> ExcelResponseDto<T> from(List<T> dtoList) {
        return new ExcelResponseDto<>(dtoList);
    }
}