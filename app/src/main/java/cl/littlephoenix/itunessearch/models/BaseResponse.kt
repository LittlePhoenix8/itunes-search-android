package cl.littlephoenix.itunessearch.models

class BaseResponse<T>(val resultCount: Int, val results: Array<T>?)
