package com.yifat.finalproject.DataBase;

import android.app.Activity;

// The base class for all business logic classes:
public abstract class BaseLogic {

    // The dal object
    protected DAL dal;

    // constructor
    public BaseLogic(Activity activity) {
        dal = new DAL(activity);
    }

    // Opens the database
    public void open() {
        dal.open();
    }

    // Closes the database
    public void close() {
        dal.close();
    }

}
