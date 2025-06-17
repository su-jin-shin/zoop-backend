package com.example.demo.common.excel;

import java.util.List;

public interface ExcelExportService<T> {
    List<T> getExcelData(Long userId);
}
