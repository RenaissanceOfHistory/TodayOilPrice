from pydantic import BaseModel
from typing import Any
from enum import Enum


class CodeStatus(Enum):
    """状态码"""
    SUCCESS = 200
    FAIL = 401


class Result(BaseModel):
    """公共返回类"""
    code: CodeStatus
    info: str
    data: Any
