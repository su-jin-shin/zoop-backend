from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from app.crawler_api import main as crawl_main, crawl_by_article_no

app = FastAPI()

# CORS 미들웨어 추가
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "https://zoop-frontend-sable.vercel.app",
        "https://zoopzoop.shop"
    ],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class Filter(BaseModel):
    regionName: str
    regionCode: str
    tradeTypeName: str
    tradeTypeCode: str
    realEstateTypeName: str
    realEstateTypeCode: str
    dealOrWarrantPrc: int
    rentPrice: int
    userMessage: str




@app.post("/crawl")
async def crawl_estate_data(data: Filter):

    search_condition = {
        "dong": {
            "code": data.regionCode,
            "name": data.regionName
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
        "user_message": data.userMessage
    }

    # 크롤링 실행 (비동기)
    crawl_and_recommendation = await crawl_main(search_condition)
    if isinstance(crawl_and_recommendation, dict) and "results" in crawl_and_recommendation:
        print("crawl_and_recommendation가 dictionary 타입")
        crawl_and_recommendation = crawl_and_recommendation["results"]

    return {
        "status": "completed",
        "data": {
            "results": crawl_and_recommendation
        }
    }

@app.post("/{article_no}/crawl")
async def crawl_estate_data_by_article_no(article_no: str, data: Filter):
    print(f"받은 articleNo: {article_no}")

    # 크롤링 실행 (비동기)
    property_id = await crawl_by_article_no(article_no, data.realEstateTypeCode, data.regionCode)
    print('property_id: ', property_id)

    return {
        "status": "completed",
        "data": property_id
    }

@app.get("/health")
async def health():
    return {"status": "ok"}



