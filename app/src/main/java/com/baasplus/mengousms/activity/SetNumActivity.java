package com.baasplus.mengousms.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baasplus.mengousms.Global;
import com.baasplus.mengousms.R;
import com.baasplus.mengousms.tools.Utility;
import com.coderpage.swipemenulistview.SwipeMenu;
import com.coderpage.swipemenulistview.SwipeMenuCreator;
import com.coderpage.swipemenulistview.SwipeMenuItem;
import com.coderpage.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by abner-l on 15/6/5.
 */
public class SetNumActivity extends Activity {

    private SwipeMenuListView allNumLV;
    private EditText addNumET;
    private Button addNumBTN;

    private NumbersAdapter numAdapter;
    private List<String> nums = new ArrayList<>();

//    private SharedPreferences settingSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setnumber);
        initView();
    }

    private void initView() {
        allNumLV = (SwipeMenuListView) findViewById(R.id.lv_numbers);
        addNumET = (EditText) findViewById(R.id.et_add_num);
        addNumBTN = (Button) findViewById(R.id.btn_add_num);
        addNumET.addTextChangedListener(new EnterNumListener());

        final Global global = Global.getInstance(SetNumActivity.this);
        nums = Utility.setToList(global.getNumbers());
        Collections.sort(nums);
        numAdapter = new NumbersAdapter(SetNumActivity.this, nums);
        allNumLV.setAdapter(numAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                // set item width
                openItem.setWidth(90);
                // set item title
                openItem.setTitle("编辑");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(90);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        allNumLV.setMenuCreator(creator);
        allNumLV.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    // 编辑
                    case 0:
                        break;
                    // 删除
                    case 1:
                        String item = nums.get(position);
                        global.delNum(item);
                        global.saveNums();
                        nums.remove(position);
                        numAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });
        allNumLV.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

    }

    /**
     * 添加号码按钮点击事件
     */
    public void addNum(View v) {
        Global global = Global.getInstance(SetNumActivity.this);
        String num = addNumET.getText().toString().trim();
        global.addNum(num);
        // 添加失败，提示，返回
        if (!global.saveNums()) {
            Toast.makeText(SetNumActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
            return;
        }
        // 添加成功
        nums.add(num);
        Collections.sort(nums);
        // 更新 ListView
        numAdapter.notifyDataSetChanged();
        // 清空 EditText
        addNumET.setText("");
        // 隐藏添加按钮
        addNumBTN.setVisibility(View.INVISIBLE);
    }


    /**
     * 监听号码输入框输入
     */
    class EnterNumListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                // 当输入框有内容时，设置添加按钮可见
                addNumBTN.setVisibility(View.VISIBLE);
            } else {
                // 当输入框没有内容时，设置添加按钮为不可见
                addNumBTN.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class NumbersAdapter extends BaseAdapter {
        private List<String> data = null;
        private Context context;

        public NumbersAdapter(Context context, List<String> data) {
            super();
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.num_list_item, null);
            }

            TextView numTV = (TextView) convertView.findViewById(R.id.tv_num);
            numTV.setText(data.get(position));
            return convertView;
        }
    }
}
