package com.example.budget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budget.R;
import com.example.budget.TrancanctionModel;
import com.example.budget.UpdateActivity;

import java.util.ArrayList;

public class TransanctionAdapter extends RecyclerView.Adapter<TransanctionAdapter.TransactionViewHolder> {
    private Context context;
    private ArrayList<TrancanctionModel> transactionList;

    public TransanctionAdapter(Context context, ArrayList<TrancanctionModel> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.one_recycler_item,parent,false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TrancanctionModel model = transactionList.get(position);
        String priority = model.getType();
        if (priority.equals("Expense")){
            holder.priority.setBackgroundResource(R.drawable.red_shape);
        } else {
            holder.priority.setBackgroundResource(R.drawable.green_shape);
        }
        holder.amount.setText(model.getAmount());
        holder.date.setText(model.getDate());
        holder.note.setText(model.getNote());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UpdateActivity.class);
                intent.putExtra("id",transactionList.get(position).getId());
                intent.putExtra("amount",transactionList.get(position).getAmount());
                intent.putExtra("note",transactionList.get(position).getNote());
                intent.putExtra("type",transactionList.get(position).getType());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView note, amount, date;
        View priority;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note_one);
            amount = itemView.findViewById(R.id.amount_one);
            date = itemView.findViewById(R.id.date_one);
            priority = itemView.findViewById(R.id.priority_one);
        }
    }
}
