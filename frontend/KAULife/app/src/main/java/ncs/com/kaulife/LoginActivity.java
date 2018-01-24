package ncs.com.kaulife;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    private EditText editTextId;
    private EditText editTextPwd;
    private Switch switchAuto;
    private Button btnLogin;
    private ArrayList<LoginData> loginDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        InitModel();
        initView ();
        AboutView();
    }

    public void InitModel () {
        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);

    }
    public void initView () {
        editTextId = findViewById(R.id.editTextId);
        editTextPwd = findViewById(R.id.editTextPwd);
        switchAuto = findViewById(R.id.switchAuto);
        btnLogin = findViewById(R.id.btnLogin);
    }

    public void AboutView () {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentNum = editTextId.getText().toString();
                String password = editTextPwd.getText().toString();
                Boolean auto = switchAuto.isChecked();

                LoginData loginData = new LoginData(studentNum, password, false);
                LoginCheck(loginData, auto);
            }
        });
    }

    public void LoginCheck (final LoginData loginData, final Boolean auto) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
        final ProgressDialog dialog = ProgressDialog.show(this, "로그인", "잠시만 기다려주세요", true, true);
        dialog.setCancelable(false);
        dialog.show();
        ServerInterface serverInterface = new Repo().getService();
        Call<String> c = serverInterface.LmsLogin(loginData.studentNum, loginData.password);
        c.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                Intent intent = new Intent (getApplicationContext(), MenuActivity.class);

                String result = response.body();
                if (result.equals("1")) {
                    if (auto) {
                        if (loginDatas.size() != 0) {
                            LoginData temp = loginDatas.get(0);
                            if (!(temp.studentNum.equals(loginData.studentNum))) {
                                LmsData.deleteAll(LmsData.class);
                                LoginData.deleteAll(LoginData.class);
                                loginData.save();
                            } else {
                                temp.password = loginData.password;
                                temp.save();
                            }
                        } else {
                            loginData.save();
                        }
                    } else {
                        intent.putExtra("id", loginData.studentNum);
                        intent.putExtra("password", loginData.password);
                    }
                    startActivity(intent);
                    finish();
                } else if (result.equals("0")) {
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                alertDialogBuilder.setMessage("서버에 오류가 있습니다. 잠시 후 다시 시도해주세요")
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

    }

}
