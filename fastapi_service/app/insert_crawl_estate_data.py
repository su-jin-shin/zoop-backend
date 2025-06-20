import json
import os
import sys
from pathlib import Path

import asyncpg
from dotenv import load_dotenv

# 모듈 import 기준 디렉토리 설정 (ai 폴더 기준)
CURRENT_DIR = Path(__file__).resolve().parent
MODULE_BASE_DIR = CURRENT_DIR.parent
sys.path.insert(0, str(MODULE_BASE_DIR))
from config.loader import DB_CONFIG

if not DB_CONFIG:
    raise ValueError('DB 설정이 누락되었습니다. .env 파일 또는 DB_CONFIG를 확인해주세요.')


def dump_if_not_empty(value):
    if not value:  # None, [] 둘 다 처리됨
        return None
    return json.dumps(value)

def safe_float(value):
    try:
        return float(value) if value is not None else None
    except (TypeError, ValueError):
        return None

def get_image_base_url():
    try:
        env_path = Path(__file__).resolve().parents[1] # .env 파일 경로
        load_dotenv(dotenv_path=env_path / ".env") # .env 불러오기
        print("***********" + os.getenv('IMAGE_BASE_URL'))
        return os.getenv('IMAGE_BASE_URL')
    except Exception as e:
        print(f'.env 파일 로드 중 에러 발생: {e}')
        return None


