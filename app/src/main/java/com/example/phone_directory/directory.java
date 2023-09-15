package com.example.phone_directory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class directory extends AppCompatActivity {
    public RecyclerView idRVCourse;
    public ArrayList<model> modelArrayList;
    public RecyclerViewAdapter recyclerViewAdapter;
    public ProgressBar progressBar;
    public RetrofitAPI myAPI;
    public String baseurl ="https://ims.iiit.ac.in/";

    ArrayList<model> filteredList = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory);
        idRVCourse = findViewById(R.id.idRVCourse);
        progressBar = findViewById(R.id.idPBLoading);
        modelArrayList = new ArrayList<>();
        EditText editText=findViewById(R.id.search);



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filteredList.clear();
                ArrayList<model> filteredList = filter(editable.toString());
                recyclerViewAdapter.updateData(filteredList);

            }
        });

        if(InternetChecker.isInternetAvailable(directory.this)){
            getAlldata();
        }else {
// Retrieve the cached JSON string from SharedPreferences
            SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String globalSearchData = preferences.getString( getIntent().getStringExtra("type"), null);

            if (globalSearchData != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<model>>(){}.getType();
                modelArrayList = gson.fromJson(globalSearchData, type);

                recyclerViewAdapter = new RecyclerViewAdapter(modelArrayList, directory.this, getIntent().getStringExtra("type"));
                // below line is to set layout manager for our recycler view.
                LinearLayoutManager manager = new LinearLayoutManager(directory.this, RecyclerView.VERTICAL, false);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                // setting layout manager for our recycler view.
                idRVCourse.setLayoutManager(manager);
                idRVCourse.setHasFixedSize(true);
                //below line is to set adapter to our recycler view.
                idRVCourse.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }
            progressBar.setVisibility(View.GONE);
        }

    }

    public ArrayList<model> filter(String text){

        for(model i: modelArrayList){
            try {
                if(i.getBody().toLowerCase().contains(text.toLowerCase())|| i.getDepartment().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add(i);
                }
            }catch (Exception e){

            }

        }
        return filteredList;

    }


    public void getAlldata () {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myAPI = retrofit.create(RetrofitAPI.class);
        Intent in = getIntent();
        String typ = in.getStringExtra("type");
        Call<ArrayList<model>> call = myAPI.getAlldata(typ);
        modelArrayList.clear();
        call.enqueue(new Callback<ArrayList<model>>() {
            @Override
            public void onResponse(Call<ArrayList<model>> call,
                                   Response<ArrayList<model>> response) {
                if (response.isSuccessful()) {

                    String jsonString = new Gson().toJson(response.body());

                    // Store the JSON string in SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(typ, jsonString);
                    editor.apply();

                    // on successful we are hiding our progressbar.
                    progressBar.setVisibility(View.GONE);
                    modelArrayList = response.body();

                        recyclerViewAdapter = new RecyclerViewAdapter(modelArrayList, directory.this, typ);
                        // below line is to set layout manager for our recycler view.
                        LinearLayoutManager manager = new LinearLayoutManager(directory.this, RecyclerView.VERTICAL, false);
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        // setting layout manager for our recycler view.
                        idRVCourse.setLayoutManager(manager);
                        idRVCourse.setHasFixedSize(true);
                        //below line is to set adapter to our recycler view.
                        idRVCourse.setAdapter(recyclerViewAdapter);


                }else {

                    // on failure we are showing a toast message.
                    Toast.makeText(directory.this, "Fail to get data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<model>> call, Throwable t) {
                Toast.makeText(directory.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }



        });
    }
}
