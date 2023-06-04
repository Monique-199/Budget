package com.example.budget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.budget.databinding.ActivityAddTransanctionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTransanctionActivity extends AppCompatActivity {
    ActivityAddTransanctionBinding binding;
    FirebaseFirestore fStore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddTransanctionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fStore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        binding.expenseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="Expense";
               binding.expenseCheckBox.setChecked(true);
               binding.incomeCheckbox.setChecked(false);
            }
        });
        binding.incomeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="Income";
                binding.expenseCheckBox.setChecked(false);
                binding.incomeCheckbox.setChecked(true);
            }
        });
        binding.btnAddTransanction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount=binding.userAmountAdd.getText().toString().trim();
                String note=binding.userNoteAdd.getText().toString().trim();
                if(amount.length()<=0){
                  return;
                }
                if(type.length()<=0){
                    Toast.makeText(AddTransanctionActivity.this, "Select transanction type", Toast.LENGTH_SHORT).show();
                }
                SimpleDateFormat sdf= new SimpleDateFormat("dd MM yyyy_HHmmss", Locale.getDefault());
                String currentDateandTime= sdf.format(new Date());
                String id= UUID.randomUUID().toString();
                Map<String,Object> transanction=new HashMap<>();
                transanction.put("id",id);
                transanction.put("amount",amount);
                transanction.put("note",note);
                transanction.put("type",type);
                transanction.put("date",currentDateandTime);

                fStore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(id)
                        .set(transanction)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddTransanctionActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                binding.userNoteAdd.setText("");
                                binding.userAmountAdd.setText("");
                                //binding.userNoteAdd.setText("");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddTransanctionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}