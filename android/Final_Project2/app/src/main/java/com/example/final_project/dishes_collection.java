package com.example.final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class dishes_collection extends AppCompatActivity {

    private static String apiurl="http://192.168.0.195/yu/user_collection_fetch.php";
    private static String title[];
    private static String dishes_type[];
    private static String food_main[];
    private static String country_cusine[];
    private static String m_or_v[];
    private static String description[];
    private static String img[];
    private static String Provenance[];
//    private static String step[];
    private static String lacked_ing[];
    private static String Dishes_id[];
    private static String author[];

    String t,dt,fm,cc,mv,dc,pro,st,i_path,mid,LIT,did,fid,username, refridgeid,aut;
    String[] DID,IMG_PAT,DISHES_TYPE,
            FOOD_MAIN,
            COUNTRY_CUSINE,
            M_OR_V,
            DESCRIPTION,
            PROVENANCE,
            STEP,
            LACKED_ING,TITLE,AUTHOR;
    TextView txt_fridge_id;
    ListView cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_dishes_collection);

        cl=(ListView)findViewById(R.id.lv_rec);
        mid=getIntent().getStringExtra("memberid");

        TextView tbtv=(TextView)findViewById(R.id.textView2); //toolbar?????????textview
        ImageView btn_back = (ImageView)findViewById(R.id.imageView); //???????????????
        tbtv.setText("????????????");

        // ??????????????????id
        String username = getIntent().getStringExtra("memberid");//???????????????????????????
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id3);//???????????????????????????
        txtloginName.setText(username);//???????????????????????????

        String refridgeid = getIntent().getStringExtra("refridgeid");//???????????????????????????
        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id2);//???????????????????????????
        txtrefridgeid.setText(refridgeid);//???????????????????????????

        fetch_data_into_array(cl);







        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);
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
                Intent intent3 = new Intent(getApplicationContext(),MainActivity.class);
                intent3.putExtra("refridgeid",id2);
                intent3.putExtra("memberid",id3); // ????????????memberid?????????????????????????????????????????????
                startActivity(intent3);
                finish();
            }
        });




    }//End of oncreate


    public void fetch_data_into_array(ListView view)
    {

        String url1 = apiurl +"?member_id="+mid;
        Log.v("url",url1);

        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    title = new String[ja.length()];
                    img = new String[ja.length()];
                    Dishes_id= new String[ja.length()];


                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        Dishes_id[i]=jo.getString("Dishes_id");
                        title[i] = jo.getString("title");
                        img[i] ="http://192.168.0.195/img/" + jo.getString("img");;


                    }

                    DID=Dishes_id;

//                    STEP =step;
//                    LACKED_ING = lacked_ing;
                    if(title.length==0)
                    {

                        myadapter adptr = new myadapter(getApplicationContext(), title, img,Dishes_id);
                        view.setAdapter(adptr);
                        setListViewHeight(view);
                        adptr.notifyDataSetChanged();


                        AlertDialog.Builder alertDialog =
                                new AlertDialog.Builder(dishes_collection.this);
                        alertDialog.setTitle("?????????");
                        alertDialog.setMessage("????????????????????????");
                        alertDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //startActivity(new Intent(getApplicationContext(),User_setting.class));

//                                Intent uintent = new Intent(getApplicationContext(),MainActivity.class);
//                                uintent.putExtra("memberid",username);//?????????????????????
//
//
//                                uintent.putExtra("refridgeid",refridgeid);//?????????????????????
//                                startActivity(uintent);
                                onBackPressed();
                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();


//                        Snackbar.make(view, "????????????????????????",Snackbar.LENGTH_LONG).setAction("Action", null).show();


                    }else {
                        myadapter adptr = new myadapter(getApplicationContext(), title,  img,Dishes_id);
                        view.setAdapter(adptr);
                        setListViewHeight(view);
                        adptr.notifyDataSetChanged();
                        view.setOnItemClickListener((parent, view, position, id) -> {
                            final String s = cl.getItemAtPosition(position).toString();

                            did = DID[position];


                            Intent intent1 = new Intent(dishes_collection.this, dishes_info_rec_random.class);
                            intent1.putExtra("mid", mid);
                            intent1.putExtra("did", did);
                            intent1.putExtra("fid", fid);
                            startActivity(intent1);
                        });

                    }




                } catch (Exception ex) {
//                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Exception: ", ex.getMessage());
                }
            }

            @Override
            protected String doInBackground(String... strings)
            {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer data = new StringBuffer();
                    String line;


                    while ((line = br.readLine()) != null) {
                        data.append(line + "\n");
                    }
                    br.close();

                    return data.toString();

                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }

        }
        dbManager obj=new dbManager();
        obj.execute(url1);

    }


    //???listview????????????????????????????????????????????????????????????
    public void setListViewHeight(ListView listView) {
        //??????listView???adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //listAdapter.getCount()????????????????????????
        for (int i = 0,len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // listView.getDividerHeight()???????????????????????????????????????
        // params.height??????????????????ListView???????????????????????????
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter .getCount() - 1));
        listView.setLayoutParams(params);
    }

    class myadapter extends ArrayAdapter<String>
    {
        Context context;
        String ttl[];

        String id[];
        String rimg[];


        myadapter(Context c, String ttl[],  String rimg[],String id[])
        {
            super(c, R.layout.collectionlist_row, R.id.collection_title,ttl);
            context=c;
            this.ttl=ttl;
            this.id=id;
            this.rimg=rimg;

        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.collectionlist_row,parent,false);


            TextView tv1=row.findViewById(R.id.collection_title);
//            TextView tv6=row.findViewById(R.id.collection_type);
            ImageView img=row.findViewById(R.id.collection_img);

            tv1.setText(ttl[position]);
//            tv6.setText(dsc[position]);


            String did=id[position];

            Button button_add=(Button)row.findViewById(R.id.delete_from_collection_btn);

            button_add.setOnClickListener(v -> {



                if(!ttl[position].equals(""))
                {
                    Log.v("did",did);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            int count=0;
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "member_id";
                            field[1] = "Dishes_id";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = mid;
                            data[1] = did;
                            PutData putData = new PutData("http://192.168.0.195/yu/delete_from_collection.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    count++;
                                    Log.i("message: ", result);
                                    Log.i("mid: ", mid);
                                    Log.i("mid: ", mid);

                                    if (result.equals("Delete Success"))
                                    {
                                        Snackbar.make(v,"????????????", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        fetch_data_into_array(cl);

                                    }
                                    else {
                                        Snackbar.make(v,result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        fetch_data_into_array(cl);
                                    }

//                                    if (result.equals("Delete sucess"))
//                                    {
//                                        Snackbar.make(v,result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
//
//                                        fetch_data_into_array(cl);
//
//                                        Snackbar.make(v,"HI", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                    }
//                                    else {
//                                        Snackbar.make(v,result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                    }

                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }



            });

            String url=rimg[position];


            class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
                private String url;
                private ImageView imageView;

                public ImageLoadTask(String url, ImageView imageView) {
                    this.url = url;
                    this.imageView = imageView;
                }

                @Override
                protected Bitmap doInBackground(Void... params) {
                    try {
                        URL connection = new URL(url);
                        InputStream input = connection.openStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 400, 400, true);
                        return resized;
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    imageView.setImageBitmap(result);
                }
            }
            ImageLoadTask obj=new ImageLoadTask(url,img);
            obj.execute();


            return row;
        }


    }

}