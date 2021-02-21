package com.example.workitapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.workitapp.Objects.Request;
import com.example.workitapp.Objects.Worker;
import com.example.workitapp.R;

import java.util.List;

public class Adapter_Requests extends RecyclerView.Adapter<Adapter_Requests.MyViewHolder> {
    private List<Request> requests;
    private List<Worker> workers;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;

    public Adapter_Requests(Context context, List<Request> _requests, List<Worker> _workers) {
        this.mInflater = LayoutInflater.from(context);
        this.requests = _requests;
        this.workers = _workers;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.requests_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Request r = requests.get(position);
        Worker tmp = new Worker();
        tmp.setUid(r.getUid());

        int index = workers.indexOf(tmp);
        if(index >= 0) {
            tmp = workers.get(index);
            holder.requestItem_LBL_title.setText(tmp.getName());
            holder.requestItem_LBL_id.setText("Division " + tmp.getDivisionID());
        }
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    Request getItem(int id) {
        return requests.get(id);
    }

    public void setClickListener(MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface MyItemClickListener {
        void onItemClicked(View v, int a); // open popup
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView requestItem_LBL_title;
        TextView requestItem_LBL_id;
        RelativeLayout requestItem_LAY_item;

        MyViewHolder(View itemView) {
            super(itemView);
            requestItem_LBL_title = itemView.findViewById(R.id.requestItem_LBL_title);
            requestItem_LAY_item = itemView.findViewById(R.id.requestItem_LAY_item);
            requestItem_LBL_id = itemView.findViewById(R.id.requestItem_LBL_id);

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
