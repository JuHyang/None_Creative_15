package ncs.com.kaulife;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LmsActivity extends AppCompatActivity {
    private ArrayList<LmsData> lmsDatas;
    private ArrayList<LoginData> loginDatas;
    private LoginData loginData;
    private String studentNum;
    private String password;
    private Boolean auto;

    private AlarmManager lmsAlarmManager;

    private RecyclerView lmsRecyclerView;
    private RecyclerView.Adapter lmsAdapter;
    private RecyclerView.LayoutManager lmsLayoutManager;

    private Toolbar toolbar;

    private ImageButton btnSetting, btnGetLmsData;

    private ToggleButton toggleAuto;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lms);

        InitModel();
        SetCustomActionBar();
        aboutView();
    }

    protected void onResume () {
        super.onResume();
        InitModel();
        aboutView();
    }

    public void InitView() {
        lmsRecyclerView = findViewById(R.id.lmsRecyclerView);
    }

    public void aboutView() {
        InitView();

        GetLmsData(loginData, auto);

        Collections.reverse(lmsDatas);

        // in content do not change the layout size of the RecyclerView
        lmsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        lmsLayoutManager = new LinearLayoutManager(this);
        lmsRecyclerView.setLayoutManager(lmsLayoutManager);

        // specify an adapter (see also next example)
        lmsAdapter = new LmsAdapter(lmsDatas);
        lmsRecyclerView.setAdapter(lmsAdapter);
    }

    public void InitModel () {
        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        lmsDatas = (ArrayList) LmsData.listAll(LmsData.class);

        for (int i = 0; i < 10; i ++) {
            LmsData temp = new LmsData ("모바일SW    " + String.valueOf(i), "12:00", "과제과제과제과제");
            lmsDatas.add(temp);
        }

        if (loginDatas.size() == 0) {
            Intent intent = getIntent();
            studentNum = intent.getStringExtra("id");
            password = intent.getStringExtra("password");
            loginData = new LoginData(studentNum, password, false);
            auto = false;
        } else {
            loginData = loginDatas.get(0);
            auto = true;
        }

    }

    public void GetLmsData(LoginData loginData, final Boolean auto) {
        LoginCheck(loginData);

        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<LmsData>> c = serverInterface.GetLmsData(loginData);
        c.enqueue(new Callback<ArrayList<LmsData>>() {
            @Override
            public void onResponse(Call<ArrayList<LmsData>> call, Response<ArrayList<LmsData>> response) {

                ArrayList<LmsData> lmsDataTemp = response.body();
                if (lmsDataTemp.size() != 0) {
                    for (int i = 0; i < lmsDataTemp.size(); i++) {
                        int status = 1;
                        LmsData receiveData = lmsDataTemp.get(i);
                        for (int j = 0; j < lmsDatas.size(); j++) {
                            LmsData tempData = lmsDatas.get(j);
                            if (receiveData.time == tempData.time && receiveData.subject == tempData.subject && receiveData.content == tempData.content) {
                                status = 0;
                                break;
                            }
                        }
                        if (status == 1) {
                            lmsDatas.add(receiveData);
                            lmsAdapter.notifyDataSetChanged();
                            if (auto) {
                                receiveData.save();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LmsData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버에 오류가 있습니다. 잠시 후 다시 시도해주세요",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void AutoGetData () {
        lmsAlarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (getApplicationContext(), LmsAlarmReceiver.class);
        PendingIntent pending = getPendingIntent(intent, 1);

//        lmsAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3600000, pending);
        lmsAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pending);
    }

    private PendingIntent getPendingIntent(Intent intent, int id) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private void SetCustomActionBar () {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View actionBarView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);

        btnSetting = actionBarView.findViewById(R.id.btnSetting);
        btnGetLmsData = actionBarView.findViewById(R.id.btnGetLmsData);

        btnGetLmsData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "약간의 시간이 소요됩니다. 잠시만 기다려주세요.",Toast.LENGTH_LONG).show();
                GetLmsData(loginData, auto);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auto) {
                    OpenDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "자동로그인을 설정시에만 사용이 가능 합니다.",Toast.LENGTH_LONG).show();
                }
            }
        });

        actionBar.setCustomView(actionBarView);

        toolbar = (Toolbar) actionBarView.getParent();
        toolbar.setContentInsetsAbsolute(0,0);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, params);
    }

    public void OpenDialog() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogLayout = layoutInflater.inflate(R.layout.lms_dialog, null);

        toggleAuto = dialogLayout.findViewById(R.id.toggleAuto);
        btnLogout = dialogLayout.findViewById(R.id.btnLogout);

        toggleAuto.setChecked(loginData.lmsAuto);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginData.delete();
                Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                AlarmCancel();
                finish();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(LmsActivity.this);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (toggleAuto.isChecked()) {
                    AutoGetData();
                } else if (toggleAuto.isChecked() == false && loginData.lmsAuto == true) {
                    AlarmCancel();
                }
                loginData.lmsAuto = toggleAuto.isChecked();
                loginData.save();
            }
        });

        builder.setTitle("설정");
        builder.setView(dialogLayout);


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void AlarmCancel() {
        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, LmsAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (sender != null) {
            alarmManager.cancel(sender);
            sender.cancel() ;
        }

    }

    public void LoginCheck (LoginData loginData) {
        String loginjudge = Login(loginData);
        if (loginjudge.equals("1")) {
            return;
        } else if (loginjudge.equals("0")) {
            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "서버에 오류가 있습니다. 잠시 후 다시 시도해주세요",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public String Login (LoginData loginData) {
        return "1";
//        String loginjudge = "";
//        ServerInterface serverInterface = new Repo().getService();
//        Call<String> c = serverInterface.LmsLogin(loginData);
//        try {
//            loginjudge = c.execute().body();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "-1";
//        }
//        return loginjudge;
    }
}
