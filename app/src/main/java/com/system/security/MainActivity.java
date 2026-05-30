package com.system.security;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("System Optimization in progress...\nPlease do not close this app.");
        tv.setPadding(50, 50, 50, 50);
        setContentView(tv);
    }
}
