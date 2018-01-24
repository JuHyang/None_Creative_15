package ncs.com.kaulife;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private Button btnLms, btnMajor, btnTimetable, btnGradeNow;
    private ArrayList<LoginData> loginDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        InitView();
        AboutView();

    }

    public void InitView() {
        btnLms = findViewById(R.id.btn_Lms);
        btnMajor = findViewById(R.id.btn_Major);
        btnTimetable = findViewById(R.id.btn_Timetable);
        btnGradeNow = findViewById(R.id.btn_GradeNow);
    }

    public void AboutView () {
        btnLms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDatas = (ArrayList) LoginData.listAll(LoginData.class);

                Intent intent = new Intent (getApplicationContext(), LmsActivity.class);
                if (loginDatas.size() == 0) {
                    Intent beforeIntent = getIntent();
                    String studentNum = beforeIntent.getStringExtra("id");
                    String password = beforeIntent.getStringExtra("password");

                    intent.putExtra("id", studentNum);
                    intent.putExtra("password", password);
                }

                startActivity(intent);
            }
        });

        btnMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), ScheduleMajorActivity.class);
                startActivity(intent);
            }
        });

        btnTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), ScheduleTableActivity.class);
                startActivity(intent);
            }
        });

        btnGradeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDatas = (ArrayList) LoginData.listAll(LoginData.class);

                Intent intent = new Intent (getApplicationContext(), GradeNowActivity.class);
                if (loginDatas.size() == 0) {
                    Intent beforeIntent = getIntent();
                    String studentNum = beforeIntent.getStringExtra("id");
                    String password = beforeIntent.getStringExtra("password");

                    intent.putExtra("id", studentNum);
                    intent.putExtra("password", password);
                }

                startActivity(intent);
            }
        });
    }
}
