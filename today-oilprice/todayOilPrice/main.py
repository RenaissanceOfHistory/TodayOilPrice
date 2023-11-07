from fastapi import FastAPI
import uvicorn

from todayOilPrice.service.oil_price_service import app as oil_app

# 主app
app = FastAPI()
# app添加路由
app.include_router(oil_app, prefix="/oils", tags=["油价信息模块"])


@app.get("/health")
async def health() -> dict:
    """发送健康心跳"""
    return {"status": "UP"}


def main() -> None:
    uvicorn.run(app)


if __name__ == '__main__':
    main()
