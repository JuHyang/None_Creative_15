package ncs.com.kaulife;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kkss2 on 2018-02-11.
 */

@SuppressLint("ValidFragment")
public class LmsFragment extends Fragment {
    Context context;

    private ArrayList<LmsData> lmsDatas;
    private ArrayList<LoginData> loginDatas;
    private LoginData loginData;
    private String studentNum;
    private String password;
    private Boolean auto;

    private RecyclerView lmsRecyclerView;
    private RecyclerView.Adapter lmsAdapter;
    private RecyclerView.LayoutManager lmsLayoutManager;

    public LmsFragment (Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_lms, container, false);
        InitModel();
        InitView(layout);
        GetLmsData(loginData, auto);
        AboutView();
        return layout;
    }


    public void onResume() {
        super.onResume();
        InitModel();
        AboutView();
    }

    public void InitView(View view) {
        lmsRecyclerView = view.findViewById(R.id.lmsRecyclerView);
        // in content do not change the layout size of the RecyclerView
        lmsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        lmsLayoutManager = new LinearLayoutManager(context);
        lmsRecyclerView.setLayoutManager(lmsLayoutManager);

        // specify an adapter (see also next example)
        lmsAdapter = new LmsAdapter(lmsDatas);
        lmsRecyclerView.setAdapter(lmsAdapter);
    }

    public void AboutView() {}

    public void InitModel () {
        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        lmsDatas = (ArrayList) LmsData.listAll(LmsData.class);

        if (lmsDatas.size() == 0) {
            lmsDatas.add(new LmsData("", "", "알림 정보가 없습니다"));
        }

        Collections.reverse(lmsDatas);

        loginData = loginDatas.get(0);
        auto = true;
    }

    public void GetLmsData(LoginData loginData, final Boolean auto) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        final ProgressDialog dialog = ProgressDialog.show(context, "", "잠시만 기다려주세요", true, true);
//        dialog.setCancelable(false);
//        dialog.show();
        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<LmsData>> c = serverInterface.GetLmsData(loginData.studentNum, loginData.password);
        c.enqueue(new Callback<ArrayList<LmsData>>() {
            @Override
            public void onResponse(Call<ArrayList<LmsData>> call, Response<ArrayList<LmsData>> response) {
//                dialog.dismiss();
                ArrayList<LmsData> lmsDataTemp = response.body();
                if (lmsDataTemp.size() != 0) {
                    if (lmsDatas.get(0).content.equals("알림 정보가 없습니다")) {
                        lmsDatas.clear();
                    }

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
                            if (receiveData.content.contains("[과제]")) {
                                receiveData.time = "마감 " + receiveData.time;
                            }
                            lmsDatas.add(0, receiveData);
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
//                dialog.dismiss();
                alertDialogBuilder.setMessage("서버에 오류가 있습니다. 잠시 후 다시 시도해주세요")
                        .setCancelable(false)
                        .setNegativeButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

}
