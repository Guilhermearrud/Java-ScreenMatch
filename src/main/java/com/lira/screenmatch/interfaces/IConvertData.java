package com.lira.screenmatch.interfaces;

public interface IConvertData {
    <T> T getData(String json, Class<T> classe);
}
