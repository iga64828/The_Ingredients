package com.example.final_project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
//import com.example.final_project.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ingredient_scan extends AppCompatActivity {
    private static String fid,mid;
    EditText ED2;
    TextView ED1;
    ImageButton BT1,BT3,BT4,BT5,BT6,BT7;
    TextView TV1,TV2,TV3,TV4,TV5,txtres_scan,txtres_add;
    SingleSpinnerSearch sp1,sp2;
    LinearLayout LY_title,LY_type,LY_day,LY_bt_add,lin_ing_add,lin_foodpic,lin_scan,lin_scan_scan,lin_drop,lin_scan_insert,lin_scan_show,lin_scan_insert_DB;
    String aa;
//    ImageView img_menu;
    final List<KeyPairBoolData> listArray1 = new ArrayList<>();
    final List<KeyPairBoolData> listArray2 = new ArrayList<>();
    List<String> Ingredient_title = new ArrayList<>();
    List<String> Ingredient_type = new ArrayList<>();
    String title,type;



    private Activity context=this;




    RequestQueue requestQueue_barcode;

    ArrayList<String> IngIDList = new ArrayList<>();
    ArrayAdapter<String> IngIDAdapter;
    RequestQueue requestQueue_Type;

    ArrayList<String> IngTitleList = new ArrayList<>();
    ArrayAdapter<String> IngTitleAdapter;
    RequestQueue requestQueue_Title;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_scan);
        TextView tbtv=(TextView)findViewById(R.id.textView2); //toolbar?????????textview
        ImageView btn_back = (ImageView)findViewById(R.id.imageView); //???????????????
        tbtv.setText("????????????");

        ED1= findViewById(R.id.ed_scan_barcode);
        ED2= findViewById(R.id.ed_scan_title);


        BT1= findViewById(R.id.bt_scan_scan);
        BT3= findViewById(R.id.bt_scan_insert_DB);
        BT4= findViewById(R.id.bt_scan_insert);
        BT5= findViewById(R.id.bt_scan_day);
        BT6= findViewById(R.id.bt_drop_down);
        BT7= findViewById(R.id.bt_scan_show);

        TV1= findViewById(R.id.tv_scan_fridgeID);
        TV2= findViewById(R.id.tv_scan_UserID);
        TV3= findViewById(R.id.tv_scan_day);
        TV4= findViewById(R.id.tv_scan_title);
        TV5= findViewById(R.id.tv_scan_barcode);
        aa = TV5.getText().toString();// ???????????????????????????????????????????????????????????????


        sp1 = findViewById(R.id.sp_scan_title);
        sp1.setColorseparation(true);
        sp1.setSearchEnabled(true);
        sp1.setSearchHint("??????????????????");


        sp2 = findViewById(R.id.sp_scan_type);
        sp2.setColorseparation(true);
        sp2.setSearchEnabled(true);
        sp2.setSearchHint("??????????????????");

        txtres_scan=findViewById(R.id.txtres_scan);
        txtres_add=findViewById(R.id.txtres_add);

        LY_title=findViewById(R.id.LY_scan_title);
        LY_type=findViewById(R.id.LY_scan_type);
        LY_day=findViewById(R.id.LY_scan_day);
        LY_bt_add=findViewById(R.id.LY_bt_add);
        lin_ing_add=findViewById(R.id.lin_ing_add);
        lin_foodpic=findViewById(R.id.lin_foodpic);
        lin_scan=findViewById(R.id.lin_scan);
        lin_scan_scan=findViewById(R.id.lin_scan_scan);
        lin_drop=findViewById(R.id.lin_drop);
        lin_scan_insert=findViewById(R.id.lin_scan_insert);
        lin_scan_show=findViewById(R.id.lin_scan_show);
        lin_scan_insert_DB=findViewById(R.id.lin_scan_insert_DB);
//        img_menu=findViewById(R.id.img_menu);




        requestQueue_barcode = Volley.newRequestQueue(getApplicationContext());

        requestQueue_Title = Volley.newRequestQueue(this);
        requestQueue_Type = Volley.newRequestQueue(this);

        String url = "http://192.168.0.195/mike/TypeID.php";
        JsonObjectRequest jsonObjectRequest_Type = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("ingredient_type");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String str_ingID = jsonObject.optString("ingredient_type");
                        IngIDList.add(str_ingID);
