package com.saeedsoft.security.service;

import com.saeedsoft.security.CallBlocker.PhoneNumberException;
import com.saeedsoft.security.CallBlocker.TooShortNumberException;


public interface IChecker {
    int YES = 0;
    int NO = 1;
    int NONE = 2;

    int isBlockable(Call call) throws TooShortNumberException, PhoneNumberException;

    void doLast();

    void refresh();
}
