package com.example.demo.Filter.service.Impl;

import com.example.demo.Filter.domain.ChatFilterHistory;
import com.example.demo.Filter.domain.Filter;
import com.example.demo.Filter.domain.KeywordFilterHistory;
import com.example.demo.Filter.domain.Region;
import com.example.demo.Filter.dto.request.FilterRequestDto;
import com.example.demo.Filter.repository.ChatFilterHistoryRepository;
import com.example.demo.Filter.repository.FilterRepository;
import com.example.demo.Filter.repository.KeywordFilterHistoryRepository;
import com.example.demo.Filter.repository.RegionRepository;
import com.example.demo.Filter.service.FilterService;
import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.dto.ChatRoomRequestDto;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.service.ChatService;
import com.example.demo.common.exception.DuplicateFilterHistoryException;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.common.exception.UserNotFoundException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class FilterServiceImpl implements FilterService {

    private final UserInfoRepository userInfoRepository;
    private final RegionRepository regionRepository;
    private final FilterRepository filterRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatFilterHistoryRepository chatFilterHistoryRepository;
    private final KeywordFilterHistoryRepository keywordFilterHistoryRepository;

    private final ChatService chatService;

    // 알림 쪽에서 사용자가 선택한 필터 조건 저장
    @Override
    public void saveKeywordFilter(Long userId, FilterRequestDto filterRequestDto) {

        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        // 지역 조회
        Region region = regionRepository.findByCortarNo(filterRequestDto.getBCode()).orElseThrow(NotFoundException::new);

        // 필터 조건 저장
        Filter newFilter = filterRequestDto.toEntity(region);

        // 필터 중복 체크 및 저장
        Filter filter = createOrFind(newFilter);

        // 키워드 필터 히스토리 사용중인 것과 중복인지 체크
        boolean alreadyExists = keywordFilterHistoryRepository
                .existsByUserInfoAndFilterAndIsUsedTrue(userInfo, filter);

        if (alreadyExists) {
            throw new DuplicateFilterHistoryException();
        }

        // 히스토리 저장
        KeywordFilterHistory history = new KeywordFilterHistory(filter, userInfo);
        keywordFilterHistoryRepository.save(history);
}

    //채팅방 필터 등록
    @Override
    public void saveChatFilter(FilterRequestDto filterRequestDto, Long chatRoomId) {

        // 지역 조회
        Region region = regionRepository.findByCortarNo(filterRequestDto.getBCode())
                .orElseThrow(NotFoundException::new);

        // 필터 생성 (or 재사용)
        Filter filter = createOrFind(filterRequestDto.toEntity(region));


        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(NotFoundException::new);

        // 중복 확인
        boolean alreadyExists = chatFilterHistoryRepository
                .existsByChatRoomAndFilter(chatRoom, filter);

        if (alreadyExists) {
            throw new DuplicateFilterHistoryException();
        }
        // 히스토리 저장
        ChatFilterHistory history = new ChatFilterHistory(filter, chatRoom);
        chatFilterHistoryRepository.save(history);
    }

    // 이전에 있던 키워드 필터 히스토리 변경시 필터에 데이터 조건 있으면 참조만 없으면 등록 후 참조
    @Override
    public void modifyKeywordFilter(Long userId, Long historyId, FilterRequestDto updateRequestDto) {
        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        // 키워드 필터 히스토리에 존재하는 내역인지 확인
        KeywordFilterHistory history = keywordFilterHistoryRepository.findByKeywordFilterHistoryIdAndUserInfo(historyId,userInfo).orElseThrow(NotFoundException::new);

        // 기존 키워드 필터 히스토리 기록 비활성화
        keywordFilterHistoryRepository.disableKeywordFilterHistory(history.getKeywordFilterHistoryId());

        // 지역 조회
        Region region = regionRepository.findByCortarNo(updateRequestDto.getBCode()).orElseThrow(NotFoundException::new);

        // 필터 저장 또는 재사용
        Filter filter = createOrFind(updateRequestDto.toEntity(region));

        // 키워드 필터 히스토리 사용중인 것과 중복인지 체크
        boolean alreadyExists = keywordFilterHistoryRepository
                .existsByUserInfoAndFilterAndIsUsedTrue(userInfo, filter);

        // 히스토리 저장
        if (alreadyExists) {
            throw new DuplicateFilterHistoryException();
        }

        // 히스토리 저장
        KeywordFilterHistory newKeywordFilterHistory = new KeywordFilterHistory(filter, userInfo);
        keywordFilterHistoryRepository.save(newKeywordFilterHistory);

    }
    // 기존 키워드 필터 히스트로리에서 삭제 ( 실제로는 isUsed = true -> false 처리)
    @Override
    public void deactivateKeywordFilter(Long userId, Long keywordFilterHistoryId) {

        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        // 사용자의 히스토리 내역 확인
        KeywordFilterHistory history = keywordFilterHistoryRepository
                .findByKeywordFilterHistoryIdAndUserInfo(keywordFilterHistoryId, userInfo)
                .orElseThrow(NotFoundException::new);

        // 키워드 필터 히스토리 비활성화 isUsed = true -> false
        history.deactivate();
    }

    @Override
    public List<String> getAllFilterTitlesByUser(Long userId) {
        // 사용자 조회
        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        // 사용자가 등록한 keyword를 전체 목록 조회
        List<KeywordFilterHistory> keywordFilterHistories = keywordFilterHistoryRepository.findByUserInfoAndIsUsedTrue(userInfo);
        return keywordFilterHistories.stream()
                .map(keywordFilterHistory -> keywordFilterHistory.getFilter().getFilterTitle())
                .toList();
    }

    // 사용자가 등록한 필터 조건 상세 조회
//    @Override
//    public KeywordFilterHistoryResponseDto getKeywordFilterDetail(Long userId, Long keywordFilterHistoryId) {
//
//        // 사용자 조회
//        UserInfo userInfo = userInfoRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
//
//        // 해당 히스토리 조회 (유저 일치 + 존재 확인)
//        KeywordFilterHistory history = keywordFilterHistoryRepository
//                .findByKeywordFilterHistoryIdAndUserInfo(keywordFilterHistoryId, userInfo)
//                .orElseThrow(NotFoundException::new);
//
//        // 키워드 필터 히스토리에 연결된 필터 상세 조회
//        Filter filter = history.getFilter();
//
//        // Filter entity -> dto 변환 후 반환
//        return KeywordFilterHistoryResponseDto.from(filter);
//
//    }

    // 중복된 필터 조건이 아니면 필터 조건 등록
    private Filter createOrFind(Filter newFilter) {
        return filterRepository.findDuplicate(newFilter)
                .orElseGet(() -> filterRepository.save(newFilter));
    }

    // 채팅 필터 히스토리 조회
    @Override
    public FilterRequestDto getFilterByChatRoomId(Long chatRoomId) {
        ChatFilterHistory chatFilterHistory = chatFilterHistoryRepository
                .findByChatRoom_ChatRoomIdFetchFilterAndRegion(chatRoomId)
                .orElseThrow(NotFoundException::new);

        Filter filter = chatFilterHistory.getFilter();

        return FilterRequestDto.from(filter);
    }

}
