package ncs.com.kaulife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScheduleEditActivity extends AppCompatActivity {

    private String[] time = {"9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00" };
    private String[] day = {"월", "화", "수", "목", "금"};

    private Spinner spinnerTimeFirstStart, spinnerTimeFirstEnd, spinnerTimeSecondStart, spinnerTimeSecondEnd, spinnerDayFirst, spinnerDaySecond;
    private EditText editTextRoomFirst, editTextRoomSecond;
    private TextView textViewTimeSecond, textViewTempSecond, textViewRoomSecond;
    private Button btnSave;

    private ArrayList<ScheduleData> scheduleDatas;
    private ScheduleData targetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);


        InitModel();
        InitView();
        AboutView();
    }

    public void InitModel () {
        scheduleDatas = (ArrayList) ScheduleData.listAll(ScheduleData.class);
        Intent intent = getIntent();
        String subject = intent.getStringExtra("subject");

        for (int i = 0; i < scheduleDatas.size(); i ++) {
            ScheduleData temp = scheduleDatas.get(i);
            if (temp.subject.equals(subject)) {
                targetData = temp;
                break;
            }
        }



    }

    public void InitView () {
        spinnerTimeFirstStart = findViewById(R.id.spinnerTimeFirstStart);
        spinnerTimeFirstEnd = findViewById(R.id.spinnerTimeFirstEnd);
        spinnerTimeSecondStart = findViewById(R.id.spinnerTimeSecondStart);
        spinnerTimeSecondEnd = findViewById(R.id.spinnerTimeSecondEnd);
        spinnerDayFirst = findViewById(R.id.spinnerDayFirst);
        spinnerDaySecond = findViewById(R.id.spinnerDaySecond);
        editTextRoomFirst = findViewById(R.id.editTextRoomFirst);
        editTextRoomSecond = findViewById(R.id.editTextRoomSecond);
        textViewTimeSecond = findViewById(R.id.textViewTimeSecond);
        textViewTempSecond = findViewById(R.id.textViewTempSecond);
        textViewRoomSecond = findViewById(R.id.textViewRoomSecond);
        btnSave = findViewById(R.id.btnSave);
    }

    public void AboutView() {

        ArrayAdapter<String> firstStartTimeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                time);
        spinnerTimeFirstStart.setAdapter(firstStartTimeAdapter);

        ArrayAdapter<String> firstEndTimeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                time);
        spinnerTimeFirstEnd.setAdapter(firstEndTimeAdapter);

        ArrayAdapter<String> SecondStartTimeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                time);
        spinnerTimeSecondStart.setAdapter(SecondStartTimeAdapter);

        ArrayAdapter<String> SecondEndTimeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                time);
        spinnerTimeSecondEnd.setAdapter(SecondEndTimeAdapter);

        ArrayAdapter<String> FirstDayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                day);
        spinnerDayFirst.setAdapter(FirstDayAdapter);

        ArrayAdapter<String> SecondDayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                day);
        spinnerDaySecond.setAdapter(SecondDayAdapter);


        String room_arr[] = targetData.room.split("/");
        if (!targetData.room.equals("")) {
            editTextRoomFirst.setText(room_arr[0]);
        } else {
            editTextRoomFirst.setText("");
        }
        String time_arr[] = targetData.time.split("/");

        int index = 0;
        String temp_time = time_arr[0];
        if (temp_time.contains("월")) {
            index = 0;
        } else if (temp_time.contains("화")) {
            index = 1;
        } else if (temp_time.contains("수")) {
            index = 2;
        } else if (temp_time.contains("목")) {
            index = 3;
        } else if (temp_time.contains("금")) {
            index = 4;
        }
        spinnerDayFirst.setSelection(index);

        index = 0;

        temp_time = temp_time.substring(2);

        String[] time_mid = temp_time.split("∼");

        for (int i = 0; i < time.length; i++) {
            if (time[i].equals(time_mid[0])) {
                spinnerTimeFirstStart.setSelection(i);
            }

            if (time[i].equals(time_mid[1])) {
                spinnerTimeFirstEnd.setSelection(i);
            }
        }

        final String[] result = {"", "", "", "", "", "", "", ""};

        spinnerDayFirst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result[0] = day[position] + ")";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTimeFirstStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result[1] = time[position] + "∼";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTimeFirstEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result[2] = time[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (!targetData.time.contains("/")) {
            spinnerTimeSecondStart.setVisibility(View.GONE);
            spinnerTimeSecondEnd.setVisibility(View.GONE);
            spinnerDaySecond.setVisibility(View.GONE);
            editTextRoomSecond.setVisibility(View.GONE);
            textViewTimeSecond.setVisibility(View.GONE);
            textViewTempSecond.setVisibility(View.GONE);
            textViewRoomSecond.setVisibility(View.GONE);
        } else {
            if (!targetData.room.equals("")) {
                editTextRoomSecond.setText(room_arr[1]);
            } else {
                editTextRoomSecond.setText("");
            }

            temp_time = time_arr[1];
            if (temp_time.contains("월")) {
                index = 0;
            } else if (temp_time.contains("화")) {
                index = 1;
            } else if (temp_time.contains("수")) {
                index = 2;
            } else if (temp_time.contains("목")) {
                index = 3;
            } else if (temp_time.contains("금")) {
                index = 4;
            }

            temp_time = temp_time.substring(2);

            time_mid = temp_time.split("∼");

            for (int i = 0; i < time.length; i++) {
                if (time[i].equals(time_mid[0])) {
                    spinnerTimeSecondStart.setSelection(i);
                }

                if (time[i].equals(time_mid[1])) {
                    spinnerTimeSecondEnd.setSelection(i);
                }
            }
            spinnerDaySecond.setSelection(index);

            spinnerDaySecond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    result[3] = "/" + day[position] + ")";
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerTimeSecondStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    result[4] = time[position] + "∼";
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerTimeSecondEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    result[5] = time[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result[6] = editTextRoomFirst.getText().toString();
                if (targetData.time.contains("/")) {
                    result[7] = "/" + editTextRoomSecond.getText().toString();
                }
                String resultTime = result[0] + result[1] + result[2] + result[3] + result[4] + result[5];
                String resultRoom = result[6] + result[7];
                if (resultRoom.equals("/")) {
                    resultRoom = "";
                }
                targetData.time = resultTime;
                targetData.room = resultRoom;
                targetData.ChangeTimeForm();
                targetData.save();
                Toast.makeText(ScheduleEditActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

}
