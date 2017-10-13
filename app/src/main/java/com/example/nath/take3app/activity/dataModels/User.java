package com.example.nath.take3app.activity.dataModels;

/**
 * Created by nath on 11-Oct-17.
 */

public class User {

    private String full_name;

    public User(String full_name) {
        this.full_name = full_name;
    }

    public User() {
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }


    @Override
    public String toString() {
        return "User{" +
                "full_name='" + full_name + '\'' +
                '}';
    }


}
