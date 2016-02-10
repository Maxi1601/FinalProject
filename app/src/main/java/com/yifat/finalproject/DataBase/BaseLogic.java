package com.yifat.finalproject.DataBase;

import android.app.Activity;

// The base class for all business logic classes:
public abstract class BaseLogic {

    // The dal object:
    protected DAL dal; // We want only the derived classes to access the dal object.

    // constructor:
    public BaseLogic(Activity activity) {
        dal = new DAL(activity);
    }

    // Open the database:
    public void open() {
        dal.open();
    }

    // close the database:
    public void close() {
        dal.close();
    }

}
