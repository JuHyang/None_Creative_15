package ncs.com.kaulife;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kkss2 on 2018-01-24.
 */

public class GradeNowAdapter extends RecyclerView.Adapter<GradeNowAdapter.ViewHolder> {

    private ArrayList<GradeData> gradeDatas;

    public GradeNowAdapter (ArrayList<GradeData> gradeDatas) {
        this.gradeDatas = gradeDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grade_now_recyclerview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GradeData temp = gradeDatas.get(position);
        holder.textViewSubjectNow.setText(temp.subject);
        holder.textViewCreditNow.setText(String.valueOf(temp.credit));
        holder.textViewGradeNow.setText(temp.grade);
    }

    @Override
    public int getItemCount() {
        return gradeDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSubjectNow;
        TextView textViewCreditNow;
        TextView textViewGradeNow;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewSubjectNow = itemView.findViewById(R.id.textViewSubjectNow);
            textViewCreditNow = itemView.findViewById(R.id.textViewCreditNow);
            textViewGradeNow = itemView.findViewById(R.id.textViewGradeNow);

        }
    }


}
