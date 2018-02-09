package ncs.com.kaulife;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kkss2 on 2018-01-15.
 */

public class ScheduleTableAdapter extends RecyclerView.Adapter<ScheduleTableAdapter.ViewHolder> {
    private ArrayList<ScheduleTableData> scheduleTableDatas;

    public ScheduleTableAdapter (ArrayList<ScheduleTableData> scheduleTableDatas) {
        this.scheduleTableDatas = scheduleTableDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_recyclerview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleTableData temp = scheduleTableDatas.get(position);
        holder.textViewSubject.setText(temp.subject);
        holder.textViewProfessor.setText(temp.professor);
        holder.textViewRoom.setText(temp.room);
        holder.tableView.setBackgroundColor(temp.color);
    }

    @Override
    public int getItemCount() {
        return scheduleTableDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout tableView;
        TextView textViewSubject;
        TextView textViewProfessor;
        TextView textViewRoom;
        public ViewHolder(View itemView) {
            super(itemView);
            tableView = itemView.findViewById(R.id.tableView);
            textViewSubject = itemView.findViewById(R.id.textViewSubject_table);
            textViewProfessor = itemView.findViewById(R.id.textViewProfessor_table);
            textViewRoom = itemView.findViewById(R.id.textViewRoom_table);
        }
    }
}
