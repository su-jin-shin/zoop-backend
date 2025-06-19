import asyncio
import json
import math
from datetime import datetime
from pathlib import Path

import aiohttp
from aiohttp import ClientSession, ClientTimeout
from more_itertools import chunked

from .insert_crawl_estate_data import insert_many_properties

CURRENT_DIR = Path(__file__).resolve().parent

# 헤더/쿠키
with open(CURRENT_DIR / 'secrets/headers.json', 'r', encoding='utf-8') as f:
    HEADERS = json.load(f)

with open(CURRENT_DIR / 'secrets/cookies.json', 'r', encoding='utf-8') as f:
    COOKIES = json.load(f)

semaphore = asyncio.Semaphore(500)
BATCH_SIZE = 20 # 매물 병렬 처리 단위
PAGE_CHUNK_SIZE = 50 # 페이지 병렬 처리 단위

MAX_RETRY = 3 # 최대 시도 수
failed_articles = []  # 실패한 article 기록용

MAX_ARTICLE_COUNT = 10

async def fetch_json(session: ClientSession, url: str) -> dict:
    try:
        async with semaphore:
            timeout = ClientTimeout(total=15)
            async with session.get(url, headers=HEADERS, cookies=COOKIES, timeout=timeout) as response:
                if response.status == 200:
                    print(f'성공: {url}')
                    return await response.json()
                else:
                    print(f'실패 status {response.status}: {url}')
                    return {}
    except asyncio.TimeoutError:
        print(f'타임아웃: {url}')
        return {}
    except Exception as e:
        print(f'요청 에러: {url} | 에러: {e}')
        return {}


async def fetch_page(session, page, dong_code, real_estate_type_code, trade_type_code, deal_or_warrant_price, rent_price):
    url = (
        f'https://new.land.naver.com/api/articles'
        f'?cortarNo={dong_code}'
        f'&order=rank'
        f'&realEstateType={real_estate_type_code}'  # 아파트/오피스텔/빌라/원룸-투룸
        f'&tradeType={trade_type_code}'  # 매매/전세/월세
        f'&tag=%3A%3A%3A%3A%3A%3A%3A%3A'
        f'&rentPriceMin=0&rentPriceMax={rent_price}'  # 900000000
        f'&priceMin=0&priceMax={deal_or_warrant_price}'
        f'&areaMin=0&areaMax=900000000'
        f'&priceType=RETAIL'
        f'&showArticle=false'
        f'&sameAddressGroup=false'
        f'&page={page}'
    )
    return await fetch_json(session, url)


async def handle_article(article, session, dong_code, real_estate_type_code, v_complex_cache, a_complex_cache):
    article_no = article.get('articleNo')
    detail_url = f'https://new.land.naver.com/api/articles/{article_no}?complexNo='
    detail_data = await fetch_json(session, detail_url)

    # 단지 정보 불러오기(빌라, 원룸-투룸)
    if real_estate_type_code in ['VL:YR', 'DDDGG:DSD']:

        if article.get('isComplex'):  # 해당 매물의 단지가 존재하면 단지 정보를 가져온다.

            # 해당 매물 정보
            property_info_url = f'https://new.land.naver.com/api/property/article/rent/{article["articleRealEstateTypeCode"]}/{article_no}/{article["tradeTypeCode"]}'
            property_info_data = await fetch_json(session, property_info_url)
            # 해당 매물 정보에서 단지 정보 추출
            complex_info = property_info_data.get('data', {}).get('communalComplexInfo', {})  # 해당 key가 없으면 {}를 반환
            complex_no = complex_info.get('complexNumber')  # 해당 key가 없으면 None을 반환

            if complex_no:  # complex_no가 None이나 빈 문자열이 아니면
                article['complexInfo'] = complex_info

                if complex_no not in v_complex_cache:  # 캐시에 저장되어 있지 않다면
                    complex_info_url = f'https://new.land.naver.com/api/property/complex/villa/{complex_no}'
                    complex_info_data = await fetch_json(session, complex_info_url)

                    complex_photo_url = f'https://new.land.naver.com/api/property/complex/villa/{complex_no}/photo'
                    complex_photo_data = await fetch_json(session, complex_photo_url)

                    # 캐시에 저장
                    v_complex_cache[complex_no] = {
                        'complexDetails': complex_info_data['data'],
                        'photoInfo': complex_photo_data['data'],
                    }

                # 캐시에서 가져와서 단지 정보 삽입
                article['complexInfo'].update(v_complex_cache[complex_no])  # 내용 추가

    # 단지 정보 불러오기(아파트, 오피스텔)
    elif real_estate_type_code in ['APT', 'OPST']:
        complex_no = detail_data.get('articleDetail', {}).get('hscpNo')

        if complex_no:
            if complex_no not in a_complex_cache:

                complex_info_url = f'https://new.land.naver.com/api/regions/complexes?cortarNo={dong_code}&realEstateType={real_estate_type_code}&order=rank'
                complex_info_data = await fetch_json(session, complex_info_url)

                for complex in complex_info_data.get('complexList', []):
                    if complex['complexNo'] == complex_no:
                        matched_complex = complex

                        # 좀 더 상세한 단지 정보 (일단 여기서는 사진만 가져옴)
                        complex_detail_info_url = f'https://new.land.naver.com/api/complexes/{complex_no}'
                        complex_detail_info_data = await fetch_json(session, complex_detail_info_url)

                        a_complex_cache[complex_no] = {
                            'basicInfo': matched_complex,
                            'photos': complex_detail_info_data.get('photos', [])
                        }
                        break

            # 캐시에서 가져와서 단지 정보 삽입
            article['complexInfo'] = a_complex_cache[complex_no]['basicInfo']
            article['complexInfo']['photos'] = a_complex_cache[complex_no]['photos']

    article['articleDetails'] = detail_data
    return article


