package com.example.demoCollection.widget.multiTask;

public interface BounceListener {
    public void onState(boolean header, BounceScroller.State state);

    public void onOffset(boolean header, int offset);
}
