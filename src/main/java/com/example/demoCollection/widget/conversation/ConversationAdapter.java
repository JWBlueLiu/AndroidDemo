package com.example.demoCollection.widget.conversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.demoCollection.R;
import com.example.demoCollection.widget.leCheckBox.LeCheckBox;
import java.util.ArrayList;
import java.util.List;

public class ConversationAdapter extends AbsTransitionAdapter {

    private Context mContext;
    private List<Conversation> mConversations;
    private LayoutInflater inflater;
    private final int TYPE_COUNT = 2, TYPE_LEFT = 0, TYPEE_RIGHT = 1;

    public ConversationAdapter(Context context) {
        mContext = context;
        mConversations = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mConversations.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return mConversations.get(position).id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final Conversation conversation = mConversations.get(position);
        final int type = conversation.msgType;

        if (null == convertView) {
//            switch (type) {
//                case TYPE_LEFT:
//                    convertView = inflater.inflate(R.layout.conversation_item_msg_left, parent, false);
//                    break;
//                case TYPEE_RIGHT:
            convertView = inflater.inflate(R.layout.conversation_item_msg_right, parent, false);
//                    break;
//            }
            viewHolder = new ViewHolder();
            viewHolder.conversation = (FrameLayout) convertView.findViewById(R.id.fl_conversation);
            viewHolder.icon = convertView.findViewById(R.id.icon);
            viewHolder.msg = (TextView) convertView.findViewById(R.id.tv_msg);
            viewHolder.mask = convertView.findViewById(R.id.mask);
            viewHolder.cb = (LeCheckBox) convertView.findViewById(R.id.cb);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (type) {
            case TYPE_LEFT:
                viewHolder.icon.setBackgroundColor(mContext.getResources().getColor(conversation.icon));
                break;
            case TYPEE_RIGHT:
                viewHolder.icon.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
                break;
        }
        viewHolder.msg.setText(conversation.msg);
        viewHolder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectMode) {
                    toggleSelect(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, conversation.msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (isSelectMode) {
            viewHolder.cb.setVisibility(View.VISIBLE);
            viewHolder.cb.setChecked(conversation.isSelected, true);
            if (conversation.isSelected) {
                viewHolder.mask.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mask.setVisibility(View.GONE);
            }
        } else {
            viewHolder.cb.setVisibility(View.GONE);
            viewHolder.cb.setChecked(false, true);
            viewHolder.mask.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return mConversations.get(position).msgType;
    }

    static class ViewHolder {
        FrameLayout conversation;
        View icon;
        TextView msg;
        View mask;
        LeCheckBox cb;
    }

    public void addConversations(List<Conversation> msgs) {
        mConversations.addAll(msgs);
        notifyDataSetChanged();
    }

    public boolean isDeleteAll() {
        for (Conversation conversation : mConversations) {
            if (conversation.isSelected) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public void unSelectAll() {
        for (Conversation conversation : mConversations) {
            conversation.isSelected = false;
        }
    }

    public void toggleSelect(int position) {
        Conversation conversation = mConversations.get(position);
        conversation.isSelected = !conversation.isSelected;
    }

    public void deleteCheckedItems() {
        int len = mConversations.size();
        for (int i = 0; i < len; i++) {
            Conversation conversation = mConversations.get(i);
            if (Boolean.valueOf(conversation.isSelected)) {
                mConversations.remove(i);
                len--;
                i--;
            }
        }
    }

}
