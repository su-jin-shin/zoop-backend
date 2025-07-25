import os
from pathlib import Path

from dotenv import load_dotenv

try:
    env_path = Path(__file__).resolve().parents[2] # .env 파일 경로
    load_dotenv(dotenv_path=env_path / ".env.local") # .env 불러오기
    print("env_path: ", env_path)
except Exception as e:
    print(f'.env 파일 로드 중 에러 발생: {e}')

try:
    DB_CONFIG = {
        #'database': os.getenv('POSTGRES_DB'),
        'database': 'mydb',
        #'user': os.getenv('SPRING_DATASOURCE_USERNAME'),
        'user': 'myuser',
        #'password': os.getenv('SPRING_DATASOURCE_PASSWORD'),
        'password': 'mypass',
        'host': os.getenv('POSTGRES_HOST', 'db'),
        'port': os.getenv('POSTGRES_PORT', 5432)
    }
except Exception as e:
    print(f'DB 환경 변수 설정 중 에러 발생: {e}')
    DB_CONFIG = {}