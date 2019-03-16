package com.vinsasoft.security.AntiVirus;


public interface IResultsAdapterItem
{
    enum ResultsAdapterItemType { Header, AppMenace, SystemMenace}

    public ResultsAdapterItemType getType();

}


