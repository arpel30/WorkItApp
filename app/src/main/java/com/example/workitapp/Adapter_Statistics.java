package com.example.workitapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.adapters.ImageViewBindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workitapp.Objects.Assignment;
import com.example.workitapp.Objects.Worker;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Statistics extends RecyclerView.Adapter<Adapter_Statistics.MyViewHolder> {
    private List<Worker> workers;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_Statistics(Context context, List<Worker> _workers) {
        this.mInflater = LayoutInflater.from(context);
        this.workers = _workers;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.statistics_item, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("pttt", "Position = " + position);
        Worker w = workers.get(position);
        holder.statItem_LBL_name.setText(w.getName());
        holder.statItem_LBL_count.setText("" + w.getAssignmentsDoneWeek());
        Glide
                .with(mInflater.getContext())
                .load(R.drawable.unauth_pic)
                .into(holder.statItem_IMG_pic);

//        glide : holder.statItem_IMG_pic

//        holder.assignItemW_LAY_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mClickListener != null) {
//                    mClickListener.onItemClicked(v, a);
//                }
//            }
//        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return workers.size();
    }

    // convenience method for getting data at click position
    Worker getItem(int id) {
        return workers.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface MyItemClickListener {
//        void onItemClicked(View v, int a); // open popup
//        void onFinishAssignment(View v, Assignment a); // finish assignment
    }



    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView statItem_LBL_name;
        TextView statItem_LBL_count;
        CircleImageView statItem_IMG_pic;

        MyViewHolder(View itemView) {
            super(itemView);
            statItem_LBL_name = itemView.findViewById(R.id.statItem_LBL_name);
            statItem_LBL_count = itemView.findViewById(R.id.statItem_LBL_count);
            statItem_IMG_pic = itemView.findViewById(R.id.statItem_IMG_pic);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mClickListener != null) {
//                        mClickListener.onItemClicked(view, getAdapterPosition());
//                    }
//                }
//            });
        }
    }

}
