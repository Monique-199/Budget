package com.example.budget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.budget.databinding.ActivityDashBoardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity {
    ActivityDashBoardBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    int sumExpense=0;
    int sumIncome=0;

    ArrayList<TrancanctionModel> trancanctionModelArrayList;
    TransanctionAdapter transanctionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        trancanctionModelArrayList=new ArrayList<>();

        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecyclerView.setHasFixedSize(true);
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(DashBoardActivity.this,MainActivity.class));
                        finish();
                }
            }
        });

        binding.signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             createSignOutDialog();
            }
        });

        binding.addFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(DashBoardActivity.this,AddTransanctionActivity.class));
                } catch (Exception e){

                }

            }
        });
        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(DashBoardActivity.this,DashBoardActivity.class));
                    finish();
               } catch(Exception e){

                }
            }
        });
        loadData();
    }

    private void createSignOutDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(DashBoardActivity.this);
        builder.setTitle("Delete")
                .setMessage("are you sure you want to delete this")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       firebaseAuth.signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private void loadData(){
       firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes")
               .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot ds:task.getResult()){
                            TrancanctionModel model=new TrancanctionModel(
                                    ds.getString("id"),
                                    ds.getString("note"),
                                    ds.getString("amount"),
                                    ds.getString("type"),
                                    ds.getString("date"));
                            int amount=Integer.parseInt(ds.getString("amount"));
                            if(ds.getString("type").equals("Expense")){
                                sumExpense=sumExpense+amount;

                            }else{
                                sumIncome=sumIncome+amount;
                            }
                            trancanctionModelArrayList.add(model);
                        }
                        binding.totalIncome.setText(String.valueOf(sumIncome));
                        binding.totalExpense.setText(String.valueOf(sumExpense));
                        binding.totalBalance.setText(String.valueOf(sumIncome-sumExpense));
                       transanctionAdapter=new TransanctionAdapter(DashBoardActivity.this,trancanctionModelArrayList);
                       binding.historyRecyclerView.setAdapter(transanctionAdapter);
                   }
               });













































































































































    }
}