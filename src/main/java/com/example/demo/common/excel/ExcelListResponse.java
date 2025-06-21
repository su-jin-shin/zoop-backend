package com.example.demo.common.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class ExcelListResponse<T> {

    private int countProperties;
    private final List<T> data;

    public ExcelListResponse(int countProperties, List<T> data) {
        this.countProperties = countProperties;
        this.data = data == null ? List.of() : new ArrayList<>(data);
    }

    public List<T> getData() {
        return data == null ? null : new ArrayList<>(data);
    }

    public static class ExcelListResponseBuilder<T> {
        public ExcelListResponseBuilder<T> data(List<T> data) {
            this.data = data == null ? null : new ArrayList<>(data);
            return this;
        }
    }
}

