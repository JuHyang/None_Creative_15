package ncs.com.kaulife;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import java.util.ArrayList;

/**
 * Created by kkss2 on 2018-02-12.
 */

@SuppressLint("ValidFragment")
public class SettingFragment extends Fragment {
    Context context;

    private Button btnTableClear, btnLogout;
    private Switch switchLms, switchGrade;

    private AlarmManager lmsAlarmManager;
    private AlarmManager gradeAlarmManager;

    private ArrayList<LoginData> loginDatas;
    private LoginData loginData;

    public SettingFragment (Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_setting, container, false);
        InitView(layout);
        AboutView();
        return layout;
    }

    public void onResume() {
        super.onResume();
        AboutView();
    }

    public void InitView (View view) {
        switchLms = view.findViewById(R.id.switchLms);
        switchGrade = view.findViewById(R.id.switchGrade);
        btnTableClear = view.findViewById(R.id.btn_table_clear);
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    public void AboutView () {
        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        loginData = loginDatas.get(0);

        switchLms.setChecked(loginData.lmsAuto);
        switchLms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchLms.isChecked()) {
                    AutoLmsGetData();
                    loginData.lmsAuto = true;
                } else {
                    AlarmLmsCancel();
                    loginData.lmsAuto = false;
                }
                loginData.save();
            }
        });

        switchGrade.setChecked(loginData.gradeAuto);
        switchGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchGrade.isChecked()) {
                    AutoGradeGetData();
                    loginData.gradeAuto = true;
                } else {
                    AlarmGradeCancel();
                    loginData.gradeAuto = false;
                }
                loginData.save();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까 ?")
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlarmLmsCancel();
                                        AlarmGradeCancel();
                                        LoginData.deleteAll(LoginData.class);
                                        LmsData.deleteAll(LmsData.class);
                                        ScheduleData.deleteAll(ScheduleData.class);
                                        Intent intent = new Intent (context, LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        btnTableClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("초기화")
                        .setMessage("시간표를 초기화 하시겠습니까 ?")
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ScheduleData.deleteAll(ScheduleData.class);
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }


    public void AutoLmsGetData () {
        Log.d("확인", "AutoGetData 입장");
        lmsAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (context, LmsAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        lmsAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3600000, pendingIntent);
//        lmsAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pendingIntent);
    }

    public void AlarmLmsCancel() {
        Log.d("확인", "AlarmCancel 입장");
//        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        lmsAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LmsAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (sender != null) {
            lmsAlarmManager.cancel(sender);
            sender.cancel() ;
        }
    }

    public void AutoGradeGetData () {
        Log.d("확인", "AutoGetData 입장");
        gradeAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (context, GradeAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        gradeAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, 3600000, pendingIntent);
//        gradeAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pendingIntent);
    }

    public void AlarmGradeCancel() {
        Log.d("확인", "AlarmCancel 입장");
        gradeAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GradeAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (sender != null) {
            gradeAlarmManager.cancel(sender);
            sender.cancel() ;
        }
    }
}
