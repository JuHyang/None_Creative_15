package ncs.com.kaulife;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kkss2 on 2017-11-15.
 */

public class LmsAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<LoginData> loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        LoginData loginDataTemp = loginDatas.get(0);
        GetLmsData(loginDataTemp);
    }


    public void GetLmsData(LoginData loginData) {
        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<LmsData>> c = serverInterface.GetLmsData(loginData);
        c.enqueue(new Callback<ArrayList<LmsData>>() {
            @Override
            public void onResponse(Call<ArrayList<LmsData>> call, Response<ArrayList<LmsData>> response) {
                ArrayList<LmsData> lmsDatas = (ArrayList) LmsData.listAll(LmsData.class);

                int status = 1;
                ArrayList<LmsData> lmsDataTemp = response.body();
                for (int i = 0; i < lmsDataTemp.size(); i ++) {
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

                        //노티피케이션 날리기
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LmsData>> call, Throwable t) {
            }
        });

    }
}
