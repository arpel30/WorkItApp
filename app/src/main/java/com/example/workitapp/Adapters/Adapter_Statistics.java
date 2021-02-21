package com.example.workitapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workitapp.More.Constants;
import com.example.workitapp.Objects.Worker;
import com.example.workitapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Statistics extends RecyclerView.Adapter<Adapter_Statistics.MyViewHolder> {
    private List<Worker> workers;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;

    public Adapter_Statistics(Context context, List<Worker> _workers) {
        this.mInflater = LayoutInflater.from(context);
        this.workers = _workers;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.statistics_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Worker w = workers.get(position);
        holder.statItem_LBL_name.setText(w.getName());
        holder.statItem_LBL_count.setText("" + w.getAssignmentsDoneWeek());
        if (!w.getImgUrl().equals(Constants.DEFAULT))
            Glide
                    .with(mInflater.getContext())
                    .load(w.getImgUrl())
                    .fitCenter()
                    .into(holder.statItem_IMG_pic);
        else
            holder.statItem_IMG_pic.setImageResource(Constants.PROFILE_DEFAULT); // vector drawable
    }

    @Override
    public int getItemCount() {
        return workers.size();
    }

    Worker getItem(int id) {
        return workers.get(id);
    }

    public void setClickListener(MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface MyItemClickListener {
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView statItem_LBL_name;
        TextView statItem_LBL_count;
        CircleImageView statItem_IMG_pic;

        MyViewHolder(View itemView) {
            super(itemView);
            statItem_LBL_name = itemView.findViewById(R.id.statItem_LBL_name);
            statItem_LBL_count = itemView.findViewById(R.id.statItem_LBL_count);
            statItem_IMG_pic = itemView.findViewById(R.id.statItem_IMG_pic);
        }
    }
}
