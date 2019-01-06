package com.example.zhanyuan.finalapp.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.adapters.DataTypeAdapter;
import com.example.zhanyuan.finalapp.utils.DatabaseSchema;
import com.example.zhanyuan.finalapp.utils.MyDatabaseHelper;
import com.example.zhanyuan.finalapp.utils.ResourcesUtil;
import com.example.zhanyuan.finalapp.utils.SharedPrefenrenceHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BookNoteActivity extends BaseActivity implements  View.OnClickListener{

    private TextView incomeTv, payoutTv, saveTv, titleTv;
    private Button back_btn, type_checkbox;
    private EditText description_input, money_input;
    private LinearLayout keyboarder;

    // keyboarder elements
    TextView number1, number2, number3, number4, number5, number6, number7, number8, number9, number0, dotTv, confermTv;
    RelativeLayout delete;

    public String type = DatabaseSchema.DataSchema.payout;

    // calculator
    private boolean isDot;
    private String num = "0";   // number before dot
    private String dotNum = "."; //number after dot
    private final int MAX_NUM = 9999999;
    private final int DOT_NUM = 2;
    private int count = 0;      // count how many number there are after dot

    private SharedPrefenrenceHelper sp;


    @Override
    protected int getLayout() {
        return R.layout.activity_book_note;
    }

    @Override
    protected void bindView() {
        incomeTv = (TextView)findViewById(R.id.tb_note_income);
        payoutTv = (TextView)findViewById(R.id.tb_note_payout);
        saveTv = (TextView)findViewById(R.id.save);
        titleTv = (TextView)findViewById(R.id.note_title);
        back_btn = (Button)findViewById(R.id.back_btn);
        type_checkbox = (Button)findViewById(R.id.type_checkbox);
        description_input = (EditText)findViewById(R.id.description_input);
        money_input = (EditText)findViewById(R.id.money_input);
        keyboarder = (LinearLayout)findViewById(R.id.keyboard);

        // keyboarder elements
        number0 = (TextView)findViewById(R.id.tb_calc_num_0);
        number1 = (TextView)findViewById(R.id.tb_calc_num_1);
        number2 = (TextView)findViewById(R.id.tb_calc_num_2);
        number3 = (TextView)findViewById(R.id.tb_calc_num_3);
        number4 = (TextView)findViewById(R.id.tb_calc_num_4);
        number5 = (TextView)findViewById(R.id.tb_calc_num_5);
        number6 = (TextView)findViewById(R.id.tb_calc_num_6);
        number7 = (TextView)findViewById(R.id.tb_calc_num_7);
        number8 = (TextView)findViewById(R.id.tb_calc_num_8);
        number9 = (TextView)findViewById(R.id.tb_calc_num_9);
        dotTv = (TextView)findViewById(R.id.tb_calc_num_dot);
        confermTv = (TextView)findViewById(R.id.tb_calc_num_done);
        delete = (RelativeLayout)findViewById(R.id.tb_calc_num_del);

        incomeTv.setOnClickListener(this);
        payoutTv.setOnClickListener(this);
        saveTv.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        type_checkbox.setOnClickListener(this);

        number0.setOnClickListener(this);
        number1.setOnClickListener(this);
        number2.setOnClickListener(this);
        number3.setOnClickListener(this);
        number4.setOnClickListener(this);
        number5.setOnClickListener(this);
        number6.setOnClickListener(this);
        number7.setOnClickListener(this);
        number8.setOnClickListener(this);
        number9.setOnClickListener(this);
        dotTv.setOnClickListener(this);
        confermTv.setOnClickListener(this);
        delete.setOnClickListener(this);


        // money_input do not pull out the keyboarder
        money_input.setInputType(InputType.TYPE_NULL);
        // in this way we can use default keyboarder
        //money_input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);

        money_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    keyboarder.setVisibility(View.VISIBLE);

                }
                if(!hasFocus){
                    keyboarder.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void initEventAndData() {
        setTitleStatus();
        sp = new SharedPrefenrenceHelper(mContext);
    }


    private void setTitleStatus(){
        if (type.equals(DatabaseSchema.DataSchema.payout)){
            payoutTv.setSelected(true);
            incomeTv.setSelected(false);
            titleTv.setText(DatabaseSchema.DataSchema.payout);

        }
        if (type.equals(DatabaseSchema.DataSchema.income)){
            incomeTv.setSelected(true);
            payoutTv.setSelected(false);
            titleTv.setText(DatabaseSchema.DataSchema.income);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tb_note_income:
                type = DatabaseSchema.DataSchema.income;
                setTitleStatus();
                break;
            case R.id.tb_note_payout:
                type = DatabaseSchema.DataSchema.payout;
                setTitleStatus();
                break;
            case R.id.save:
                SaveRecord();
                break;
            case R.id.back_btn:
                this.finish();
                break;
            case R.id.type_checkbox:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                View view = LayoutInflater.from(mContext).inflate(R.layout.detail_list_view, null, false);
                ListView listView= (ListView)view.findViewById(R.id.detail_list);
                Button confirm_btn = (Button)view.findViewById(R.id.confirm_btn);

                HashMap<Integer, Boolean> isselected = new HashMap<Integer,Boolean>();

                ArrayList<String> names;
                if(titleTv.getText().toString().equals(DatabaseSchema.DataSchema.income))
                    names = DatabaseSchema.DataSchema.names_income;
                else
                    names = DatabaseSchema.DataSchema.names_payout;

                final DataTypeAdapter adapter = new DataTypeAdapter(mContext,names, isselected);

                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                final PopupWindow popWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                //popWindow.setAnimationStyle(R.anim.anim_pop);
                popWindow.setTouchable(true);
                popWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popWindow.showAtLocation(v, Gravity.CENTER, 0 ,0);

                confirm_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nameSelected = adapter.getNameSelected();
                        type_checkbox.setText(nameSelected);
                        popWindow.dismiss();
                    }
                });
                break;

            case R.id.tb_calc_num_done:
                SaveRecord();
                break;
            case R.id.tb_calc_num_0:
                calcMoney(0);
                break;
            case R.id.tb_calc_num_1:
                calcMoney(1);
                break;
            case R.id.tb_calc_num_2:
                calcMoney(2);
                break;
            case R.id.tb_calc_num_3:
                calcMoney(3);
                break;
            case R.id.tb_calc_num_4:
                calcMoney(4);
                break;
            case R.id.tb_calc_num_5:
                calcMoney(5);
                break;
            case R.id.tb_calc_num_6:
                calcMoney(6);
                break;
            case R.id.tb_calc_num_7:
                calcMoney(7);
                break;
            case R.id.tb_calc_num_8:
                calcMoney(8);
                break;
            case R.id.tb_calc_num_9:
                calcMoney(9);
                break;
            case R.id.tb_calc_num_dot:
                isDot = true;
                dotNum = ".";

                money_input.setText(num + dotNum);
                break;
            case R.id.tb_calc_num_del:
                if (isDot){
                    if (count > 0){
                        dotNum = dotNum.substring(0, dotNum.length() - 1);
                        count--;
                        money_input.setText(num + dotNum);
                    }
                    if (count == 0){
                        isDot = false;
                        dotNum = ".";
                        money_input.setText(num);
                    }
                }else {
                    if (num.length() > 0) {
                        num = num.substring(0, num.length() - 1);
                        money_input.setText(num);
                    }
                    if (num.length() == 0) {
                        num = "0";
                        money_input.setText("");
                    }
                }
                break;

        }
    }


    private void SaveRecord(){

        // save record into database
        if(description_input.getText().toString().equals("")){
            Toast.makeText(mContext, "Please to write a description", Toast.LENGTH_LONG).show();
        }else if(money_input.getText().toString().equals("")) {
            Toast.makeText(mContext, "Please to write money", Toast.LENGTH_LONG).show();
        }else if(type_checkbox.getText().equals("type")){
            Toast.makeText(mContext, "Please to select type", Toast.LENGTH_LONG).show();
        }else {
            MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(mContext, "my.db", null, 1);
            SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
            Calendar cal;
            int year, month, day;

            cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            day = cal.get(Calendar.DAY_OF_MONTH);

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseSchema.DataSchema.year, year);
            contentValues.put(DatabaseSchema.DataSchema.month, month);
            contentValues.put(DatabaseSchema.DataSchema.day, day);
            contentValues.put(DatabaseSchema.DataSchema.rectype, type);
            contentValues.put(DatabaseSchema.DataSchema.money, Float.parseFloat(num + dotNum));
            contentValues.put(DatabaseSchema.DataSchema.recdescription, description_input.getText().toString());
            contentValues.put(DatabaseSchema.DataSchema.recdetail, type_checkbox.getText().toString());

            db.insert(DatabaseSchema.DataSchema.tableName, null, contentValues);


            // save the money into sharedPreference
            String key;
            String previousValue;
            float value;

            if(type_checkbox.getText().toString().equals(ResourcesUtil.food))
                key = ResourcesUtil.food;
            else if(type_checkbox.getText().toString().equals(ResourcesUtil.housing))
                key = ResourcesUtil.housing;
            else if(type_checkbox.getText().toString().equals(ResourcesUtil.transportation))
                key = ResourcesUtil.transportation;
            else if(type_checkbox.getText().toString().equals(ResourcesUtil.study))
                key = ResourcesUtil.study;
            else if(type_checkbox.getText().toString().equals(ResourcesUtil.medicine))
                key = ResourcesUtil.medicine;
            else
                key = ResourcesUtil.others;

            // save current money into right type
            previousValue = sp.read(key);
            value = Float.parseFloat(previousValue);
            value += Float.parseFloat(num + dotNum);
            sp.save(key, String.valueOf(value));

            // save current money into total
            previousValue = sp.read("total");
            value = Float.parseFloat(previousValue);
            value += Float.parseFloat(num + dotNum);
            sp.save("total", String.valueOf(value));

            this.finish();
        }
    }

    private void calcMoney(int number){
        if (num.equals("0") && number == 0)
            return;
        if (isDot) {
            if (count < DOT_NUM) {
                count++;
                dotNum += number;
                money_input.setText((num + dotNum));
            }
        }else if (Integer.parseInt(num) < MAX_NUM) {
            if (num.equals("0"))
                num = "";
            num += number;
            money_input.setText(num);
        }
    }
}
