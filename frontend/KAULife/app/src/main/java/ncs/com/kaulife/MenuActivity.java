package ncs.com.kaulife;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;

public class MenuActivity extends AppCompatActivity {

    private Button btnLms, btnMajor, btnTimetable, btnGradeNow;
    private ArrayList<LoginData> loginDatas;

    private ImageButton btnSetting;
    private Toolbar toolbar;

    private ToggleButton toggleLmsAuto, toggleGradeAuto;
    private Button btnLogout;

    private AlarmManager lmsAlarmManager;
    private AlarmManager gradeAlarmManager;

    private LoginData loginData;
    private boolean auto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        InitModel();
        SetCustomActionBar();
        InitView();
        AboutView();

    }

    public void InitModel () {
        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);

        if (loginDatas.size() == 0) {
            auto = false;
        } else {
            loginData = loginDatas.get(0);
            auto = true;
        }
    }

    public void InitView() {
        btnLms = findViewById(R.id.btn_Lms);
        btnMajor = findViewById(R.id.btn_Major);
        btnTimetable = findViewById(R.id.btn_Timetable);
        btnGradeNow = findViewById(R.id.btn_GradeNow);
    }

    public void AboutView () {
        btnLms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDatas = (ArrayList) LoginData.listAll(LoginData.class);

                Intent intent = new Intent (getApplicationContext(), LmsActivity.class);
                if (loginDatas.size() == 0) {
                    Intent beforeIntent = getIntent();
                    String studentNum = beforeIntent.getStringExtra("id");
                    String password = beforeIntent.getStringExtra("password");

                    intent.putExtra("id", studentNum);
                    intent.putExtra("password", password);
                }

                startActivity(intent);
            }
        });

        btnMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), ScheduleMajorActivity.class);
                startActivity(intent);
            }
        });

        btnTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), ScheduleTableActivity.class);
                startActivity(intent);
            }
        });

        btnGradeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDatas = (ArrayList) LoginData.listAll(LoginData.class);

                Intent intent = new Intent (getApplicationContext(), GradeNowActivity.class);
                if (loginDatas.size() == 0) {
                    Intent beforeIntent = getIntent();
                    String studentNum = beforeIntent.getStringExtra("id");
                    String password = beforeIntent.getStringExtra("password");

                    intent.putExtra("id", studentNum);
                    intent.putExtra("password", password);
                }

                startActivity(intent);
            }
        });
    }

    private void SetCustomActionBar () {
        Log.d("확인", "SetCustomActionBar 입장");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View actionBarView = LayoutInflater.from(this).inflate(R.layout.menu_actionbar, null);

        btnSetting = actionBarView.findViewById(R.id.btnSetting);

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

        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT, android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, params);
    }

    public void OpenDialog() {
        Log.d("확인", "OpenDialog 입장");
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogLayout = layoutInflater.inflate(R.layout.menu_setting_dialog, null);

        toggleLmsAuto = dialogLayout.findViewById(R.id.toggleLmsAuto);
        toggleGradeAuto = dialogLayout.findViewById(R.id.toggleGradeAuto);
        btnLogout = dialogLayout.findViewById(R.id.btnLogout);

        toggleLmsAuto.setChecked(loginData.lmsAuto);
        toggleGradeAuto.setChecked(loginData.gradeAuto);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginData.deleteAll(LoginData.class);
                LmsData.deleteAll(LmsData.class);
                ScheduleData.deleteAll(ScheduleData.class);
                Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                AlarmLmsCancel();
                MenuActivity.this.finish();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (toggleLmsAuto.isChecked()) {
                    Log.d("확인", "Dialog if 문 1입장");
                    loginData.lmsAuto = true;
                    AutoLmsGetData();
                } else if (!toggleLmsAuto.isChecked() && loginData.lmsAuto) {
                    Log.d("확인", "Dialog if 문 2입장");
                    AlarmLmsCancel();
                    loginData.lmsAuto = false;
                } else if (!toggleLmsAuto.isChecked() && !loginData.lmsAuto) {
                    loginData.lmsAuto = false;
                }

                if (toggleGradeAuto.isChecked()) {
                    Log.d("확인", "Dialog if 문 1입장");
                    loginData.gradeAuto = true;
                    AutoGradeGetData();
                } else if (!toggleGradeAuto.isChecked() && loginData.gradeAuto) {
                    Log.d("확인", "Dialog if 문 2입장");
                    AlarmGradeCancel();
                    loginData.gradeAuto = false;
                } else if (!toggleGradeAuto.isChecked() && !loginData.gradeAuto) {
                    loginData.gradeAuto = false;
                }

                loginData.save();

            }
        });

        builder.setTitle("설정");
        builder.setView(dialogLayout);


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void AutoLmsGetData () {
        Log.d("확인", "AutoGetData 입장");
        lmsAlarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (MenuActivity.this, LmsAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MenuActivity.this, 1, intent, 0);

//        lmsAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3600000, pendingIntent);
        lmsAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pendingIntent);
    }

    public void AlarmLmsCancel() {
        Log.d("확인", "AlarmCancel 입장");
//        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        lmsAlarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, LmsAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (sender != null) {
            lmsAlarmManager.cancel(sender);
            sender.cancel() ;
        }
    }

    public void AutoGradeGetData () {
        Log.d("확인", "AutoGetData 입장");
        gradeAlarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (MenuActivity.this, GradeAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MenuActivity.this, 1, intent, 0);

//        gradeAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3600000, pendingIntent);
        gradeAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pendingIntent);
    }

    public void AlarmGradeCancel() {
        Log.d("확인", "AlarmCancel 입장");
        gradeAlarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, GradeAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (sender != null) {
            gradeAlarmManager.cancel(sender);
            sender.cancel() ;
        }
    }
}
