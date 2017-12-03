package ncs.com.kaulife;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kkss2 on 2017-11-15.
 */

public class LmsAlarmReceiver extends BroadcastReceiver {
    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("확인", "AlarmReceiver 입장");
        ArrayList<LoginData> loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        LoginData loginDataTemp = loginDatas.get(0);
        if (loginDataTemp.lmsAuto) {
            LoginCheck(loginDataTemp, context);
        }
    }


    public void LoginCheck (final LoginData loginData, final Context context) {
        ServerInterface serverInterface = new Repo().getService();
        Call<LoginReceiveData> c = serverInterface.LmsLogin(loginData.studentNum, loginData.password);
        c.enqueue(new Callback<LoginReceiveData>() {
            @Override
            public void onResponse(Call<LoginReceiveData> call, Response<LoginReceiveData> response) {
                LoginReceiveData loginReceiveData = response.body();
                if (loginReceiveData.result.equals("1")) {
                    GetLmsData(loginData, context);
                } else if (loginReceiveData.result.equals("0")) {
                    GenerateNotification(context, "로그인에 실패하였습니다. 로그인 정보를 확인 해 주세요.");
                }
            }
            @Override
            public void onFailure(Call<LoginReceiveData> call, Throwable t) {
            }
        });
    }

    public void GetLmsData(LoginData loginData, final Context context) {
        Log.d("확인", "GetLmsData 입장");
        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<LmsData>> c = serverInterface.GetLmsData(loginData.studentNum, loginData.password);
        c.enqueue(new Callback<ArrayList<LmsData>>() {
            @Override
            public void onResponse(Call<ArrayList<LmsData>> call, Response<ArrayList<LmsData>> response) {
                ArrayList<LmsData> lmsDatas = (ArrayList) LmsData.listAll(LmsData.class);
                ArrayList<LmsData> lmsDataTemp = response.body();
                if (lmsDataTemp.size() != 0) {
                    for (int i = 0; i < lmsDataTemp.size(); i++) {
                        int status = 1;
                        LmsData receiveData = lmsDataTemp.get(i);
                        for (int j = 0; j < lmsDatas.size(); j++) {
                            LmsData tempData = lmsDatas.get(j);
                            if (receiveData.subject.equals(tempData.subject) && receiveData.content.equals(tempData.content)) {
                                status = 0;
                                break;
                            }
                        }
                        if (status == 1) {
                            receiveData.save(); //데이터 저장

                            GenerateNotification(context, receiveData.subject + " : " + receiveData.content);
                            //노티피케이션 날리기
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LmsData>> call, Throwable t) {
            }
        });

    }

    public void GenerateNotification (Context context, String message) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , new Intent (context, LmsActivity.class),
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


        notificationManager.notify(1234, builder.build());
    }

    
}
