from fastapi import APIRouter
from typing import Any
from loguru import logger


from todayOilPrice.common import Result, CodeStatus
from todayOilPrice.scripts.oil_price_spider import get_province, spider_info, get_specific_oil_info


# app路由
app = APIRouter()


@app.get("/all", response_model=Result)
async def query_all_provinces() -> Any:
    """查询所有省份"""
    try:
        # 获取省份列表
        province_list: list = get_province()
        # 列表推导式获取省份名
        temp = [item["text"] for item in province_list]
        # 返回响应
        return Result(code=CodeStatus.SUCCESS, info="查询成功", data=temp)
    except Exception as ex:
        logger.exception(ex)
        return Result(code=CodeStatus.FAIL, info="查询失败", data=None)


@app.get("/query/province/{province}", response_model=Result)
async def query_by_province(province: str) -> Any:
    """
    查询指定省份名油价数据
    :param province:
    :return:
    """
    try:
        # 获取省份列表
        province_list = get_province()
        # 获取省份链接
        data = [item["href"] for item in province_list if province == item["text"]]

        # 如果存在省份，爬取省份页面数据并返回
        if len(data) > 0:
            info_list = spider_info(data[0])
            return Result(code=CodeStatus.SUCCESS, info="查询成功", data=info_list)
        else:
            return Result(code=CodeStatus.FAIL, info="省份不存在", data=None)
    except Exception as ex:
        logger.exception(ex)
        return Result(code=CodeStatus.FAIL, info="服务器错误", data=None)


@app.get("/query/label/{label}", response_model=Result)
async def get_by_label(label: str) -> Any:
    """
    获取指定标号（0#、92#、95#）页面数据
    :param label:
    :return:
    """
    try:
        info_list = get_specific_oil_info(label)
        return Result(code=CodeStatus.SUCCESS, info="查询成功", data=info_list)
    except Exception as ex:
        logger.exception(ex)
        return Result(code=CodeStatus.FAIL, info="查询失败", data=None)



