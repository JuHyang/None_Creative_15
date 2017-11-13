package ncs.com.kaulife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;

public class LoginActivity extends Activity {
    EditText editTextId;
    EditText editTextPwd;
    Switch switchAuto;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Intent intent = new Intent (getApplicationContext(), LmsActivity.class);
                String id = editTextId.getText().toString();
                String password = editTextPwd.getText().toString();
                Boolean auto = switchAuto.isChecked();
                if (auto) {
                    LoginData loginData = new LoginData(id, password);
                    String loginjudge = Login(loginData);
                    if (loginjudge.equals("1")) {
                        loginData.save();
                        startActivity(intent);
                        finish();
                    } else if (loginjudge.equals("0")) {
                        Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "서버에 오류가 있습니다. 잠시 후 다시 시도해주세요",Toast.LENGTH_SHORT).show();
                    }


                } else {
                    intent.putExtra("id", id);
                    intent.putExtra("password", password);
                    LoginData loginData = new LoginData(id, password);
                    String loginjudge = Login(loginData);
                    if (loginjudge.equals("1")) {
                        loginData.save();
                        startActivity(intent);
                        finish();
                    } else if (loginjudge.equals("0")) {
                        Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "서버에 오류가 있습니다. 잠시 후 다시 시도해주세요",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public String Login (LoginData loginData) {
        String loginjudge = "";
        ServerInterface serverInterface = new Repo().getService();
        Call<String> c = serverInterface.LmsLogin(loginData);
        try {
            loginjudge = c.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return "-1";
        }
        return loginjudge;
    }

}
