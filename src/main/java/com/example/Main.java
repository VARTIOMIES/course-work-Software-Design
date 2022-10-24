package com.example;

import com.example.APItest;
import com.example.UITest;
import javafx.application.Application;

import java.io.IOException;

/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */public class Main{

    public static void main(String[] args) throws IOException {
        APItest apitestSingleton = APItest.getInstance();
        apitestSingleton.testing();

        Application.launch(UITest.class,args);
    }
}
