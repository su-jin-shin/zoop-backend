from fastapi import FastAPI
from pydantic import BaseModel

from .crawler_api import main as crawl_main

app = FastAPI()


class Filter(BaseModel):
    placeName: str
    bCode: str
    tradeTypeName: str
    tradeTypeCode: str
    realEstateTypeName: str
    realEstateTypeCode: str
    dealOrWarrantPrc: int
    rentPrice: int


@app.post("/crawl")
async def crawl_estate_data(data: Filter):

    search_condition = {
        "dong": {
            "code": data.bCode,
            "name": data.placeName
        },
        "trade_type": {
            "code": data.tradeTypeCode,
            "name": data.tradeTypeName
        },
        "real_estate_type": {
            "code": data.realEstateTypeCode,
            "name": data.realEstateTypeName
        },
        "deal_or_warrant_price": data.dealOrWarrantPrc,
        "rent_price": data.rentPrice,
    }

    # 크롤링 실행 (비동기)
    crawl_data = await crawl_main(search_condition)

    return {
        "status": "crawled",
        "data": crawl_data
    }




