package com.testa.htmlckq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Activity_app extends AppCompatActivity {
    private TextView tv_think;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        tv_think=findViewById(R.id.tv_think);
        tv_think.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
