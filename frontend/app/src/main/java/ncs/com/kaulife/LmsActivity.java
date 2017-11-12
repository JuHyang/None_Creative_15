package ncs.com.kaulife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class LmsActivity extends Activity {
    TextView textView1;
    TextView textView2;
    ArrayList<LoginData> loginDatas;
    String studentNum;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lms);

        aboutView();
    }

    public void initview() {
        textView1 = findViewById(R.id.textViewId);
        textView2 = findViewById(R.id.textViewPwd);
    }

    public void aboutView() {
        initview();

        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);

        if (loginDatas.size() == 0) {
            Intent intent = getIntent();
            studentNum = intent.getStringExtra("id");
            password = intent.getStringExtra("password");
        } else {
            LoginData temp = loginDatas.get(0);
            studentNum = temp.studentNum;
            password = temp.password;
        }

        textView1.setText(studentNum);
        textView2.setText(password);
    }
}
