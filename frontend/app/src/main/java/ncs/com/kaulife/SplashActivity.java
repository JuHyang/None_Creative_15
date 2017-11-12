package ncs.com.kaulife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SelectActivity();
            }
        }, 3000);
    }

    public void SelectActivity () {
        ArrayList<LoginData> loginDatas;
        Intent intent;
        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        if (loginDatas.size() == 0) {
            intent = new Intent (getApplicationContext(), LoginActivity.class);
        } else {
            intent = new Intent (getApplicationContext(), LmsActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
