package com.vinsasoft.security.service;

import com.vinsasoft.security.CallBlocker.PhoneNumberException;
import com.vinsasoft.security.CallBlocker.TooShortNumberException;


public interface IChecker {
    int YES = 0;
    int NO = 1;
    int NONE = 2;

    int isBlockable(Call call) throws TooShortNumberException, PhoneNumberException;

    void doLast();

    void refresh();
}
