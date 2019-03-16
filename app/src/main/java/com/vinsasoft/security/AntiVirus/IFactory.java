package com.vinsasoft.security.AntiVirus;


public interface IFactory<T>
{
    T createInstance(String s);
}
