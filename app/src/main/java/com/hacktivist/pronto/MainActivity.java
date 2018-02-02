package com.hacktivist.pronto;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hacktivist.pronto.Config.StringConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;


    Spinner sp_categories;
    EditText et_datePicker;
    Calendar calendar;


    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        sp_categories = findViewById(R.id.sp_categories);
        et_datePicker =  findViewById(R.id.et_datePicker);

        calendar = Calendar.getInstance();


        initDatePicker();
        volleyFetchJobCategories();


    }

    private void volleyFetchJobCategories() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, StringConfig.FL_JOB_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String strStatus = jsonObject.getString("status");

                    if (strStatus.equals("success")) {

                        List<String> nameList = new ArrayList<>();

                        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                        JSONArray jsonArray = jsonObjectResult.getJSONArray("job_bundle_categories");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String job_bundles = jsonObject1.getString("job_bundles");
                            int id = jsonObject1.getInt("id");
                            String name = jsonObject1.getString("name");

                            nameList.add(name);

                            Log.d(StringConfig.LOG_TAG, "job_bundles : " + job_bundles);
                            Log.d(StringConfig.LOG_TAG, "id : " + id);
                            Log.d(StringConfig.LOG_TAG, "name : " + name);


                            arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, nameList);
                            sp_categories.setAdapter(arrayAdapter);



                        }


                    } else {

                        Toast.makeText(MainActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);

    }

    private void initDatePicker(){



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_datePicker.setText(sdf.format(calendar.getTime()));

    }

}
