package com.example.uy.sellapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uy.sellapp.dal.SQLiteHelper;
import com.example.uy.sellapp.model.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class UpdateDeleteActivity extends AppCompatActivity implements View.OnClickListener{
    public Spinner sp;
    private EditText eTitle, ePrice, eDate;
    private Button btUpdate, btBack, btRemove, btCart;
    private Item item;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        initView();
        if(!user.getEmail().equalsIgnoreCase("admin@gmail.com")) {
            eTitle.setEnabled(false);
            ePrice.setEnabled(false);
            eDate.setEnabled(false);
            sp.setEnabled(false);
        }
        btBack.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        btRemove.setOnClickListener(this);
        btCart.setOnClickListener(this);
        eDate.setOnClickListener(this);
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        eTitle.setText(item.getTitle());
        ePrice.setText(item.getPrice());
        eDate.setText(item.getDate());
        int p = 0;
        for (int i = 0; i < sp.getCount(); i++) {
            if(sp.getItemAtPosition(i).toString().equalsIgnoreCase(item.getCategory())){
                p=i;
                break;
            }
        }
        sp.setSelection(p);
    }
    private void initView() {
        sp = findViewById(R.id.spCategory);
        eTitle = findViewById(R.id.tvTitle);
        ePrice = findViewById(R.id.tvPrice);
        eDate = findViewById(R.id.tvDate);
        btUpdate = findViewById(R.id.btUpdate);
        btBack = findViewById(R.id.btBack);
        btRemove = findViewById(R.id.btRemove);
        btCart = findViewById(R.id.btCart);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        sp.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner,
                getResources().getStringArray(R.array.category)));

    }

    @Override
    public void onClick(View view) {
        SQLiteHelper db = new SQLiteHelper(this);
        if (view == eDate){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(UpdateDeleteActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if(d<10){
                        date = "0";
                    }
                    if(m>8){
                        date=d +"/" +(m+1) +"/"+y;
                    }else {
                        date=d +"/0" +(m+1) +"/"+y;
                    }
                    eDate.setText(date);
                }
            },year,month,day);
            dialog.show();
        }
        if(view==btBack){
            finish();
        }
        if (view==btUpdate){
            if(user.getEmail().equalsIgnoreCase("admin@gmail.com")) {
                String t = eTitle.getText().toString();
                String p = ePrice.getText().toString();
                String c = sp.getSelectedItem().toString();
                String d = eDate.getText().toString();
                if (!t.isEmpty() && p.matches("\\d+")) {
                    int id = item.getId();
                    Item i = new Item(id, t, c, p, d);
                    db = new SQLiteHelper(this);
                    db.update(i);
                    finish();
                }
            }
        }
        if (view==btCart){
            int id = item.getId();
            Item i = new Item(id, 0);
            db = new SQLiteHelper(this);
            db.updateCart(i);
            finish();
        }
        if (view == btRemove){
            if(user.getEmail().equalsIgnoreCase("admin@gmail.com")) {
                int id = item.getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Thông báo xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa '" + item.getTitle() + "' không?");
                builder.setIcon(R.drawable.remove);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteHelper bb = new SQLiteHelper(getApplicationContext());
                        bb.delete(id);
                        finish();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}