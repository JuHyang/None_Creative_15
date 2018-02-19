package ncs.com.kaulife;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleListActivity extends AppCompatActivity {

    private ArrayList<ScheduleData> scheduleDatas;
    private ArrayList<ScheduleData> scheduleDatasAlready;
    private String label;

    private RecyclerView scheduleRecyclerView;
    private RecyclerView.Adapter scheduleAdapter;
    private RecyclerView.LayoutManager scheduleLayoutManager;

    private TextView textViewSubjectCheck, textViewGradeCheck, textViewCategoryCheck, textViewCreditCheck,
            textViewProfessorCheck, textViewMajorCheck, textViewTimeCheck, textViewRoomCheck, textViewTargetCheck;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        InitModel();
        InitView();
        AboutView();

    }

    public void InitModel () {
        scheduleDatas = new ArrayList<> ();
        Intent intent = getIntent();
        label = intent.getStringExtra("label");
        GetScheduleData(label);
    }

    public void InitView() {
        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
        // in content do not change the layout size of the RecyclerView
        scheduleRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        scheduleLayoutManager = new LinearLayoutManager(this);
        scheduleRecyclerView.setLayoutManager(scheduleLayoutManager);

        // specify an adapter (see also next example)
        scheduleAdapter = new ScheduleListAdpater(scheduleDatas);
        scheduleRecyclerView.setAdapter(scheduleAdapter);
    }

    public void AboutView() {
        scheduleRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), scheduleRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openDialog(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    public void GetScheduleData(String label) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<ScheduleData>> c = serverInterface.GetScheduleData(label);

        c.enqueue(new Callback<ArrayList<ScheduleData>>() {
            @Override
            public void onResponse(Call<ArrayList<ScheduleData>> call, Response<ArrayList<ScheduleData>> response) {
                scheduleDatas.addAll(response.body());
                scheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<ScheduleData>> call, Throwable t) {
                alertDialogBuilder.setMessage("서버에 오류가 있습니다. 잠시 후 다시 시도해주세요")
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
    public void openDialog (int position) {
        final ScheduleData temp = scheduleDatas.get(position);

        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogLayout = layoutInflater.inflate(R.layout.dialog_schedule_check, null);

        textViewSubjectCheck = dialogLayout.findViewById(R.id.textViewSubjectCheck);
        textViewGradeCheck = dialogLayout.findViewById(R.id.textViewGradeCheck);
        textViewCategoryCheck = dialogLayout.findViewById(R.id.textViewCategoryCheck);
        textViewCreditCheck = dialogLayout.findViewById(R.id.textViewCreditCheck);
        textViewProfessorCheck = dialogLayout.findViewById(R.id.textViewProfessorCheck);
        textViewMajorCheck = dialogLayout.findViewById(R.id.textViewMajorCheck);
        textViewTimeCheck = dialogLayout.findViewById(R.id.textViewTimeCheck);
        textViewRoomCheck = dialogLayout.findViewById(R.id.textViewRoomCheck);
        textViewTargetCheck = dialogLayout.findViewById(R.id.textViewTargetCheck);

        textViewSubjectCheck.setText("과목명 : " + temp.subject);
        textViewGradeCheck.setText("학년 : " + String.valueOf(temp.grade));
        textViewCategoryCheck.setText("이수 구분 : " + temp.category);
        textViewCreditCheck.setText("학점 : " + String.valueOf(temp.credit));
        textViewProfessorCheck.setText("주담당 교수 : " + temp.professor);
        textViewMajorCheck.setText("개설학과(전공) : " + temp.major);
        textViewTimeCheck.setText("교시 : " + temp.time);
        textViewRoomCheck.setText("강의실 : " + temp.room);
        textViewTargetCheck.setText("수강대상 전공 : " + temp.target);

        temp.ChangeTimeForm();

        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleListActivity.this);
        if (CheckIndex(temp.subject, temp.timeNum)) {
            builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Toast.makeText(getApplicationContext(), "시간표에 등록되었습니다", Toast.LENGTH_SHORT).show();
                    temp.save();
                }
            });
        }
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setTitle("등록");
        builder.setView(dialogLayout);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



    public boolean CheckIndex (String subject, String timeNumInput) {
        if (timeNumInput.equals("")){
            return false;
        }
        ArrayList<Integer> inputIndex = new ArrayList<>();
        String[] timeNumInput_arr = timeNumInput.split("/");
        for (int i = 0; i < timeNumInput_arr.length; i ++) {
            String[] timeNumInput_temp = timeNumInput_arr[i].split(",");
            for (int j = 0; j < timeNumInput_temp.length; j ++) {
                inputIndex.add(Integer.parseInt(timeNumInput_temp[i]));
            }
        }

        scheduleDatasAlready = (ArrayList) ScheduleData.listAll(ScheduleData.class);
        for (int i = 0; i < scheduleDatasAlready.size(); i ++) {
            if (scheduleDatasAlready.get(i).subject.equals(subject)) {
                return false;
            }
            String timeNum = scheduleDatasAlready.get(i).timeNum;
            String[] timeNum_arr = timeNum.split("/");
            for (int j = 0; j < timeNum_arr.length; j ++) {
                String[] temp_arr = timeNum_arr[j].split(",");
                ArrayList<Integer> indexList = new ArrayList<> ();
                for (int k = 0; k < temp_arr.length; k++) {
                    indexList.add(Integer.parseInt(temp_arr[k]));
                }
                for (int k = 0; k < indexList.size(); k++) {
                    for (int o = 0; o < inputIndex.size(); o ++) {
                        if (indexList.get(k) == inputIndex.get(o)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
