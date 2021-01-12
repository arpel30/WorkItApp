package com.example.workitapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.workitapp.Objects.Assignment;

import java.util.List;

public class Adapter_AssignmentM extends RecyclerView.Adapter<Adapter_AssignmentM.MyViewHolder> {
    private List<Assignment> assignments;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_AssignmentM(Context context, List<Assignment> _assignments) {
        this.mInflater = LayoutInflater.from(context);
        this.assignments = _assignments;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.assignment_item_manager, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("pttt", "Position = " + position);
        Assignment a = assignments.get(position);
        holder.assignItemM_LBL_title.setText(a.getTitle());
        holder.assignItemM_LBL_date.setText("" + a.getDueTo().toString());

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
        return assignments.size();
    }

    // convenience method for getting data at click position
    Assignment getItem(int id) {
        return assignments.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface MyItemClickListener {
        void onItemClicked(View v, int a); // open popup
        void onFinishAssignment(View v, Assignment a); // finish assignment
    }



    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView assignItemM_LBL_date;
        TextView assignItemM_LBL_title;
        RelativeLayout assignItemM_LAY_item;

        MyViewHolder(View itemView) {
            super(itemView);
            assignItemM_LBL_date = itemView.findViewById(R.id.assignItemM_LBL_date);
            assignItemM_LBL_title = itemView.findViewById(R.id.assignItemM_LBL_title);
            assignItemM_LAY_item = itemView.findViewById(R.id.assignItemM_LAY_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mClickListener.onItemClicked(view, getAdapterPosition());
                    }
                }
            });
        }
    }

}
