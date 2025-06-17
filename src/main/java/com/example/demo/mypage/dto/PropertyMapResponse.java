package com.example.demo.mypage.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "mapProperties는 생성자에서 복사되어 방어됨"
)
@Getter
@Builder
public class PropertyMapResponse {

    private final List<MapPropertyDto> mapProperties;
    private final MyPropertyPageResponse myPropertyPageResponse;

}


