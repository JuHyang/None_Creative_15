package ncs.com.kaulife;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kkss2 on 2017-11-13.
 */

public class LmsAdapter extends RecyclerView.Adapter<LmsAdapter.ViewHolder> {
    private ArrayList<LmsData> lmsDatas;

    public LmsAdapter (ArrayList<LmsData> lmsDatas) {
        this.lmsDatas = lmsDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_lms, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LmsData temp = lmsDatas.get(position);
        holder.textViewTime.setText(temp.time);
        holder.textViewSubject.setText(temp.subject);
        holder.textViewContent.setText(temp.content);
    }

    @Override
    public int getItemCount() {
        return lmsDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTime;
        TextView textViewSubject;
        TextView textViewContent;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.textViewTime_lms);
            textViewSubject = itemView.findViewById(R.id.textViewSubject_lms);
            textViewContent = itemView.findViewById(R.id.textViewContent_lms);
        }
    }
}
