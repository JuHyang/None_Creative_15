package ncs.com.kaulife;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    EditText editTextId;
    EditText editTextPwd;
    Switch switchAuto;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        aboutView();
    }

    public void initView () {
        editTextId = findViewById(R.id.editTextId);
        editTextPwd = findViewById(R.id.editTextPwd);
        switchAuto = findViewById(R.id.switchAuto);
        btnLogin = findViewById(R.id.btnLogin);
    }

    public void aboutView () {
        initView ();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = editTextId.getText().toString();
                String password = editTextPwd.getText().toString();
                Boolean auto = switchAuto.isChecked();
                if (auto) {
                    LoginData loginData = new LoginData(id, password, false);
                    LoginCheck(loginData, auto);

                } else {

                    LoginData loginData = new LoginData(id, password, false);
                    LoginCheck(loginData, auto);
                }

            }
        });
    }

    public void LoginCheck (final LoginData loginData, final Boolean auto) {
        ServerInterface serverInterface = new Repo().getService();
        Call<LoginReceiveData> c = serverInterface.LmsLogin(loginData.studentNum, loginData.password);
        c.enqueue(new Callback<LoginReceiveData>() {
            @Override
            public void onResponse(Call<LoginReceiveData> call, Response<LoginReceiveData> response) {
                Intent intent = new Intent (getApplicationContext(), LmsActivity.class);

                LoginReceiveData loginReceiveData = response.body();
                if (loginReceiveData.result.equals("1")) {
                    if (auto) {
                        loginData.save();
                    } else {
                        intent.putExtra("id", loginData.studentNum);
                        intent.putExtra("password", loginData.password);
                    }
                    startActivity(intent);
                    finish();
                } else if (loginReceiveData.result.equals("0")) {
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginReceiveData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버에 오류가 있습니다. 잠시 후 다시 시도해주세요",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

}
