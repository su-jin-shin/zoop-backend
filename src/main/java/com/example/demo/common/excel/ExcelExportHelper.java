package com.example.demo.common.excel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Component
public class ExcelExportHelper {

    public <T> ResponseEntity<ExcelResponseDto<T>> export(List<T> dtoList) {
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        log.info("📤 [엑셀 생성 완료] 소요 시간 = {} ms", (end - start));
        return ResponseEntity.ok(ExcelResponseDto.from(dtoList));
    }
}
