package com.saeedsoft.security.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;


public abstract class BaseActivity2 extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    protected void init() {
        initView();
        initData();
        initEvent();
    }
	
    protected abstract void initView();


    protected abstract void initData();

  
    protected abstract void initEvent();

}
