package com.example.phone_directory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    // as we are making get request so we are displaying
    // GET as annotation.
    // and inside we are passing last parameter for our url.
    // URL = https://ims-dev.iiit.ac.in/telecontacts.php?typ=staff
    @GET("telecontacts.php?")
    // as we are calling data from array so we are calling
    // it with array list and naming that method as getAllCourses();
    //Call<ArrayList<model>> getAlldata(); ;
    Call<ArrayList<model>> getAlldata(@Query("typ") String emptype);

}
