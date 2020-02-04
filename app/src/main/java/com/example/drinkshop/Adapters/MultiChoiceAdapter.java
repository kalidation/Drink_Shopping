package com.example.drinkshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkshop.Model.Drink;
import com.example.drinkshop.R;
import com.example.drinkshop.Utils.Common;

import java.util.List;

public class MultiChoiceAdapter extends RecyclerView.Adapter<MultiChoiceAdapter.MyViewHolder> {

    private Context context;
    private List<Drink> drinkList;

    public MultiChoiceAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.multi_check_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.checkBox.setText(drinkList.get(position).getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Common.toppingNames.add(drinkList.get(position).getName());
                    Common.toppingPrice+=Double.parseDouble(drinkList.get(position).getPrice());
                }else{
                    Common.toppingNames.remove(drinkList.get(position).getName());
                    Common.toppingPrice-=Double.parseDouble(drinkList.get(position).getPrice());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox =  itemView.findViewById(R.id.checkbox_topping);
        }
    }
}
