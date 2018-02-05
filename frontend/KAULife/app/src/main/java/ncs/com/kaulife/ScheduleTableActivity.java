package ncs.com.kaulife;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.net.Inet4Address;
import java.util.ArrayList;

public class ScheduleTableActivity extends AppCompatActivity {

    private RecyclerView timeTableView;
    private RecyclerView.Adapter scheduleTableAdapter;
    private ArrayList<ScheduleTableData> scheduleTableDatas = new ArrayList<>();
    private ArrayList<ScheduleData> scheduleDatas;

    private Button btn_TableRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_table);

        InitModel();
        InitView();
        AboutView();
    }

    public void InitView () {
        btn_TableRefresh = findViewById(R.id.btn_TableRefresh);
        timeTableView = findViewById(R.id.tableRecyclerView);
        timeTableView.setLayoutManager(new GridLayoutManager(this, 6));

        scheduleTableAdapter = new ScheduleTableAdapter(scheduleTableDatas);
        timeTableView.setAdapter(scheduleTableAdapter);
    }

    public void InitModel () {
        int max = 0;

        scheduleTableDatas.clear();
        scheduleDatas = (ArrayList) ScheduleData.listAll(ScheduleData.class);

        scheduleTableDatas.add(new ScheduleTableData (""));
        scheduleTableDatas.add(new ScheduleTableData ("월"));
        scheduleTableDatas.add(new ScheduleTableData ("화"));
        scheduleTableDatas.add(new ScheduleTableData ("수"));
        scheduleTableDatas.add(new ScheduleTableData ("목"));
        scheduleTableDatas.add(new ScheduleTableData ("금"));

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

        FillList();

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
                    if (!(room_arr[0].equals(""))) {
                        scheduleTableDatatemp.room = room_arr[j];
                    }
                }
            }
        }

    }

    public void AboutView () {
        btn_TableRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduleTableActivity.this);
                alertDialogBuilder.setTitle("초기화")
                        .setMessage("시간표를 초기화 하시겠습니까 ?")
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ScheduleData.deleteAll(ScheduleData.class);
                                        InitModel();
//                                        scheduleDatas.clear();
//                                        scheduleTableDatas.clear();
//                                        scheduleTableDatas.add(new ScheduleTableData (""));
//                                        scheduleTableDatas.add(new ScheduleTableData ("월"));
//                                        scheduleTableDatas.add(new ScheduleTableData ("화"));
//                                        scheduleTableDatas.add(new ScheduleTableData ("수"));
//                                        scheduleTableDatas.add(new ScheduleTableData ("목"));
//                                        scheduleTableDatas.add(new ScheduleTableData ("금"));
                                        scheduleTableAdapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        timeTableView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), timeTableView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduleTableActivity.this);
                        alertDialogBuilder.setTitle("삭제")
                                .setMessage(scheduleTableDatas.get(position).subject + "를 시간표에서 삭제 하시겠습니까 ?")
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String subjectTarget = scheduleTableDatas.get(position).subject;
                                                for (int i = 0; i < scheduleDatas.size(); i ++) {
                                                    ScheduleData temp = scheduleDatas.get(i);
                                                    if (temp.subject.equals(subjectTarget)) {
                                                        temp.delete();
                                                        scheduleDatas.remove(temp);
                                                        break;
                                                    }
                                                }
                                                for (int i = 0; i < scheduleTableDatas.size() ; i ++) {
                                                    ScheduleTableData temp = scheduleTableDatas.get(i);
                                                    if (temp.subject.equals(subjectTarget)) {
                                                        temp.subject = "";
                                                        temp.room = "";
                                                        temp.professor = "";
                                                    }
                                                }
                                                scheduleTableAdapter.notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("취소",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }
}
