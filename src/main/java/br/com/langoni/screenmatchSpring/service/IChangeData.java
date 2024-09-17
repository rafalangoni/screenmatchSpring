package br.com.langoni.screenmatchSpring.service;

public interface IChangeData {
    <T> T getData(String json, Class<T> classGeneric);
}
