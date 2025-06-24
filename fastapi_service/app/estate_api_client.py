import os
import requests
from typing import Any, Dict, List, Optional
from pathlib import Path
from dotenv import load_dotenv

import aiofiles
import json

env_path = Path(__file__).resolve().parents[1]  # .env 파일 경로
load_dotenv(dotenv_path=env_path / ".env")  # .env 불러오기
HEADERS = {"Content-Type": "application/json"}  # 필요 시 인증 토큰 추가 가능


def get_request_url(url_name):
    url = os.getenv(url_name)
    if not url:
        raise EnvironmentError(f"환경 변수 '{url_name}'를 찾을 수 없습니다.")
    return url


# 1. 동별 매물 정보 전송
def upload_dong_properties(address: str, properties: List[Dict[str, Any]], cond: dict) -> Optional[Dict[str, Any]]:
    url = get_request_url("PROPERTY_UPLOAD_URL")

    # 누락 필드 보정
    for prop in properties:
        complex_info = prop.get("complexInfo", {})
        # 문자열이지만 정수처럼 보이면 int로 변환하고, 아니면 0으로
        try:
            complex_info["totalHouseholdCount"] = int(complex_info.get("totalHouseholdCount") or 0)
        except ValueError:
            complex_info["totalHouseholdCount"] = 0
        complex_info.setdefault("useApproveYmd", "")
        complex_info.setdefault("cortarAddress", "")
        prop["complexInfo"] = complex_info

    payload = {
        "address": address,
        "data": properties
    }

    try:
        response = requests.post(url, json=payload, headers=HEADERS)
        response.raise_for_status()
        response_data = search_dong_properties(address, cond)
        print("==============================")
        # 마지막에 한 번에 저장
        OUTPUT_DIR = Path(__file__).resolve().parent / 'output'
        json_file_subject = f'{OUTPUT_DIR}/detail_data.json'
        # 성공한 매물 저장
        os.makedirs(OUTPUT_DIR, exist_ok=True)
        with open(json_file_subject, 'w', encoding='utf-8') as f:
            json.dump(response_data, f, ensure_ascii=False, indent=2)
        print(f'[DONE] 저장 완료: {json_file_subject}')
        print("==============================")
        return response_data
    except requests.RequestException as e:
        print("동 매물 전송 실패:", e)
        return None


def format_price(number: int) -> str:
    if number < 10000:
        return f"{number}만원"
    else:
        억 = number // 10000
        만원 = number % 10000
        if 만원 == 0:
            return f"{억}억"
        return f"{억}억 {만원}만원"


def search_dong_properties(address: str, cond: dict):
    trade_type_name = cond['trade_type']['name']
    real_estate_type_name = cond['real_estate_type']['name']
    deal_or_warrant_price = cond['deal_or_warrant_price']
    rent_price = cond['rent_price']
    user_message = cond['user_message']

    base_url = get_request_url("PROPERTY_SEARCH_URL")
    url = f'{base_url}/{address}'

    deal_or_warrant_price_str = format_price(deal_or_warrant_price)
    rent_price_str = format_price(rent_price)
    msg = f' {user_message}' if user_message else ''

    if '원룸' in real_estate_type_name or '투룸' in real_estate_type_name:
        real_estate_type_name = '원룸'

    if trade_type_name == '월세':
        if deal_or_warrant_price == 0:  # 보증금을 입력하지 않았을 때
            query = f'{real_estate_type_name} {trade_type_name} {rent_price_str} 이하 추천해줘.{msg}'
        else:
            query = f'{real_estate_type_name} {trade_type_name} {rent_price_str} 이하, 보증금 {deal_or_warrant_price_str} 이하 추천해줘.{msg}'
    else :
        query = f'{real_estate_type_name} {trade_type_name} {deal_or_warrant_price_str} 이하 추천해줘.{msg}'


    print('query: ', query)
    payload = {
        "query": query
    }

    try:
        response = requests.post(url, json=payload, headers=HEADERS)
        response.raise_for_status()
        return response.json()
    except requests.RequestException as e:
        print('동 매물 조회 실패: ', e)
        return None
