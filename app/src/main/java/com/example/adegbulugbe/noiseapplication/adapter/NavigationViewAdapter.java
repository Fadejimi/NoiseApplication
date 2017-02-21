package com.example.adegbulugbe.noiseapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adegbulugbe.noiseapplication.R;
import com.example.adegbulugbe.noiseapplication.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by Fadejimi on 1/14/17.
 */

public class NavigationViewAdapter extends RecyclerView.Adapter<NavigationViewAdapter.MyViewHolder> {
    List<NavDrawerItem> navDrawerItems = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationViewAdapter(Context context, List<NavDrawerItem> itemList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.navDrawerItems = itemList;
    }

    public void delete(int position) {
        navDrawerItems.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = navDrawerItems.get(position);
        holder.title.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
        }
    }
}
