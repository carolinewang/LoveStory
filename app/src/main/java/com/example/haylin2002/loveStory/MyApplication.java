package com.example.haylin2002.loveStory;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

/**
 * Created by haylin2002 on 15/4/21.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "4l6H5UOr3QQwZ2hQM3sbppAsORjkrboUAufPSU56", "WSozgbkl646Bf49ZUo9T8lvTdcNKszNwhraFk4x5");
        ParseACL.setDefaultACL(new ParseACL(), true);
    }
}
