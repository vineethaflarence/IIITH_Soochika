package com.example.phone_directory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    String type;
    CardView staff,faculty,security,aarogya,maingate,others;
    EditText searchView;
    LinearLayout childLayout;

    RecyclerView rvSearchResults;

    ArrayList<model> filteredList=new ArrayList<>();
    public RetrofitAPI myAPI;
    public String baseurl ="https://ims.iiit.ac.in/";
    public ArrayList<model> modelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllData();

        staff = findViewById(R.id.staff);
        faculty=findViewById(R.id.faculty);
        security=findViewById(R.id.security);
        aarogya=findViewById(R.id.aarogya);
        maingate=findViewById(R.id.maingate);
        others=findViewById(R.id.others);
        searchView=findViewById(R.id.search_view);
        rvSearchResults=findViewById(R.id.rvSearchResults);
        childLayout=findViewById(R.id.childLayout);
        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,directory.class);
                in.putExtra("type","staff");
                startActivity(in);
            }
        });

        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in2 = new Intent(MainActivity.this,directory.class);
                in2.putExtra("type","security");
                startActivity(in2);
            }
        });
        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,directory.class);
                in.putExtra("type","faculty");
                startActivity(in);
            }
        });
        maingate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,directory.class);
                in.putExtra("type","maingate");
                startActivity(in);
            }
        });
        aarogya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,directory.class);
                in.putExtra("type","aarogya");
                startActivity(in);
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,directory.class);
                in.putExtra("type","others");
                startActivity(in);
            }
        });

        GlobalSearchAdapter adapter = new GlobalSearchAdapter(filteredList, MainActivity.this, "");
        rvSearchResults.setAdapter(adapter);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().trim().isEmpty()){
                    childLayout.setVisibility(View.VISIBLE);
                    rvSearchResults.setVisibility(View.GONE);
                }else {
                    childLayout.setVisibility(View.GONE);
                    rvSearchResults.setVisibility(View.VISIBLE);
                }
                filteredList.clear();
                ArrayList<model> filteredList = filter(s.toString());

                adapter.updateData(filteredList);

                adapter.notifyDataSetChanged();



            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        searchView.setText("");
    }

    public void getAllData () {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myAPI = retrofit.create(RetrofitAPI.class);
        Intent in = getIntent();
        Call<ArrayList<model>> call = myAPI.getAlldata("");

        ProgressDialog p = new ProgressDialog(MainActivity.this);
        p.setMessage("Loading...");
        p.show();

        call.enqueue(new Callback<ArrayList<model>>() {
            @Override
            public void onResponse(Call<ArrayList<model>> call,
                                   Response<ArrayList<model>> response) {
                if (response.isSuccessful()) {
                    modelArrayList = response.body();
                    Log.d("data", modelArrayList.toString());
                    //Toast.makeText(MainActivity.this, modelArrayList.toString(), Toast.LENGTH_SHORT).show();
                }
                p.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<model>> call, Throwable t) {
                p.dismiss();
                Toast.makeText(MainActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<model> filter(String text){

        try {

            for (model i : modelArrayList) {
                try {
                    if (i.getBody().toLowerCase().contains(text.toLowerCase()) || i.getDepartment().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(i);
                    }
                } catch (Exception e) {

                }

            }
            return filteredList;
        }catch (Exception e){
            return filteredList;
        }

    }


}