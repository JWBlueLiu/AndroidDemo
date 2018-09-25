package com.example.demoCollection.widget.conversation;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.transition.TransitionManager;
import android.util.Pair;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.example.demoCollection.Msg;
import com.example.demoCollection.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConversationActivity extends Activity implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();

    private InputMethodManager mInputMethodManager;
    private LayoutInflater mInflater;

    private ConversationListView mConversationList;
    private ConversationAdapter mConversationAdapter;

    private LinearLayout mBottomContainer;
    private EditText replyAll;
    private WrapContentHeightViewPager mBottomPanelContainer;
    private int pagerCount;
    private LinearLayout mBottoDotContainer;
    private boolean mBottomPanelInit;

    int[] icons = new int[]{
            android.R.color.background_dark,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light,
            android.R.color.holo_blue_bright,
            android.R.color.darker_gray,
            android.R.color.secondary_text_dark,
            android.R.color.holo_green_light,
            android.R.color.primary_text_dark_nodisable,
    };

    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.list_select_menu, menu);
            mode.setTitle("选择了");
            mActionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    mConversationList.deleteCheckedItems(mConversationAdapter);
                    mode.finish();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (!mConversationList.mIsDeletingAnimRunning) {  // 在删除动画正在进行时，不启动 退出多选模式 的动画
                exitSelectMode();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                List<Conversation> msgList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    msgList.add(
                            new Conversation(
                                    i,
                                    icons[r.nextInt(icons.length)],
                                    Msg.msgStrs[r.nextInt(Msg.msgStrs.length)],
                                    r.nextInt(2)));
                }
                TransitionManager.beginDelayedTransition(mConversationList, mConversationList.createAddItemsTransitions());
                mConversationAdapter.addConversations(msgList);
            }
        });

        mInflater = LayoutInflater.from(getApplicationContext());
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        initView();
    }

    private void initView() {
        initConverSationList();
        initBottonPanel();
    }

    private void initConverSationList() {
        mConversationList = (ConversationListView) findViewById(R.id.lv_conversation);

        mConversationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideInputMethod();
                handdleBottomPanel(false);

                if (mConversationAdapter.isSelectMode()) {
                    mConversationAdapter.toggleSelect(position);
                    mConversationAdapter.notifyDataSetInvalidated();
                }
            }
        });
        mConversationList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mConversationAdapter.isSelectMode()) { // 如果不在多选模式，则进入多选模式
                    enterSelectMode();
                }
                return false;
            }
        });
        mConversationAdapter = new ConversationAdapter(getApplicationContext());
        mConversationList.setAdapter(mConversationAdapter);
    }

    private void initBottonPanel() {
        mBottomContainer = (LinearLayout) findViewById(R.id.ll_bottom_container);
        replyAll = (EditText) findViewById(R.id.et_reply_all);
        mBottomPanelContainer = (WrapContentHeightViewPager) findViewById(R.id.vp_bottom_panel_container);
        mBottoDotContainer = (LinearLayout) findViewById(R.id.ll_bottom_dot_container);
        ImageView option = (ImageView) findViewById(R.id.btn_option);

        // get bottom dot size
        final int bottomDotSize = (int) getResources().getDimension(R.dimen.conversation_bottom_dot_size);
        final int bottomDotMargin = (int) getResources().getDimension(R.dimen.conversation_bottom_dot_margin);

        mBottomContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!mBottomPanelInit) {
                    mBottomPanelInit = true;
                    mBottomContainer.setVisibility(View.GONE);
                }
            }
        });

        option.setOnClickListener(this);
        replyAll.setOnClickListener(this);

        // Option Panel
        GridView optionPanel = (GridView) mInflater.inflate(R.layout.conversation_card_list_bottom_option_panel, null);
        TypedArray optionIcons = getResources().obtainTypedArray(R.array.bottom_option_icon);
        String[] optionTexts = getResources().getStringArray(R.array.bottom_option_text);
        List<Pair> options = new ArrayList<>();
        for (int i = 0; i < optionTexts.length; i++) {
            options.add(new Pair(optionIcons.getResourceId(i, -1), optionTexts[i]));
        }
        OptionPanelAdapter optionPanelAdapter = new OptionPanelAdapter(getApplicationContext());
        optionPanel.setAdapter(optionPanelAdapter);
        optionPanelAdapter.addOptions(options);
        // Attachment Panel
        ListView attachmentPanel = (ListView) mInflater.inflate(R.layout.conversation_card_list_bottom_attachment_panel, null);

        //  Bottom Panel List
        List bottomPanelList = new ArrayList<>();
        bottomPanelList.add(optionPanel);
        bottomPanelList.add(attachmentPanel);

        mBottomPanelContainer.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                View dot = mInflater.inflate(R.layout.conversation_card_list_bottom_panel_dot, null);
                if (pagerCount == 0) {
                    dot.setSelected(true);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bottomDotSize, bottomDotSize);
                params.setMargins(bottomDotMargin, 0, bottomDotMargin, 0);

                mBottoDotContainer.addView(dot, params);
                pagerCount += 1;
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                mBottoDotContainer.removeViewAt(pagerCount - 1);
                pagerCount -= 1;
            }
        });
        mBottomPanelContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int childCount = mBottoDotContainer.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    mBottoDotContainer.getChildAt(i).setSelected(false);
                }
                mBottoDotContainer.getChildAt(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mBottomPanelContainer.setAdapter(new BottomPanelPagerAdapter(bottomPanelList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_option:
                if (mBottomPanelContainer.isShown()) {
                    showInputMethod(replyAll);
                    handdleBottomPanel(false);
                } else {
                    hideInputMethod();
                    mBottomPanelContainer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handdleBottomPanel(true);
                        }
                    }, 100);
                }
                break;
            case R.id.et_reply_all:
                showInputMethod(replyAll);
                handdleBottomPanel(false);

                mConversationList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mConversationList.smoothScrollToPosition(mConversationAdapter.getCount() - 1);
                    }
                }, 100);
                break;
        }
    }

    private void hideInputMethod() {
        if (this.getWindow() != null && this.getWindow().getCurrentFocus() != null) {
            mInputMethodManager.hideSoftInputFromWindow(this.getWindow().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void showInputMethod(View view) {
        if (null != view) {
            view.requestFocus();
            mInputMethodManager.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    private void handdleBottomPanel(boolean isShow) {
        int state;
        if (isShow) {
            state = View.VISIBLE;
        } else {
            state = View.GONE;
        }
        mBottomContainer.setVisibility(state);
    }

    // 进入多选模式
    private void enterSelectMode() {
        mConversationList.enterSelectMode(mConversationAdapter);
        mActionMode = startActionMode(mActionModeCallback);
        mConversationList.mActionMode = mActionMode;
    }

    private void exitSelectMode() {
        mConversationList.exitSelectMode(mConversationAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mBottomPanelContainer.isShown() || mConversationAdapter.isSelectMode()) {
            handdleBottomPanel(false);
            exitSelectMode();
        } else {
            super.onBackPressed();
        }
    }

}
