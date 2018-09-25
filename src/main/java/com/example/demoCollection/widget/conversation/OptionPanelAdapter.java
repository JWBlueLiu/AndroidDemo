package com.example.demoCollection.widget.conversation;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.demoCollection.R;
import java.util.ArrayList;
import java.util.List;

public class OptionPanelAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Pair> mOptions;

    public OptionPanelAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mOptions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null != convertView) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.conversation_card_lsit_bottom_option_panel_item, parent, false);
            holder.optionIcon = (ImageView) convertView.findViewById(R.id.iv_option_icon);
            holder.optionText = (TextView) convertView.findViewById(R.id.tv_option_text);

            convertView.setTag(holder);
        }

        Pair option = mOptions.get(position);
        holder.optionIcon.setBackgroundResource((Integer) option.first);
        holder.optionText.setText((String) option.second);

        return convertView;
    }

    public void addOptions(List<Pair> options) {
        if (null != options && !options.isEmpty()) {
            mOptions.addAll(options);
            notifyDataSetChanged();
        }
    }

    class ViewHolder {
        ImageView optionIcon;
        TextView optionText;
    }

}
