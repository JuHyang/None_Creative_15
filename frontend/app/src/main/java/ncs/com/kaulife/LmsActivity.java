package ncs.com.kaulife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LmsActivity extends Activity {
    TextView textView1;
    TextView textView2;
    ArrayList<LmsData> lmsDatas;
    ArrayList<LoginData> loginDatas;
    LoginData loginData;
    String studentNum;
    String password;
    Boolean auto;

    private RecyclerView lmsRecyclerView;
    private RecyclerView.Adapter lmsAdapter;
    private RecyclerView.LayoutManager lmsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lms);

        aboutView();
    }

    public void initview() {
        lmsRecyclerView = findViewById(R.id.lmsRecyclerView);
        textView1 = findViewById(R.id.textViewId);
        textView2 = findViewById(R.id.textViewPwd);
    }

    public void aboutView() {
        initview();

        loginDatas = (ArrayList) LoginData.listAll(LoginData.class);
        lmsDatas = (ArrayList) LmsData.listAll(LmsData.class);

        if (loginDatas.size() == 0) {
            Intent intent = getIntent();
            studentNum = intent.getStringExtra("id");
            password = intent.getStringExtra("password");
            loginData = new LoginData(studentNum, password);
            auto = false;
        } else {
            loginData = loginDatas.get(0);
            auto = true;
        }

        GetLmsData(loginData, auto);

        Collections.reverse(lmsDatas);

       //여기부터 리스트뷰 들어가면 끝
        // in content do not change the layout size of the RecyclerView
        lmsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        lmsLayoutManager = new LinearLayoutManager(this);
        lmsRecyclerView.setLayoutManager(lmsLayoutManager);

        // specify an adapter (see also next example)
        lmsAdapter = new LmsAdapter(lmsDatas);
        lmsRecyclerView.setAdapter(lmsAdapter);
    }

    public void GetLmsData(LoginData loginData, final Boolean auto) {
        ServerInterface serverInterface = new Repo().getService();
        Call<ArrayList<LmsData>> c = serverInterface.GetLmsData(loginData);
        c.enqueue(new Callback<ArrayList<LmsData>>() {
            @Override
            public void onResponse(Call<ArrayList<LmsData>> call, Response<ArrayList<LmsData>> response) {
                ArrayList<LmsData> lmsDataTemp = response.body();
                for (int i = 0; i < lmsDataTemp.size(); i ++) {
                    LmsData temp = lmsDataTemp.get(i);
                    lmsDatas.add(temp);
                    if (auto) {
                        temp.save();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LmsData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버에 오류가 있습니다. 잠시 후 다시 시도해주세요",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
