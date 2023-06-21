package com.example.uy.sellapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.uy.sellapp.AddActivity;
import com.example.uy.sellapp.CartActivity;
import com.example.uy.sellapp.R;
import com.example.uy.sellapp.UpdateDeleteActivity;
import com.example.uy.sellapp.adapter.RecycleViewAdapter;
import com.example.uy.sellapp.dal.SQLiteHelper;
import com.example.uy.sellapp.model.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment implements RecycleViewAdapter.ItemListener {
    private RecyclerView recyclerView;
    RecycleViewAdapter adapter;
    private SQLiteHelper db;
    private TextView tvTong;
    private Item item;
    private ImageView add_cart_icon;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        tvTong = view.findViewById(R.id.tvTong);
        add_cart_icon = (ImageView) view.findViewById(R.id.add_cart_icon);
        if (add_cart_icon != null){
            add_cart_icon.setVisibility(View.VISIBLE);
        }

        adapter = new RecycleViewAdapter();
        db = new SQLiteHelper(getContext());
        List<Item>list = db.getByCart();
        adapter.setList(list);
        tvTong.setText("Tổng tiền : " + tong(list) +"K");
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
    }
    private int tong(List<Item> list){
        int t=0;
        for(Item i:list){
            t+= Integer.parseInt(i.getPrice());
        }
        return t;
    }

    @Override
    public void onItemClick(View view, int position) {
        Item item = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), CartActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
//        Date d = new Date();
//        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        List<Item> list = db.getByCart();
        adapter.setList(list);
        tvTong.setText("Tổng tiền: " + tong(list) + "K");
    }
}
