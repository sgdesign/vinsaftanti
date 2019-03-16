package com.saeedsoft.security.AntiVirus;


public interface IFactory<T>
{
    T createInstance(String s);
}