//                        IngIDAdapter = new ArrayAdapter<>(ingredient_scan.this,
//                                android.R.layout.simple_spinner_item, IngIDList);
//                        IngIDAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        sp2.setAdapter(IngIDAdapter);
                        Ingredient_type.add(str_ingID);


                    }
                    for (int i = 0; i < Ingredient_type.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();

                        h.setId(i + 1);
                        h.setName(Ingredient_type.get(i));
                        h.setSelected(false);
                        listArray2.add(h);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue_Type.add(jsonObjectRequest_Type);
        sp2.setItems(listArray2, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                type=selectedItem.getName();
            }

            @Override
            public void onClear() {
                Toast.makeText(ingredient_scan.this, "Cleared Selected Item", Toast.LENGTH_SHORT).show();
            }
        });

        String url_title = "http://192.168.0.195/mike/select_ingredient.php";
        JsonObjectRequest jsonObjectRequest_Title = new JsonObjectRequest(Request.Method.POST,
                url_title, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("ingredient");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String str_titleID = jsonObject.optString("ingredient_title");
                        IngTitleList.add(str_titleID);
//                        IngTitleAdapter = new ArrayAdapter<>(ingredient_scan.this,
//                                android.R.layout.simple_spinner_item, IngTitleList);
//                        IngTitleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        sp1.setAdapter(IngTitleAdapter);
                        Ingredient_title.add(str_titleID);
                    }
                    for (int i = 0; i < Ingredient_title.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();

                        h.setId(i + 1);
                        h.setName(Ingredient_title.get(i));
                        h.setSelected(false);
                        listArray1.add(h);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue_Title.add(jsonObjectRequest_Title);


        sp1.setItems(listArray1, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                title=selectedItem.getName();
            }

            @Override
            public void onClear() {
                Toast.makeText(ingredient_scan.this, "Cleared Selected Item", Toast.LENGTH_SHORT).show();
            }
        });



        String username = getIntent().getStringExtra("memberid");//???????????????????????????
        TextView txtloginName = (TextView) findViewById(R.id.tv_scan_UserID);//???????????????????????????
        txtloginName.setText(username);//???????????????????????????
        mid = username;

        String refridgeid = getIntent().getStringExtra("refridgeid");//???????????????????????????
        TextView txtrefridgeid = (TextView) findViewById(R.id.tv_scan_fridgeID);//???????????????????????????
        txtrefridgeid.setText(refridgeid);//???????????????????????????
        fid=refridgeid;


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.Fridge);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.Fridge:
                        String str = txtloginName.getText().toString();//?????????????????????
                        Intent intent = new Intent(getApplicationContext(), Fridge.class);
                        intent.putExtra("memberid",str);//?????????????????????

                        String fri = txtrefridgeid.getText().toString();//?????????????????????
                        intent.putExtra("refridgeid",fri);//?????????????????????
                        startActivity(intent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,Fridge.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.home:
                        String hstr = txtloginName.getText().toString();//?????????????????????
                        Intent hintent = new Intent(getApplicationContext(), MainActivity.class);
                        hintent.putExtra("memberid",hstr);//?????????????????????

                        String hfri = txtrefridgeid.getText().toString();//?????????????????????
                        hintent.putExtra("refridgeid",hfri);//?????????????????????
                        startActivity(hintent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shopping_list:
                        String sstr = txtloginName.getText().toString();//?????????????????????
                        Intent sintent = new Intent(getApplicationContext(), shopping_list.class);
                        sintent.putExtra("memberid",sstr);//?????????????????????

                        String sfri = txtrefridgeid.getText().toString();//?????????????????????
                        sintent.putExtra("refridgeid",sfri);//?????????????????????
                        startActivity(sintent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,shopping_list.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.User:
                        String ustr = txtloginName.getText().toString();//?????????????????????
                        Intent uintent = new Intent(getApplicationContext(), User_setting.class);
                        uintent.putExtra("memberid",ustr);//?????????????????????

                        String ufri = txtrefridgeid.getText().toString();//?????????????????????
                        uintent.putExtra("refridgeid",ufri);//?????????????????????
                        startActivity(uintent);
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id2 = txtrefridgeid.getText().toString();
                String id3 = txtloginName.getText().toString();
                Intent intent3 = new Intent(getApplicationContext(),Fridge.class);
                intent3.putExtra("refridgeid",id2);
                intent3.putExtra("memberid",id3); // ????????????memberid?????????????????????????????????????????????
                startActivity(intent3);
                finish();
            }
        });


        BT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode_scan();
