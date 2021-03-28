package com.testa.htmlckq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Activity_make extends AppCompatActivity {
    private TextView tv_make;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_make);
        tv_make=findViewById(R.id.tv_make);
        tv_make.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
