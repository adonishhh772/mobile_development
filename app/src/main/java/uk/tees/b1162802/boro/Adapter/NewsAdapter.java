package uk.tees.b1162802.boro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.data.model.News;

public class NewsAdapter extends ArrayAdapter<News> {
    private Context mContext;
    private int mResource;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<News> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);
        TextView newsTitle = convertView.findViewById(R.id.news_title);
        TextView newsUrl = convertView.findViewById(R.id.news_url);

        newsTitle.setText(getItem(position).getNewsTitle());
        newsUrl.setText(getItem(position).getNewsUrl());
        return convertView;
    }
}
