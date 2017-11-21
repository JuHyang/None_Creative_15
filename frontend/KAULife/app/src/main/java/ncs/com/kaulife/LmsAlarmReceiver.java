package ncs.com.kaulife;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
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
    private Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "알람리시버", Toast.LENGTH_LONG).show();
        ArrayList<LoginData> loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        LoginData loginDataTemp = loginDatas.get(0);
        if (Login(loginDataTemp).equals("1")) {
            GetLmsData(loginDataTemp, context);
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


    public void GetLmsData(LoginData loginData, final Context context) {
        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<LmsData>> c = serverInterface.GetLmsData(loginData);
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
                            if (receiveData.time == tempData.time && receiveData.subject == tempData.subject && receiveData.content == tempData.content) {
                                status = 0;
                                break;
                            }
                        }
                        if (status == 1) {
                            receiveData.save(); //데이터 저장

                            GenerateNotification(receiveData, context);
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

    public void GenerateNotification (LmsData lmsData, Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , new Intent (context, LmsActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification = new NotificationCompat.Builder(context)
                .setContentTitle (lmsData.time)
                .setContentText(lmsData.subject + " : " + lmsData.content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(1234, notification);
    }

    
}