//                BT3.setVisibility(View.INVISIBLE);
//                BT1.setVisibility(View.INVISIBLE);
//                img_menu.setVisibility(View.VISIBLE);
//                BT6.setVisibility(View.VISIBLE);
//                BT7.setVisibility(View.INVISIBLE);
//                BT4.setVisibility(View.VISIBLE);

                lin_scan.setVisibility(View.VISIBLE);
                lin_drop.setVisibility(View.VISIBLE);
                lin_scan_insert.setVisibility(View.VISIBLE);
                txtres_scan.setVisibility(View.VISIBLE);

                txtres_add.setVisibility(View.INVISIBLE);
                lin_scan_insert_DB.setVisibility(View.INVISIBLE);
                lin_scan_show.setVisibility(View.INVISIBLE);
                lin_scan_scan.setVisibility(View.INVISIBLE);
                lin_foodpic.setVisibility(View.INVISIBLE);
                LY_title.setVisibility(View.INVISIBLE);
                LY_type.setVisibility(View.INVISIBLE);
                lin_ing_add.setVisibility(View.INVISIBLE);

            }
        });//???????????????

        BT7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode_add();

//                BT1.setVisibility(View.INVISIBLE);
//                img_menu.setVisibility(View.INVISIBLE);
//                BT6.setVisibility(View.INVISIBLE);
//                BT7.setVisibility(View.INVISIBLE);
//                BT4.setVisibility(View.INVISIBLE);
//                BT3.setVisibility(View.VISIBLE);
                txtres_scan.setVisibility(View.INVISIBLE);
                lin_scan_insert.setVisibility(View.INVISIBLE);
                lin_scan_show.setVisibility(View.INVISIBLE);
                lin_drop.setVisibility(View.INVISIBLE);
                lin_scan_scan.setVisibility(View.INVISIBLE);
                LY_title.setVisibility(View.INVISIBLE);
                lin_scan.setVisibility(View.INVISIBLE);
                lin_foodpic.setVisibility(View.INVISIBLE);


                txtres_add.setVisibility(View.VISIBLE);
                lin_scan_insert_DB.setVisibility(View.VISIBLE);
                LY_type.setVisibility(View.VISIBLE);
                lin_ing_add.setVisibility(View.VISIBLE);


            }
        });//???????????????

        BT6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                BT1.setVisibility(View.VISIBLE);
