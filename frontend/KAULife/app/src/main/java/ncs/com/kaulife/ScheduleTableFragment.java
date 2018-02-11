package ncs.com.kaulife;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by kkss2 on 2018-02-11.
 */

@SuppressLint("ValidFragment")
public class ScheduleTableFragment extends Fragment {
    Context context;

    private RecyclerView timeTableView;
    private RecyclerView.Adapter scheduleTableAdapter;
    private ArrayList<ScheduleTableData> scheduleTableDatas = new ArrayList<>();
    private ArrayList<ScheduleData> scheduleDatas;

    private Button btn_TableRefresh;

    private ArrayList<Integer> colors = new ArrayList<>();

    public ScheduleTableFragment (Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_schedule_table, container, false);
        InitModel();
        InitView(layout);
        AboutView();
        return layout;
    }

    public void onResume() {
        super.onResume();
        Log.v("tag", "Table On Resume");

        InitModel();
        scheduleTableAdapter.notifyDataSetChanged();
        AboutView();
    }

    public void InitView (View view) {
        btn_TableRefresh = view.findViewById(R.id.btn_TableRefresh);
        timeTableView = view.findViewById(R.id.tableRecyclerView);
        timeTableView.setLayoutManager(new GridLayoutManager(context, 6));

        scheduleTableAdapter = new ScheduleTableAdapter(scheduleTableDatas);
        timeTableView.setAdapter(scheduleTableAdapter);
    }

    public void InitModel () {
        colors.clear();
        colors.add(Color.rgb(234,133,188));
        colors.add(Color.rgb(233,196,106));
        colors.add(Color.rgb(162,194,106));
        colors.add(Color.rgb(216,162,212));
        colors.add(Color.rgb(122,161,220));
        colors.add(Color.rgb(245,164,101));
        colors.add(Color.rgb(129,209,191));

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
                    scheduleTableDatatemp.color = colors.get(i);
                    if (k == 0) {
                        scheduleTableDatatemp.subject = scheduleDatas.get(i).subject;
                        scheduleTableDatatemp.professor = scheduleDatas.get(i).professor;
                        if (!(room_arr[0].equals(""))) {
                            scheduleTableDatatemp.room = room_arr[j];
                        }
                    } else {
                        scheduleTableDatatemp.subject = " ";
                        scheduleTableDatatemp.professor = " ";
                        scheduleTableDatatemp.room = " ";
                    }
                }
            }
        }

    }

    public void AboutView () {
        btn_TableRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("초기화")
                        .setMessage("시간표를 초기화 하시겠습니까 ?")
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ScheduleData.deleteAll(ScheduleData.class);
                                        InitModel();
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

        timeTableView.addOnItemTouchListener(new RecyclerItemClickListener(context, timeTableView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        if (!scheduleTableDatas.get(position).subject.equals("") && !scheduleTableDatas.get(position).subject.equals(" ")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("삭제")
                                    .setMessage(scheduleTableDatas.get(position).subject + "를 시간표에서 삭제 하시겠습니까 ?")
                                    .setPositiveButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String subjectTarget = scheduleTableDatas.get(position).subject;
                                                    int colorTarget = scheduleTableDatas.get(position).color;
                                                    for (int i = 0; i < scheduleDatas.size(); i++) {
                                                        ScheduleData temp = scheduleDatas.get(i);
                                                        if (temp.subject.equals(subjectTarget)) {
                                                            temp.delete();
                                                            scheduleDatas.remove(temp);
                                                            break;
                                                        }
                                                    }
                                                    for (int i = 0; i < scheduleTableDatas.size(); i++) {
                                                        ScheduleTableData temp = scheduleTableDatas.get(i);
                                                        if (temp.color == colorTarget) {
                                                            temp.subject = "";
                                                            temp.room = "";
                                                            temp.professor = "";
                                                            temp.color = Color.rgb(240,240,240);
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
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }



}
