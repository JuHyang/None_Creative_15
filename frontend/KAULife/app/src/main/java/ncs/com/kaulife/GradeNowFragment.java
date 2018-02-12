package ncs.com.kaulife;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kkss2 on 2018-02-11.
 */

@SuppressLint("ValidFragment")
public class GradeNowFragment extends Fragment{
    Context context;

    private ArrayList<GradeData> gradeDatas;
    private ArrayList<LoginData> loginDatas;
    private String studentNum;
    private String password;

    private RecyclerView gradeNowRecyclerView;
    private RecyclerView.Adapter gradeNowAdapter;
    private RecyclerView.LayoutManager gradeNowLayoutManager;
    private TextView textViewSemesterNow;

    public GradeNowFragment (Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_grade_now, container, false);
        InitModel();
        GetGradeData ();
        InitView(layout);
        AboutView();
        return layout;
    }

    public void onResume() {
        super.onResume();
        InitModel();
        AboutView();
    }


    public void InitModel() {
        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        gradeDatas = (ArrayList) GradeData.listAll(GradeData.class);
    }

    public void InitView (View view) {
        gradeNowRecyclerView = view.findViewById(R.id.gradeNowRecyclerView);
        // in content do not change the layout size of the RecyclerView
        gradeNowRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        gradeNowLayoutManager = new LinearLayoutManager(context);
        gradeNowRecyclerView.setLayoutManager(gradeNowLayoutManager);

        // specify an adapter (see also next example)
        gradeNowAdapter = new GradeNowAdapter(gradeDatas);
        gradeNowRecyclerView.setAdapter(gradeNowAdapter);

        textViewSemesterNow = view.findViewById(R.id.textViewSemesterNow);
    }

    public void AboutView () {
        if (gradeDatas.size() != 0) {
            textViewSemesterNow.setText(gradeDatas.get(0).hakgi);
        }
    }

    public void GetGradeData() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


        studentNum = loginDatas.get(0).studentNum;
        password = loginDatas.get(0).password;


        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<GradeData>> c = serverInterface.GetGradeNow(studentNum, password);

        c.enqueue(new Callback<ArrayList<GradeData>>() {
            @Override
            public void onResponse(Call<ArrayList<GradeData>> call, Response<ArrayList<GradeData>> response) {
                if (gradeDatas.size() == 0) {
                    if (response.body().size() == 0) {
//                        alertDialogBuilder.setMessage("이번학기에 수강중인 과목이 없습니다.") // textView 로 변경
//                                .setCancelable(false)
//                                .setNegativeButton("확인",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            }
//                                        });
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
                    } else {
                        gradeDatas.addAll(response.body());
                        for (int i = 0; i < gradeDatas.size(); i ++) {
                            GradeData temp = gradeDatas.get(i);
                            temp.save();
                        }
                        textViewSemesterNow.setText(gradeDatas.get(0).hakgi);
                    }
                } else {
                    if (response.body().size() == 0) {
//                        alertDialogBuilder.setMessage("이번학기에 수강중인 과목이 없습니다.")// textView 로 변경
//                                .setCancelable(false)
//                                .setNegativeButton("확인",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            }
//                                        });
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
                    }
                    else {
                        if (gradeDatas.size() != response.body().size() || !(gradeDatas.get(0).hakgi.equals(response.body().get(0).hakgi))) {
                            gradeDatas.clear();
                            GradeData.deleteAll(GradeData.class);
                            gradeDatas.addAll(response.body());
                            for (int i = 0; i < gradeDatas.size(); i ++ ) {
                                GradeData temp = gradeDatas.get(i);
                                temp.save();
                            }
                            textViewSemesterNow.setText(gradeDatas.get(0).hakgi);
                        } else {
                            for (int i = 0; i < gradeDatas.size(); i ++) {
                                GradeData temp = gradeDatas.get(i);
                                if (!(temp.grade.equals(response.body().get(i).grade))) {
                                    temp.grade = response.body().get(i).grade;
                                    temp.save();
                                }
                            }
                        }
                    }
                }
                gradeNowAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ArrayList<GradeData>> call, Throwable t) {
                alertDialogBuilder.setMessage("현재 서버에 오류가 있습니다. 잠시 후 다시 시도해 주세요")
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
