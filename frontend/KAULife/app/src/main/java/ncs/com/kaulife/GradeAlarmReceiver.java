package ncs.com.kaulife;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kkss2 on 2018-02-05.
 */

public class GradeAlarmReceiver extends BroadcastReceiver {
    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("확인", "AlarmReceiver 입장");
        ArrayList<LoginData> loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        LoginData loginDataTemp = loginDatas.get(0);
        if (loginDataTemp.gradeAuto) {
            LoginCheck(loginDataTemp, context);
        }
    }


    public void LoginCheck (final LoginData loginData, final Context context) {
        ServerInterface serverInterface = new Repo().getService();
        Call<String> c = serverInterface.LmsLogin(loginData.studentNum, loginData.password);
        c.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (result.equals("1")) {
                    GetGradeData(loginData, context);
                } else if (result.equals("0")) {
                    GenerateNotification(context, "로그인에 실패하였습니다. 로그인 정보를 확인 해 주세요.");
                    loginData.gradeAuto = false;
                    loginData.save();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void GetGradeData(LoginData loginData, final Context context) {
        Log.d("확인", "GetLmsData 입장");
        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<GradeData>> c = serverInterface.GetGradeNow(loginData.studentNum, loginData.password);
        c.enqueue(new Callback<ArrayList<GradeData>>() {
            @Override
            public void onResponse(Call<ArrayList<GradeData>> call, Response<ArrayList<GradeData>> response) {
                ArrayList<GradeData> gradeDatas = (ArrayList) GradeData.listAll(GradeData.class);
                ArrayList<GradeData> gradeDataTemp = response.body();
                if (gradeDataTemp.size() != 0) {
                    if (gradeDatas.size() == 0 ){
                        for (int i = 0; i < gradeDataTemp.size(); i ++) {
                            GradeData dataTemp = gradeDataTemp.get(i);
                            dataTemp.save();
                            return;
                        }
                    }
                    for (int i = 0; i < gradeDataTemp.size(); i++) {
                        int status = 0;
                        GradeData receiveData = gradeDataTemp.get(i);
                        GradeData alreadyData = gradeDatas.get(i);

                        if (!receiveData.grade.equals(alreadyData.grade)) {
                            status = 1;
                        }

                        if (status == 1) {
                            alreadyData.grade = receiveData.grade;
                            alreadyData.save(); //데이터 저장

                            GenerateNotification(context, receiveData.subject + " 의 성적이 업데이트 되었습니다.");
                            //노티피케이션 날리기
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GradeData>> call, Throwable t) {
            }
        });

    }

    public void GenerateNotification (Context context, String message) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("KAULife");
        builder.setContentText(message);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(Notification.PRIORITY_MAX);


        notificationManager.notify(5678, builder.build());
    }
}
