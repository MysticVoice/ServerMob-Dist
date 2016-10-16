package com.example.fredrik.chatclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new LoadUsers(new LoadUsers.Callback() {
            @Override
            public void update(List<LoadUsers.User> usrs) {
                // Update ui
            }
        }).execute("http://83.243.139.130:8080/chatserver/services/chat/users");
    }
}
