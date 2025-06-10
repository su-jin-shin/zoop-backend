from fastapi import FastAPI

app = FastAPI(title="Zoop")

@app.get("/health")
async def health():
    return {"status": "ok"}
