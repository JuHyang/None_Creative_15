package ncs.com.kaulife;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kkss2 on 2018-01-19.
 */

public class ScheduleListAdpater extends RecyclerView.Adapter<ScheduleListAdpater.ViewHolder> {
    private ArrayList<ScheduleData> scheduleDatas;

    public ScheduleListAdpater (ArrayList<ScheduleData> scheduleDatas) {
        this.scheduleDatas = scheduleDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_list_recyclerview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleData temp = scheduleDatas.get(position);
        holder.textViewSubjectList.setText(temp.subject);
        holder.textViewCategoryList.setText(temp.category);
        holder.textViewCreditList.setText(String.valueOf(temp.credit));
        holder.textViewProfessorList.setText(temp.professor);
        holder.textViewTimeList.setText(temp.time);
        holder.textViewRoomList.setText(temp.room);
        if (temp.major.equals("인문자연학부")) {
            holder.textViewTargetList.setText(temp.target);
        }
    }

    @Override
    public int getItemCount() {
        return scheduleDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSubjectList;
        TextView textViewCategoryList;
        TextView textViewCreditList;
        TextView textViewProfessorList;
        TextView textViewTimeList;
        TextView textViewRoomList;
        TextView textViewTargetList;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewSubjectList = itemView.findViewById(R.id.textViewSubjectList);
            textViewCategoryList = itemView.findViewById(R.id.textViewCategoryList);
            textViewCreditList = itemView.findViewById(R.id.textViewCreditList);
            textViewProfessorList = itemView.findViewById(R.id.textViewProfessorList);
            textViewTimeList = itemView.findViewById(R.id.textViewTimeList);
            textViewRoomList = itemView.findViewById(R.id.textViewRoomList);
            textViewTargetList = itemView.findViewById(R.id.textViewTargetList);
        }
    }
}