//                img_menu.setVisibility(View.VISIBLE);
//                BT6.setVisibility(View.INVISIBLE);
//                BT7.setVisibility(View.INVISIBLE);
//                BT4.setVisibility(View.VISIBLE);
//                BT3.setVisibility(View.INVISIBLE);
                lin_scan_insert_DB.setVisibility(View.INVISIBLE);
                txtres_add.setVisibility(View.INVISIBLE);
                lin_scan_show.setVisibility(View.INVISIBLE);
                lin_drop.setVisibility(View.INVISIBLE);
                LY_type.setVisibility(View.INVISIBLE);
                lin_ing_add.setVisibility(View.INVISIBLE);
                lin_scan.setVisibility(View.INVISIBLE);


                LY_title.setVisibility(View.VISIBLE);
                txtres_scan.setVisibility(View.VISIBLE);
                lin_scan_insert.setVisibility(View.VISIBLE);
                lin_scan_scan.setVisibility(View.VISIBLE);
                lin_foodpic.setVisibility(View.VISIBLE);
            }
        });//???????????????




        BT5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String dateTime = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
                        TV3.setText(dateTime);
                    }

                }, year, month, day).show();
            }
        });//????????????

        BT3.setOnClickListener(new View.OnClickListener() {//????????????????????????
            @Override
            public void onClick(View v) {
                String barcode,ingerdient_ID_ed,ingerdient_type,FridgeID,expiration_date;

                barcode = String.valueOf(ED1.getText());
                ingerdient_ID_ed=String.valueOf(ED2.getText());
                ingerdient_type=String.valueOf(type);
                FridgeID = String.valueOf(TV1.getText());
                expiration_date = String.valueOf(TV3.getText());

                if(!barcode.equals("")&&!ingerdient_type.equals("")&&!ingerdient_ID_ed.equals("")&&!expiration_date.equals("")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[5];
                            field[0] = "barcode";
                            field[1] = "ingredient_title";
                            field[2] = "ingredient_type";
                            field[3] = "expiration_date";
                            field[4] = "FridgeID";
                            String[] data = new String[5];
                            data[0] = barcode;
                            data[1] = ingerdient_ID_ed;
                            data[2] = ingerdient_type;
                            data[3] = expiration_date;
                            data[4] = FridgeID;
                            Log.i("FridgeID",FridgeID);
                            Log.i("barcode",barcode);
                            Log.i("ingerdient_ID_ed",ingerdient_ID_ed);
                            Log.i("ingerdient_type",ingerdient_type);
                            Log.i("expiration_date",expiration_date);
                            PutData putData = new PutData("http://192.168.0.195/mike/barcode_check.php", "POST", field, data);//??????????????????????????????PHP
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Insert into fridge contain sucess")){
                                        Toast.makeText(getApplicationContext(),"Insert into fridge contain sucess",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),Fridge.class);
                                        startActivity(intent);
                                        finish();
                                    }else if(result.equals("Insert into ingredient sucess")){
                                        Toast.makeText(getApplicationContext(),"Insert into ingredient sucess",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),Fridge.class);
                                        startActivity(intent);
                                        finish();
                                    }else if(result.equals("Update Contain Quantity sucess")){
                                        Toast.makeText(getApplicationContext(),"Insert into ingredient sucess",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),Fridge.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }else {
                    if(!ingerdient_type.equals("")&&!ingerdient_ID_ed.equals("")&&!expiration_date.equals("")){
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[4];
                                field[0] = "ingredient_title";
                                field[1] = "ingredient_type";
                                field[2] = "expiration_date";
                                field[3] = "FridgeID";
                                String[] data = new String[4];
                                data[0] = ingerdient_ID_ed;
                                data[1] = ingerdient_type;
                                data[2] = expiration_date;
                                data[3] = FridgeID;
                                PutData putData = new PutData("http://192.168.0.195/mike/no_barcode_check.php", "POST", field, data);//????????????
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if(result.equals("Update Contain Quantity sucess")){
                                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),Fridge.class);
                                            startActivity(intent);
                                            finish();
                                        }else if(result.equals("Insert into fridge contain sucess")){
                                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),Fridge.class);
                                            startActivity(intent);
                                            finish();
                                        }else if(result.equals("Insert into ingredient sucess")){
                                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),Fridge.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "???????????????!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        BT4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String barcode, FridgeID, expiration_date, ingerdient_ID_tv, ingerdient_ID_sp;

                ingerdient_ID_tv = String.valueOf(TV4.getText());
                ingerdient_ID_sp = String.valueOf(title);
                barcode = String.valueOf(TV5.getText());
                FridgeID = String.valueOf(TV1.getText());
                expiration_date = String.valueOf(TV3.getText());


                if (!barcode.equals("") && !expiration_date.equals("") && !ingerdient_ID_tv.equals("")) {//???????????????
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[3];
                            field[0] = "barcode";
                            field[1] = "FridgeID";
                            field[2] = "expiration_date";
                            String[] data = new String[3];
                            data[0] = barcode;
                            data[1] = FridgeID;
                            data[2] = expiration_date;
                            PutData putData = new PutData("http://192.168.0.195/mike/SearchBarcode_insert.php", "POST", field, data);//?????????????????????
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("??????????????????????????????!")||result.equals("?????????????????????!")||result.equals("UPDATE sucess")||result.equals("INSERT sucess")) {
//                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),Fridge.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                } else {
                    if (!ingerdient_ID_sp.equals("") && !expiration_date.equals("")||barcode.equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[3];
                                field[0] = "ingredient_title";
                                field[1] = "FridgeID";
                                field[2] = "expiration_date";
                                String[] data = new String[3];
                                data[0] = ingerdient_ID_sp;
                                data[1] = FridgeID;
                                data[2] = expiration_date;
                                PutData putData = new PutData("http://192.168.0.195/mike/SearchID_insert.php", "POST", field, data);//?????????????????? ?????????????????????
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if (result.equals("??????????????????????????????!")||result.equals("?????????????????????!")||result.equals("UPDATE sucess")||result.equals("INSERT sucess")) {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),Fridge.class);
                                            startActivity(intent);
                                            finish();
                                        } else {

                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(),"???????????????!???????????????!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txtres_scan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LY_title.setVisibility(View.INVISIBLE);
                LY_type.setVisibility(View.VISIBLE);
                lin_ing_add.setVisibility(View.VISIBLE);
                lin_scan.setVisibility(View.INVISIBLE);
                lin_foodpic.setVisibility(View.INVISIBLE);
//                BT1.setVisibility(View.INVISIBLE);
                lin_scan_scan.setVisibility(View.INVISIBLE);
//                img_menu.setVisibility(View.VISIBLE);
//                BT6.setVisibility(View.INVISIBLE);
                lin_drop.setVisibility(View.INVISIBLE);
//                BT7.setVisibility(View.VISIBLE);
                lin_scan_show.setVisibility(View.VISIBLE);
//                BT4.setVisibility(View.INVISIBLE);
                lin_scan_insert.setVisibility(View.INVISIBLE);
//                BT3.setVisibility(View.VISIBLE);
                lin_scan_insert_DB.setVisibility(View.VISIBLE);
                txtres_scan.setVisibility(View.INVISIBLE);
                txtres_add.setVisibility(View.VISIBLE);


            }
        });
        txtres_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LY_title.setVisibility(View.VISIBLE);
                LY_type.setVisibility(View.INVISIBLE);
                lin_ing_add.setVisibility(View.INVISIBLE);
                lin_scan.setVisibility(View.INVISIBLE);
                lin_foodpic.setVisibility(View.VISIBLE);
