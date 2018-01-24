package ncs.com.kaulife;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradeNowActivity extends AppCompatActivity {
    private ArrayList<GradeData> gradeDatas;
    private ArrayList<LoginData> loginDatas;
    private String studentNum;
    private String password;

    private RecyclerView gradeNowRecyclerView;
    private RecyclerView.Adapter gradeNowAdapter;
    private RecyclerView.LayoutManager gradeNowLayoutManager;
    private TextView textViewSemesterNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_now);

        InitModel();
        InitView();
        AboutView();
    }
    public void InitModel() {
        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        gradeDatas = (ArrayList) GradeData.listAll(LoginData.class);

        GetGradeData ();

    }

    public void InitView () {
        gradeNowRecyclerView = findViewById(R.id.gradeNowRecyclerView);
        // in content do not change the layout size of the RecyclerView
        gradeNowRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        gradeNowLayoutManager = new LinearLayoutManager(this);
        gradeNowRecyclerView.setLayoutManager(gradeNowLayoutManager);

        // specify an adapter (see also next example)
        gradeNowAdapter = new GradeNowAdapter(gradeDatas);
        gradeNowRecyclerView.setAdapter(gradeNowAdapter);

        textViewSemesterNow = findViewById(R.id.textViewSemesterNow);
    }

    public void AboutView () {}

    public void GetGradeData() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());

        if (loginDatas.size() == 0) {
            Intent intent = getIntent();
            studentNum = intent.getStringExtra("id");
            password = intent.getStringExtra("password");
        } else {
            studentNum = loginDatas.get(0).studentNum;
            password = loginDatas.get(0).password;
        }

        final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려주세요", true, true);
        dialog.show();
        dialog.setCancelable(false);

        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<GradeData>> c = serverInterface.GetGradeNow(studentNum, password);

        c.enqueue(new Callback<ArrayList<GradeData>>() {
            @Override
            public void onResponse(Call<ArrayList<GradeData>> call, Response<ArrayList<GradeData>> response) {
                if (gradeDatas.size() == 0) {
                    if (response.body().size() == 0) {
                        dialog.dismiss();
                        alertDialogBuilder.setMessage("이번학기에 수강중인 과목이 없습니다.")
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
                        dialog.dismiss();
                        alertDialogBuilder.setMessage("이번학기에 수강중인 과목이 없습니다.")
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
                dialog.dismiss();
                alertDialogBuilder.setMessage("현재 서버에 오류가 있습니다. 잠시 후 다시 시도해 주세요")
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

