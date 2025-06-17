package com.example.demo.mypage.service;

import com.example.demo.common.excel.ExcelExportService;
import com.example.demo.mypage.domain.BookmarkedProperty;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.property.domain.Property;
import com.example.demo.realty.domain.Realty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BookmarkedExcelExportService implements ExcelExportService {

    private final BookmarkedPropertyRepository bookmarkedPropertyRepository;

    @Override
    public List<PropertyExcelDto> getExcelData(Long userId) {
        List<BookmarkedProperty> bookmarked = bookmarkedPropertyRepository
                .findAllWithPropertyAndRealtyByUserId(userId);

        return IntStream.range(0, bookmarked.size())
                .mapToObj(i -> {
                    Property property = bookmarked.get(i).getProperty();
                    Realty realtor = property.getRealty();
                    return PropertyExcelDto.from(property, realtor, i + 1);
                })
                .toList();
    }

}
