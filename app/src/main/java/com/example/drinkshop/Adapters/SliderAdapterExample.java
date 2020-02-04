package com.example.drinkshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drinkshop.Model.Banner;
import com.example.drinkshop.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<Banner> list;

    public SliderAdapterExample(Context context , List<Banner> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        //viewHolder.textViewDescription.setText("This is slider item " + position);

        Glide.with(viewHolder.itemView).load(list.get(position).getLink()).into(viewHolder.imageViewBackground);
        viewHolder.textViewName.setText(list.get(position).getName());

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return list.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewName;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.image_pour_slider);
            textViewName = itemView.findViewById(R.id.text_pour_slider);
            this.itemView = itemView;
        }
    }

}