//                BT1.setVisibility(View.VISIBLE);
                lin_scan_scan.setVisibility(View.VISIBLE);
//                img_menu.setVisibility(View.INVISIBLE);
//                BT6.setVisibility(View.INVISIBLE);
                lin_drop.setVisibility(View.INVISIBLE);
//                BT7.setVisibility(View.INVISIBLE);
                lin_scan_show.setVisibility(View.INVISIBLE);
//                BT4.setVisibility(View.VISIBLE);
                lin_scan_insert.setVisibility(View.VISIBLE);
//                BT3.setVisibility(View.INVISIBLE);
                lin_scan_insert_DB.setVisibility(View.INVISIBLE);
                txtres_scan.setVisibility(View.VISIBLE);
                txtres_add.setVisibility(View.INVISIBLE);


            }
        });
    }


    public static String getmid()
    {
        return mid;
    }


    public static String getfid()
    {
        return fid;
    }

    private void scanCode_scan(){
        IntentIntegrator integrator = new IntentIntegrator(context);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("??????????????????????????????");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    private void scanCode_add(){
        IntentIntegrator integrator_add = new IntentIntegrator(this);
        integrator_add.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator_add.setPrompt("??????????????????????????????");
        integrator_add.setCameraId(0);
        integrator_add.setBeepEnabled(true);
        integrator_add.setBarcodeImageEnabled(true);
        integrator_add.setOrientationLocked(false);
        integrator_add.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult SR = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (SR != null) {
            if (SR.getContents() != null) {
                String SC = SR.getContents();
                if (!SC.equals("")) {

                    ED1.setText(SC.toString());
                    TV5.setText(SC.toString());
                    fetch_title_into_textview(TV4);
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,intent);
            TV5.setText("????????????!???????????????!");
        }
    }


    public void fetch_title_into_textview(View view) {

        String barcode;
        barcode = String.valueOf(TV5.getText());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "barcode";
                String[] data = new String[1];
                data[0] = barcode;

                PutData putData = new PutData("http://192.168.0.195/mike/scanBarcode.php","POST", field, data);
                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
                        result=result.replace("\"", "");
                        TV4.setText(result);
                        if(result.equals("sucess")){
//                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                            fetch_title_into_textview(TV4);
                        }else{
//                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}