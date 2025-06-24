package com.example.demo.chat.constants;

public class Constants {
    public static final int MAXIMUM_PROPERTY_COUNT = 10;

    public static final String CRAWL_AND_RECOMMEND_URL = "http://43.201.121.201:8000/crawl";
    public static final String CRAWL_BY_ARTICLE_NO_URL = "http://43.201.121.201:8000/{articleNo}/crawl";

    public static final String MATCHING_PROPERTY_MESSAGE = "%s님의 조건에 알맞는 %d개의 새로운 매물을 발견했습니다.";
    public static final String NO_MATCHING_PROPERTY_MESSAGE = "해당 조건에 일치하는 매물을 찾지 못하였습니다. 다른 조건을 입력해 보세요.";
    public static final String ADDITIONAL_FILTER_PROMPT = "추가하고 싶은 다른 조건은 있으신가요?";

    public enum MessageResultType {
        SUCCESS, FAILURE
    }

    private Constants() {}
}
