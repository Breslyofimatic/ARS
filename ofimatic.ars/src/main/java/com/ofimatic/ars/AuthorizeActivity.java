package com.ofimatic.ars;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class AuthorizeActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

//    ArrayList<HashMap<String, String>> nameList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> nameList = new ArrayList<String>();
    HashMap<String, String> map = new HashMap<String, String>();

    String []  arrayString;
    String []  arrayID;
    Spinner spinServices;
    Spinner spinMedics;
    DataAccess data = new DataAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        spinServices = (Spinner) findViewById(R.id.SpinServicios);
        spinMedics = (Spinner) findViewById(R.id.SpinMedicos);

        MyAdapterServices adapter = new MyAdapterServices(this, R.layout.row, DataAccess.servicesName);
        spinServices.setAdapter(adapter);

        //spinMedics.setOnItemSelectedListener(this);
        spinServices.setOnItemSelectedListener(this);

       // ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.row, DataAccess.medicNames);

    }


    String [] nombres;
    public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {

                spinMedics.setVisibility(View.VISIBLE);

                String servicesID = data.findServicesID(position);
                nombres =  data.ListMedics(servicesID);
                MyAdapterMedic adapter= new MyAdapterMedic(AuthorizeActivity.this, R.layout.row, nombres);
                spinMedics.setAdapter(adapter);

//            if (position == 0) {
//                spinMedics.setVisibility(View.GONE);
//            }
//            else
//            {
//                spinMedics.setVisibility(View.VISIBLE);
//                MyAdapterMedic adapter= new MyAdapterMedic(AuthorizeActivity.this, R.layout.row, DataAccess.medicNames);
//                spinMedics.setAdapter(adapter);
//            }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class MyAdapterServices extends ArrayAdapter<String> {

        public MyAdapterServices(Context context, int textViewResourceId, String[] names) {
            super(context, textViewResourceId, names);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
           // spinServices.getSelectedItemPosition();

            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);

            TextView label=(TextView)row.findViewById(R.id.Nombre);
            label.setText(DataAccess.servicesName[position] );

            return row;
        }
    }

    public class MyAdapterMedic extends ArrayAdapter<String> {

        public MyAdapterMedic(Context context, int textViewResourceId, String [] names) {
            super(context, textViewResourceId, names);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);

            TextView label=(TextView)row.findViewById(R.id.Nombre);
            label.setText(nombres[position] );

//            TextView IDlabel =(TextView)row.findViewById(R.id.ID);
//            IDlabel.setText(DataAccess.medicNames[position] );

            return row;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.authorize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
