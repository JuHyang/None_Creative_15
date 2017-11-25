package ncs.com.kaulife;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

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
        Intent intent = null;
        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        if (loginDatas.size() == 0) {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        } else {
            if (Login(loginDatas.get(0)).equals("0")) {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            } else if (Login(loginDatas.get(0)).equals("-1")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setTitle("오류");
                builder.setMessage("현재 서버에 오류가 있습니다. 잠시 후 다시 시도해 주세요.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                intent = new Intent(getApplicationContext(), LmsActivity.class);
            }
        }


        startActivity(intent);
        finish();
    }

    public String Login (LoginData loginData) {
        Log.d("확인", "Login 입장");
        final String[] loginjudge = {""};

        ServerInterface serverInterface = new Repo().getService();
        Call<LoginReceiveData> c = serverInterface.LmsLogin(loginData.studentNum, loginData.password);
        c.enqueue(new Callback<LoginReceiveData>() {
            @Override
            public void onResponse(Call<LoginReceiveData> call, Response<LoginReceiveData> response) {
                LoginReceiveData loginReceiveData = response.body();
                loginjudge[0] = loginReceiveData.result;
            }

            @Override
            public void onFailure(Call<LoginReceiveData> call, Throwable t) {
                loginjudge[0] = "-1";
            }
        });

        return loginjudge[0];
    }
}