async def insert_many_properties(article_list, real_estate_type_code):
    pool = await asyncpg.create_pool(**DB_CONFIG)
    values_to_insert = []
    IMAGE_BASE_URL = get_image_base_url()

    async with pool.acquire() as conn:

        for item in article_list:
            details = item.get('articleDetails', {})
            realtor = details.get('articleRealtor', {})
            tax = details.get('articleTax', {})

            # 1. realty 테이블에 insert + realty_id 받기
            realty_id = None
            registration_no = realtor.get('establishRegistrationNo')
            if registration_no: # realty 테이블에 insert 하려면 이 값이 반드시 있어야 한다.
                realty_id = await conn.fetchval("""
                    INSERT INTO realty (realtor_account_id, establish_registration_no, realtor_name, representative_name,
                    address, representative_tel_no, cell_phone_no, deal_count, lease_count, rent_count,
                    max_broker_fee, broker_fee
                    )
                    VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
                    ON CONFLICT (establish_registration_no) DO NOTHING
                    RETURNING realty_id
                """, realtor.get('realtorId'), registration_no, realtor.get('realtorName'),
                    realtor.get('representativeName'), realtor.get('address'), realtor.get('representativeTelNo'),
                    realtor.get('cellPhoneNo'), realtor.get('dealCount'), realtor.get('leaseCount'),
                    realtor.get('rentCount'), tax.get('maxBrokerFee'), tax.get('brokerFee')
                )

                # 만약 이미 존재해서 RETURNING이 null이라면 직접 조회
                if realty_id is None:
                    realty_id = await conn.fetchval("SELECT realty_id FROM realty WHERE establish_registration_no = $1", registration_no)

            else:
                continue # registration_no 없으면 매물도 db insert 되지 않도록


            complex_info = item.get('complexInfo', {})
            detail = details.get('articleDetail', {})
            building_register = details.get('articleBuildingRegister', {})
            facility = details.get('articleFacility', {})
            floor = details.get('articleFloor', {})
            price = details.get('articlePrice', {})
            etc_fee_amount = details.get('administrationCostInfo', {}).get('etcFeeDetails', {}).get('etcFeeAmount')


            # 2-1. complex 테이블에 insert + complex_id 받기
            complex_id = None
            complex_no = None

            if real_estate_type_code in ['VL:YR', 'DDDGG:DSD']:
                complex_no = 'v' + str(complex_info['complexNumber']) if complex_info.get('complexNumber') else None
            elif real_estate_type_code in ['APT', 'OPST']:
                complex_no = 'a' + complex_info['complexNo'] if complex_info.get('complexNo') else None

            if complex_no: # complex 테이블에 insert 하려면 이 값이 반드시 있어야 한다.
                complex_id = await conn.fetchval("""
                    INSERT INTO complex (complex_no, complex_name)
                    VALUES ($1, $2)
                    ON CONFLICT (complex_no) DO NOTHING
                    RETURNING complex_id
                """, complex_no, complex_info.get('complexName'))

                # 만약 이미 존재해서 RETURNING이 null이라면 직접 조회
                if complex_id is None:
                    complex_id = await conn.fetchval("SELECT complex_id FROM complex WHERE complex_no = $1", complex_no)


                # 2-2. 단지 사진이 존재하면 이미지 테이블에 insert
                for idx, photo in enumerate(complex_info.get('photos', {})) or []:
                    is_main = (idx == 0)
                    image_full_url = IMAGE_BASE_URL + photo['imageSrc'] if photo.get('imageSrc') else None

                    await conn.execute("""
                        INSERT INTO image (complex_id, image_url, image_type, image_order, is_main)
                        VALUES ($1, $2, $3, $4, $5)
                        ON CONFLICT (complex_id, image_type, image_order) DO NOTHING
                    """, complex_id, image_full_url, 'COMPLEX', idx + 1, is_main)


            # 3. property 테이블에 insert하려는 값 가져오기
            article_name, apt_name, heat_method_type_name, heat_fuel_type_name, household_count, use_approve_ymd, parking_count, parking_count_per_house_hold \
                = extract_article_info(item, real_estate_type_code)
            values_to_insert.append((realty_id, complex_id, item.get('articleNo'), article_name, detail.get('tradeCompleteYN'),
                                     apt_name, heat_method_type_name, heat_fuel_type_name,
                                     household_count, use_approve_ymd, item.get('realEstateTypeName'), item.get('tradeTypeName'),
                                     detail.get('cityName'), detail.get('divisionName'), detail.get('sectionName'),
                                     str(detail.get('walkingTimeToNearSubway')), detail.get('roomCount'),
                                     detail.get('bathroomCount'), detail.get('moveInTypeName'), detail.get('moveInPossibleYmd'),
                                     detail.get('articleFeatureDescription'), detail.get('detailDescription'), detail.get('parkingPossibleYN'),
                                     detail.get('principalUse'), building_register.get('mainPurpsCdNm'),
                                     item.get('floorInfo'), item.get('dealOrWarrantPrc'), str(item.get('area1')),
                                     str(item.get('area2')), item.get('direction'), item.get('articleFeatureDesc'),
                                     item.get('sameAddrMaxPrc'), item.get('sameAddrMinPrc'),
                                     facility.get('directionBaseTypeName'), facility.get('entranceTypeName'),
                                     dump_if_not_empty(facility.get('lifeFacilities')),
                                     dump_if_not_empty(facility.get('securityFacilities')),
                                     dump_if_not_empty(facility.get('etcFacilities')),
                                     floor.get('totalFloorCount'), floor.get('correspondingFloorCount'),
                                     parking_count, parking_count_per_house_hold,
                                     safe_float(item.get('latitude')), safe_float(item.get('longitude')),
                                     detail.get('buildingName'), detail.get('exposureAddress'), detail.get('exposeStartYMD'),
                                     price.get('rentPrice'), price.get('dealPrice'), price.get('warrantPrice'),
                                     price.get('allWarrantPrice'), price.get('allRentPrice'), price.get('priceBySpace'),
                                     tax.get('acquisitionTax'), tax.get('registTax'), tax.get('specialTax'),
                                     tax.get('eduTax'), price.get('financePrice'), etc_fee_amount,
                                     dump_if_not_empty(detail.get('tagList'))
                                     ))

        # 4. property 테이블에 일괄 insert
        await conn.executemany("""
            INSERT INTO property (realty_id, complex_id, article_no, article_name, trade_complete_yn, 
            apt_name, heat_method_type_name, 
            heat_fuel_type_name, household_count, use_approve_ymd, real_estate_type_name, trade_type_name,
            city_name, division_name, section_name, walking_time_to_near_subway, room_count, bathroom_count,
            move_in_type_name, move_in_possible_ymd, article_feature_description, detail_description, 
            parking_possible_yn, principal_use, main_purps_cd_nm, floor_info, deal_or_warrant_prc, 
            area1, area2, direction, article_feature_desc, same_addr_max_prc, same_addr_min_prc, 
            direction_base_type_name, entrance_type_name, life_facilities, security_facilities, etc_facilities, 
            total_floor_count, corresponding_floor_count, parking_count, parking_count_per_household,
            latitude, longitude, building_name, exposure_address, expose_start_ymd,
            rent_price, deal_price, warrant_price, all_warrant_price, all_rent_price, price_by_space, 
            acquisition_tax, regist_tax, special_tax, edu_tax, finance_price, etc_fee_amount, tag_list)
            VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19, $20, $21,
            $22, $23, $24, $25, $26, $27, $28, $29, $30, $31, $32, $33, $34, $35, $36, $37, $38, $39, $40, $41, $42, 
            $43, $44, $45, $46, $47, $48, $49, $50, $51, $52, $53, $54, $55, $56, $57, $58, $59, $60)
            ON CONFLICT (article_no) DO NOTHING
        """, values_to_insert)


        # 5. 매물 구조도/매물 사진이 존재하면 이미지 테이블에 insert

        article_no_list = [row[2] for row in values_to_insert] # article_no 목록 만들기

        # DB에서 다시 property_id 조회
        property_rows = await conn.fetch("""
            SELECT article_no, property_id, article_name
            FROM property
            WHERE article_no = ANY($1::text[])
        """, article_no_list)

        article_to_property_id = {
            row['article_no']: {
                "property_id": row['property_id'],
                "article_name": row['article_name']
            }
            for row in property_rows
        }


        image_values_to_insert = []
        properties = []
        order = 0

        for item in article_list:
            article_no = item.get('articleNo')
            if not article_no:
                continue

            article_info = article_to_property_id.get(article_no)
            if not article_info:
                continue  # 혹시라도 누락된 건 skip

            property_id = article_info['property_id']
            article_name = article_info['article_name']

            order += 1
            property_info = {
                "order": order,
                "propertyId": property_id,
                "tradeTypeName": item.get('tradeTypeName'),
                "rentPrice": price.get('rentPrice'),
                "warrantPrice": price.get('warrantPrice'),
                "dealPrice": price.get('dealPrice'),
                "dealOrWarrantPrc": item.get('dealOrWarrantPrc'),
                "tagList": item.get('tagList'), #summary
                'articleName': article_name,
                'realEstateTypeName': item.get('realEstateTypeName'),
                "area2": item.get('area2'), #area2
                "imageUrl": IMAGE_BASE_URL + item.get('representativeImgUrl') if item.get('representativeImgUrl') else None,
                "latitude": safe_float(item.get('latitude')),
                "longitude": safe_float(item.get('longitude'))
            }
            properties.append(property_info)

            # 매물 구조도
            for idx, photo in enumerate(detail.get('grandPlanList', {})) or []:
                is_main = (idx == 0)
                image_full_url = IMAGE_BASE_URL + photo['imageSrc'] if photo.get('imageSrc') else None

                image_values_to_insert.append((
                    property_id, image_full_url, 'STRUCTURE', idx + 1, is_main
                ))

            # 매물 사진
            for idx, photo in enumerate(details.get('articlePhotos', {})) or []:
                is_main = (idx == 0)
                image_full_url = IMAGE_BASE_URL + photo['imageSrc'] if photo.get('imageSrc') else None

                image_values_to_insert.append((
                    property_id, image_full_url, 'PROPERTY', idx + 1, is_main
                ))

        # 마지막에 일괄 insert
        await conn.executemany("""
            INSERT INTO image (property_id, image_url, image_type, image_order, is_main)
            VALUES ($1, $2, $3, $4, $5)
            ON CONFLICT (property_id, image_type, image_order) DO NOTHING
        """, image_values_to_insert)

    print(f'properties: {properties}, {len(properties)}개')
    await pool.close()
    return properties

#
def extract_article_info(item, real_estate_type_code):
    details = item.get('articleDetails', {})
    detail = details.get('articleDetail', {})

    if real_estate_type_code in ['VL:YR', 'DDDGG:DSD']:
        complex_info = item.get('complexInfo', {})
        facility = details.get('articleFacility', {})
        parking_count = detail.get('parkingCount')

        return (
            complex_info.get('complexName') + ' ' + complex_info.get('dongName') if complex_info else item.get('articleName'),
            details.get('articleBuildingRegister', {}).get('bldNm'),
            facility.get('heatMethodTypeName'),
            facility.get('heatFuelTypeName'),
            detail.get('householdCount'),
            facility.get('buildingUseAprvYmd'),
            str(parking_count) if parking_count is not None else None,
            detail.get('parkingPerHouseholdCount')
        )
    elif real_estate_type_code in ['APT', 'OPST']:
        return (
            detail.get('articleName'),
            detail.get('aptName'),
            detail.get('aptHeatMethodTypeName'),
            detail.get('aptHeatFuelTypeName'),
            detail.get('aptHouseholdCount'),
            detail.get('aptUseApproveYmd'),
            detail.get('aptParkingCount'),
            detail.get('aptParkingCountPerHousehold')
        )
