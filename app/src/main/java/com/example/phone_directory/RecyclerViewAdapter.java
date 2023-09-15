package com.example.phone_directory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    public ArrayList<model> modelArrayList;
    public Context context;
    public String type;

    public RecyclerViewAdapter(ArrayList<model> modelArrayList, Context context, String type) {
        this.modelArrayList = modelArrayList;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleview, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder,int position) {
        model modal = modelArrayList.get(position);
        holder.bodytxt.setText(modal.getBody());

        if (Objects.equals(type, "security") || Objects.equals(type, "aarogya")|| Objects.equals(type, "maingate")||Objects.equals(type, "others")){
            if (!modal.getLocation().isEmpty()) {
                holder.department.setText(modal.getLocation());
            } else {
                holder.department.setText(View.GONE);
            }
            holder.mail.setVisibility(View.GONE);

        }else {
            holder.department.setText(modal.getDepartment());
            if(modal.getMobile().isEmpty()){
                holder.call.setEnabled(true);
                holder.call.setAlpha(1.0f);
            }else {
                holder.call.setEnabled(true);
                holder.call.setAlpha(1.0f);
            }

        }
      //  Glide.with(context)
             //   .load("url")//modal.getprofile()
               // .centerCrop()
               // .circleCrop()
               // .into(holder.ivProfile);


        //holder.email.setText(modal.getEmail());
        holder.initials.setText(String.valueOf(modal.getBody().charAt(0)));
        holder.initials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent person_details = new Intent((Activity) context, ViewPerson.class);
                person_details.putExtra("name", modal.getBody());
                person_details.putExtra("designation", modal.getDesignation());
                person_details.putExtra("department", modal.getDepartment());
                person_details.putExtra("id", modal.getTitle());
                person_details.putExtra("emptype", modal.getEmail());
                context.startActivity(person_details);


            }
        });
        holder.mail.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("IntentReset")
            @Override
            public void onClick(View view) {


                String email = modal.getEmail();

                String[] TO = {email};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");


                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                try {
                    context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    Log.i("Finished sending email...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model modal1 = modelArrayList.get(position);
                String number = modal1.getMobile();
                if(Objects.equals(type, "faculty") || Objects.equals(type, "staff")){
                    if (number.isEmpty()){
                        number = modal1.getExtension();
                    }
                }

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));

                if (Build.VERSION.SDK_INT > 23) {
                    context.startActivity(intent);
                } else {

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "Permission Not Granted ", Toast.LENGTH_SHORT).show();
                    } else {
                        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                        ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, 9);
                        context.startActivity(intent);
                    }
                }
            }
        });




    }


    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public void updateData(ArrayList<model> filteredList) {
        this.modelArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView  bodytxt, email,department;



        ImageView call, ivProfile;
        ImageView mail;
        TextView initials;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
             department = itemView.findViewById(R.id.designation);
            bodytxt = itemView.findViewById(R.id.bodytxt);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            //email = itemView.findViewById(R.id.email);
            mail = itemView.findViewById(R.id.mail);
            call = itemView.findViewById(R.id.call);
            initials = itemView.findViewById(R.id.initials);

        }
    }
}


