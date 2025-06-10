package com.example.demo.property.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;


//List<String>타입을 JSONB로 저장 및 읽기 위해 사용

@Converter
public class JsonStringListConverter implements AttributeConverter<List<String>,String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    //List -> Json
    //엔티티의 List<String> 필드를 DB에 저장할 때 호출
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try{
            return objectMapper.writeValueAsString(attribute);
        }catch (Exception e) {
            throw new IllegalArgumentException("JSON 변환 실패", e);
        }
    }


    //json 문자열 -> List<String>
    //DB에서 읽어온 json 문자열 자바 객체로 변환할 때 호출
    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try{
            return objectMapper.readValue(dbData, new TypeReference<>() {});
        }catch (Exception e){
            throw new IllegalArgumentException("Json 변환 실패",e);
        }
    }
}
