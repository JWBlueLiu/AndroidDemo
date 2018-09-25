package com.example.demoCollection.widget.conversation;

import android.content.Context;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import com.example.demoCollection.R;

public class ConversationListView extends AbsTransitionListView {

    public ConversationListView(Context context) {
        super(context);
    }

    public ConversationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConversationListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void setListViewItemIdToTransition(TransitionSet transitionSet) {
        if (transitionSet == null)
            return;

        transitionSet.getTransitionAt(0).addTarget(R.id.fl_conversation);
        TransitionSet temp = (TransitionSet) transitionSet.getTransitionAt(1);
        if (temp != null) {
            temp.getTransitionAt(0).addTarget(R.id.cb);
        }
    }

}
