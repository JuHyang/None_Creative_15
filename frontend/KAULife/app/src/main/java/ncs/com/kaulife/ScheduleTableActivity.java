package ncs.com.kaulife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.net.Inet4Address;
import java.util.ArrayList;

public class ScheduleTableActivity extends AppCompatActivity {

    private RecyclerView timeTableView;
    private RecyclerView.Adapter scheduleTableAdapter;
    private ArrayList<ScheduleTableData> scheduleTableDatas = new ArrayList<>();
    private ArrayList<ScheduleData> scheduleDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_table);

        InitModel();
        AboutView();
    }

    public void InitView () {
        timeTableView = findViewById(R.id.tableRecyclerView);
        timeTableView.setLayoutManager(new GridLayoutManager(this, 6));
    }

    public void InitModel () {

        int max = 0;
        scheduleDatas = (ArrayList) ScheduleData.listAll(ScheduleData.class);
        for (int i = 0; i < scheduleDatas.size(); i ++) {
            String timeNum = scheduleDatas.get(i).timeNum;
            String[] timeNum_arr = timeNum.split("/");
            for (int j = 0; j < timeNum_arr.length; j ++) {
                String[] temp_arr = timeNum_arr[j].split(",");
                for (int k = 0; k < temp_arr.length; k++) {
                    if (Integer.parseInt(temp_arr[k]) > max) {
                        max = Integer.parseInt(temp_arr[k]);
                    }
                }
            }
        }

        max = max / 6;

        scheduleTableDatas.add(new ScheduleTableData (""));
        scheduleTableDatas.add(new ScheduleTableData ("월"));
        scheduleTableDatas.add(new ScheduleTableData ("화"));
        scheduleTableDatas.add(new ScheduleTableData ("수"));
        scheduleTableDatas.add(new ScheduleTableData ("목"));
        scheduleTableDatas.add(new ScheduleTableData ("금"));

        for (int i = 0; i < 6; i ++) {
            for (int j = 0; j < max ; j ++ ) {
                scheduleTableDatas.add(new ScheduleTableData ());
            }
        }

        for (int i = 1; i < scheduleTableDatas.size(); i ++) {
            if (i % 6 == 0 && (i / 6) % 2 == 1) {
                ScheduleTableData temp = scheduleTableDatas.get(i);
                temp.subject = String.valueOf(((i / 6) - 1) / 2 + 9);
            }
        }

    }

    public void FillList () {
        for (int i = 0; i < scheduleDatas.size(); i ++) {
            String timeNum = scheduleDatas.get(i).timeNum;
            String[] timeNum_arr = timeNum.split("/");
            String room = scheduleDatas.get(i).room;
            String[] room_arr = room.split("/");
            for (int j = 0; j < timeNum_arr.length; j ++) {
                String[] temp_arr = timeNum_arr[j].split(",");
                ArrayList<Integer> indexList = new ArrayList<> ();
                for (int k = 0; k < temp_arr.length; k++) {
                   indexList.add(Integer.parseInt(temp_arr[k]));
                }
                for (int k = 0; k < indexList.size(); k++) {
                    ScheduleTableData scheduleTableDatatemp = scheduleTableDatas.get(indexList.get(k));
                    scheduleTableDatatemp.subject = scheduleDatas.get(i).subject;
                    scheduleTableDatatemp.professor = scheduleDatas.get(i).professor;
                    scheduleTableDatatemp.room = room_arr[j];
                }
            }
        }

    }

    public void AboutView () {
        InitView();
        FillList();

        scheduleTableAdapter = new ScheduleTableAdapter(scheduleTableDatas);
        timeTableView.setAdapter(scheduleTableAdapter);
    }
}