async def handle_article_with_retry(article, session, dong_code, real_estate_type_code, v_complex_cache, a_complex_cache):
    article_no = article.get('articleNo')
    for attempt in range(1, MAX_RETRY + 1):
        try:
            result = await handle_article(article, session, dong_code, real_estate_type_code, v_complex_cache, a_complex_cache)
            if result:
                return result
        except Exception as e:
            print(f'[RETRY-{attempt}] article {article_no}, 에러: {e}')
        await asyncio.sleep(0.3 * attempt)  # 점점 늘어나는 대기

    # 재시도 실패 시 기록
    print(f'[FAIL] article {article_no} 재시도 실패')
    failed_articles.append(article_no)
    return None


async def fetch_articles_by_dong(session: ClientSession, cond: dict):
    dong_code = cond['dong']['code']
    dong_name = cond['dong']['name']
    trade_type_code = cond['trade_type']['code']
    trade_type_name = cond['trade_type']['name']
    real_estate_type_code = cond['real_estate_type']['code']
    real_estate_type_name = cond['real_estate_type']['name']
    deal_or_warrant_price = cond['deal_or_warrant_price']
    rent_price = cond['rent_price']

    all_details_with_page = []
    # 단지 정보 캐시
    v_complex_cache = {}
    a_complex_cache = {}

    start = datetime.now()  # 크롤링 시작 시간

    # 1페이지 요청하여 총 매물 수 알아내기
    first_page_data = await fetch_page(session, 1, dong_code, real_estate_type_code, trade_type_code,
                                       deal_or_warrant_price, rent_price)
    first_articles = first_page_data.get('articleList', [])
    map_count = first_page_data.get('mapExposedCount') # 총 매물 수

    if not first_articles:
        print(f'[INFO] [{dong_name}] 매물 없음, 수집 종료')
        return []

    print(f'[INFO] [{dong_name}] 첫 페이지 수집됨, 매물 수: {len(first_articles)}')


    # 1페이지 매물 상세 호출
    for article_batch in chunked(first_articles, BATCH_SIZE):
        first_tasks = [
            handle_article_with_retry(article, session, dong_code, real_estate_type_code, v_complex_cache, a_complex_cache)
            for article in article_batch
        ]
        first_results = await asyncio.gather(*first_tasks)
        all_details_with_page.extend([(1, result) for result in first_results if result])

        if len(all_details_with_page) > MAX_ARTICLE_COUNT:
            all_details_with_page = all_details_with_page[:MAX_ARTICLE_COUNT]
            sorted_details = [detail for _, detail in sorted(all_details_with_page, key=lambda x: x[0])]
            properties = await insert_many_properties(sorted_details, real_estate_type_code)
            print("[INFO] 10개 수집 완료, 조기 종료")
            #return sorted_details
            return properties


    # 추가 페이지 병렬 처리
    is_next = first_page_data.get('isMoreData')

    if is_next: # 2페이지도 존재하면
        if map_count:  # map_count가 있으면 병렬 처리
            total_pages = math.ceil(map_count / len(first_articles)) # len(first_articles)는 네이버 부동산 1페이지당 매물 수
            print(f'[INFO] 총 매물 수: {map_count}, 총 페이지 수: {total_pages}')

            page_numbers = list(range(2, total_pages + 1))
            for i in range(0, len(page_numbers), PAGE_CHUNK_SIZE):
                chunk = page_numbers[i:i + PAGE_CHUNK_SIZE]  # 2~51, 52~101
                fetch_tasks = [
                    fetch_page(session, page, dong_code, real_estate_type_code,
                               trade_type_code, deal_or_warrant_price, rent_price)
                    for page in chunk
                ]
                page_results = await asyncio.gather(*fetch_tasks)

                for idx, page_data in enumerate(page_results):
                    page_no = chunk[idx]
                    if not page_data:
                        continue
                    page_articles = page_data.get('articleList', [])
                    print(f'********** 매물 개수: [{dong_name} / 전체 {total_pages}페이지 중 {page_no}번째] 매물 {len(page_articles)}건 수집됨 **********')

                    # 페이지별로 매물 상세 호출 (BATCH_SIZE 단위로 나눠서 비동기 요청 처리)
                    for article_batch in chunked(page_articles, BATCH_SIZE):
                        detail_tasks = [
                            handle_article_with_retry(article, session, dong_code, real_estate_type_code,
                                           v_complex_cache, a_complex_cache)
                            for article in article_batch
                        ]
                        detail_results = await asyncio.gather(*detail_tasks)
                        all_details_with_page.extend([
                            (page_no, detail) for detail in detail_results if detail
                        ])

        else:  # map_count가 없으면 while True 루프로 순차 처리
            print('[WARN] mapExposedCount 없음 > 순차 요청으로 전환')
            page = 2
            while True:
                page_data = await fetch_page(session, page, dong_code, real_estate_type_code,
                                             trade_type_code, deal_or_warrant_price, rent_price)
                if not page_data:
                    print(f'[WARN] p{page} 실패함, 건너뜀')
                    page += 1
                    continue

                page_articles = page_data.get("articleList", [])
                if not page_articles:
                    print(f'[INFO] p{page} 매물 없음, 다음으로')
                    page += 1
                    continue

                print(f'********** 매물 개수: [{dong_name} / p{page}] 매물 {len(page_articles)}건 수집됨 **********')

                # 페이지별로 매물 상세 호출 (BATCH_SIZE 단위로 나눠서 비동기 요청 처리)
                for article_batch in chunked(page_articles, BATCH_SIZE):
                    detail_tasks = [
                        handle_article_with_retry(article, session, dong_code, real_estate_type_code,
                                       v_complex_cache, a_complex_cache)
                        for article in article_batch
                    ]
                    detail_results = await asyncio.gather(*detail_tasks)
                    all_details_with_page.extend([
                        (page, detail) for detail in detail_results if detail
                    ])

                if not page_data.get("isMoreData", False): # 더 이상의 페이지가 없으면 종료
                    break
                page += 1
                await asyncio.sleep(0.05) # API 과부하 방지

    print(f'[DONE] 최종 수집된 상세 매물 수: {len(all_details_with_page)}')

    # 크롤링 시간 측정
    end = datetime.now()
    duration = (end - start).total_seconds()
    print(f'[TIME] 크롤링 시간: {duration:.2f}초')


    # 페이지 번호 기준 정렬 후 상세 데이터만 추출
    sorted_details = [detail for _, detail in sorted(all_details_with_page, key=lambda x: x[0])]
    # DB insert
    properties = await insert_many_properties(sorted_details, real_estate_type_code)
    # return sorted_details
    return properties


async def main(search_condition):
    async with aiohttp.ClientSession() as session:
        tasks = []
        task = fetch_articles_by_dong(session, search_condition)
        tasks.append(task)
        results = await asyncio.gather(*tasks)
    return results[0]  # 파일 경로 리턴


if __name__ == '__main__':
    asyncio.run(main())
