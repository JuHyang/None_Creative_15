package ncs.com.kaulife;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleListActivity extends AppCompatActivity {

    ArrayList<ScheduleData> scheduleDatas = new ArrayList<>();

    RecyclerView scheduleRecyclerView;
    RecyclerView.Adapter scheduleAdapter;
    RecyclerView.LayoutManager scheduleLayoutManager;

    TextView textViewSubjectCheck, textViewGradeCheck, textViewCategoryCheck, textViewCreditCheck,
            textViewProfessorCheck, textViewMajorCheck, textViewTimeCheck, textViewRoomCheck, textViewTargetCheck;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        AboutView();

    }

    public void InitView() {
        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
    }

    public void AboutView() {
        InitView();

        Intent intent = getIntent();
        String label = intent.getStringExtra("label");

        GetScheduleData(label);

        // in content do not change the layout size of the RecyclerView
        scheduleRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        scheduleLayoutManager = new LinearLayoutManager(this);
        scheduleRecyclerView.setLayoutManager(scheduleLayoutManager);

        // specify an adapter (see also next example)
        scheduleAdapter = new ScheduleListAdpater(scheduleDatas);
        scheduleRecyclerView.setAdapter(scheduleAdapter);

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
        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<ScheduleData>> c = serverInterface.GetScheduleData(label);

        c.enqueue(new Callback<ArrayList<ScheduleData>>() {
            @Override
            public void onResponse(Call<ArrayList<ScheduleData>> call, Response<ArrayList<ScheduleData>> response) {
                scheduleDatas.addAll(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ScheduleData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버에 오류가 있습니다. 잠시 후 다시 시도해주세요",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openDialog (int position) {
        final ScheduleData temp = scheduleDatas.get(position);

        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogLayout = layoutInflater.inflate(R.layout.schedule_check_dialog, null);

        textViewSubjectCheck = dialogLayout.findViewById(R.id.textViewSubjectCheck);
        textViewGradeCheck = dialogLayout.findViewById(R.id.textViewGradeCheck);
        textViewCategoryCheck = dialogLayout.findViewById(R.id.textViewCategoryCheck);
        textViewCreditCheck = dialogLayout.findViewById(R.id.textViewCreditCheck);
        textViewProfessorCheck = dialogLayout.findViewById(R.id.textViewProfessorCheck);
        textViewMajorCheck = dialogLayout.findViewById(R.id.textViewMajorCheck);
        textViewTimeCheck = dialogLayout.findViewById(R.id.textViewTimeCheck);
        textViewRoomCheck = dialogLayout.findViewById(R.id.textViewRoomCheck);
        textViewTargetCheck = dialogLayout.findViewById(R.id.textViewTargetCheck);

        textViewSubjectCheck.setText(temp.subject);
        textViewGradeCheck.setText(String.valueOf(temp.grade));
        textViewCategoryCheck.setText(temp.category);
        textViewCreditCheck.setText(String.valueOf(temp.credit));
        textViewProfessorCheck.setText(temp.professor);
        textViewMajorCheck.setText(temp.major);
        textViewTimeCheck.setText(temp.time);
        textViewRoomCheck.setText(temp.room);
        textViewTargetCheck.setText(temp.target);

        String timeNum = ChangeTimeForm(temp.time);
        temp.timeNum = timeNum;


        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleListActivity.this);
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                temp.save();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setTitle("설정");
        builder.setView(dialogLayout);


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public String ChangeTimeForm (String time) {
        int index = 0;
        String result = "";

        String[] time_arr = time.split("/");

        for (int i = 0; i < time_arr.length; i ++) {
            if (i != 0) {
                result += "/";
            }
            String temp_time = time_arr[i];
            if (temp_time.contains("월")) {
                index = 1;
            } else if (temp_time.contains("화")) {
                index = 2;
            } else if (temp_time.contains("수")) {
                index = 3;
            } else if (temp_time.contains("목")) {
                index = 4;
            } else if (temp_time.contains("금")) {
                index = 5;
            }

            temp_time = temp_time.substring(2);

            String[] timeNum_arr = temp_time.split("∼");
            int hour;
            String[] timeHour_arr;
            timeHour_arr = timeNum_arr[0].split(":");
            hour = Integer.parseInt(timeHour_arr[0]);
            int timeNum_1 = ((hour - 9) * 2 + 1) * 6 + index;
            if (timeHour_arr[1].equals("30")) {
                timeNum_1 += 6;
            }
            timeHour_arr = timeNum_arr[1].split(":");
            hour = Integer.parseInt(timeHour_arr[0]);
            int timeNum_2 = ((hour - 9) * 2 + 1) * 6 + index;
            if (timeHour_arr[1].equals("30")) {
                timeNum_2 += 6;
            }

            for (; timeNum_1 < timeNum_2; timeNum_1 += 6) {
                result += String.valueOf(timeNum_1);
                if (timeNum_2 - timeNum_1 != 6) {
                    result += ",";
                }
            }
        }

        return result;
    }
}
