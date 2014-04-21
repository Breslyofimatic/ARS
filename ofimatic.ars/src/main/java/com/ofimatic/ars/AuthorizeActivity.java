package com.ofimatic.ars;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ofimatic.library.DialogHandler;
import com.ofimatic.library.TimeOutApp;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class AuthorizeActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    DataAccess dataAccess = new DataAccess();
    String error;
    ProgressDialog progressdialog;

    Spinner spinServices;
    Spinner spinMedics;
    EditText monto;
    String [] listaMedicos;
    String [] nombresMedicos;
    String idMedico;
    String servicesID;

    public static String afiliado;
    DialogHandler dialogo = new DialogHandler();
    TimerTask updateProfile;

    private void initUI(){

        setContentView(R.layout.activity_authorize);

            //Timer para la Aplicación
        Timer timer = new Timer();
        updateProfile = new TimeOutApp(AuthorizeActivity.this, MainActivity.class);
        timer.schedule(updateProfile, 10000);

        spinServices = (Spinner) findViewById(R.id.SpinServicios);
        spinMedics = (Spinner) findViewById(R.id.SpinMedicos);
        monto = (EditText) findViewById(R.id.editTextMonto);

        MyAdapterServices adapter = new MyAdapterServices(this, R.layout.row, DataAccess.servicesName);
        spinServices.setAdapter(adapter);

        spinMedics.setOnItemSelectedListener(this);
        spinServices.setOnItemSelectedListener(this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

       // ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.row, DataAccess.medicNames);

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initUI();

    }

    public void bottonProcesar(View view)
    {
         DataAccess.idServicio = servicesID;
         dataAccess.idMedico = idMedico;
         dataAccess.montoServicio = monto.getText().toString();


        dialogo.Confirm(AuthorizeActivity.this, "Autorizacion", "¿Esta seguro que desea autorizar este servicio?", "No", "Si",
                R.drawable.ic_launcher, okProcess(), cancelProcess());

    }

    public Runnable okProcess(){
        return new Runnable() {
            public void run() {
                DataAccess.idServicio = servicesID;
                dataAccess.idMedico = idMedico;
                dataAccess.montoServicio = monto.getText().toString();

                new GetProcedure().execute((Void[]) null);
            }
        };
    }

    /**
     * Proceso para Cancelación del mensaje.
     */
    public Runnable cancelProcess(){
        return new Runnable() {
            public void run() {
            }
        };
    }


    /**
     * Procesa el pago del servicio.
     */
    class GetProcedure extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Modo estricto para los hilos.
            StrictMode.enableDefaults();
            //Progress Dialogo aparece un Mensaje mientras se Busca el producto seleccionado
            progressdialog = new ProgressDialog(AuthorizeActivity.this);
            progressdialog.setMessage(getString(R.string.Procesando));
            progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressdialog.setIndeterminate(false);
            progressdialog.setCancelable(false);
            progressdialog.show();
        }

        protected Void doInBackground(Void ... arg0) {
            // Actualiza la UI desde un hilo
            try {
                //DataAccess.noPoliza = NFC.Arreglo[0];
                JSONObject json =  dataAccess.getPayAuthorize(AuthorizeActivity.this);
                error= "";
            }
            catch(Exception e)  {

                error= "Error de conexion";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void file_url) {
            //Para (Stop) el progress dialogo
            if (progressdialog!=null) {
                progressdialog.dismiss();

                if (error=="")
                {
                    if (DataAccess.encontrado){

                        updateProfile.cancel();
                        Intent intent = new Intent(AuthorizeActivity.this, ResultActivity.class);
                        startActivity(intent);

                    }
                    else {
                        Toast msj = Toast.makeText(AuthorizeActivity.this, getString(R.string.NotProcess), Toast.LENGTH_LONG);
                        msj.setGravity(Gravity.CENTER,0,0);
                        msj.show();
                    }
                }
                else{
                    Toast msj = Toast.makeText(AuthorizeActivity.this, getString(R.string.NoConection), Toast.LENGTH_LONG);
                    msj.setGravity(Gravity.CENTER,0,0);
                    msj.show();

                }
            }
        }
    }

    public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {

        final int spinIdServ = R.id.SpinServicios;
        final int spinIdMed = R.id.SpinMedicos;

        switch (parentView.getId()) {
            case spinIdServ:
                servicesID = dataAccess.findServicesID(position);
                String nombre;

                listaMedicos = dataAccess.ListMedics(servicesID);
                nombresMedicos = new String[listaMedicos.length];

                for (int i = 0; i < listaMedicos.length; i++) {
                    nombre = listaMedicos[i].split(";")[1];
                    nombresMedicos[i] = nombre;
                }

                MyAdapterMedic adapter = new MyAdapterMedic(AuthorizeActivity.this, R.layout.row, nombresMedicos);
                spinMedics.setAdapter(adapter);

                String name;
                for (int i = 0; i < nombresMedicos.length; i++) {

                    name = nombresMedicos[i];

                    if (spinMedics.getSelectedItem().equals(name)) {
                        for (int j = 0; j < listaMedicos.length; j++) {

                            if (listaMedicos[j].split(";")[1].equals(name)) {
                                idMedico = listaMedicos[j].split(";")[0];
                            }
                        }
                    }
                }

                break;

            case spinIdMed :

                String n;
                for (int i = 0; i < nombresMedicos.length; i++) {

                    n = nombresMedicos[i];

                    if (spinMedics.getSelectedItem().equals(n)) {
                        for (int j = 0; j < listaMedicos.length; j++) {

                            if (listaMedicos[j].split(";")[1].equals(n)) {
                                idMedico = listaMedicos[j].split(";")[0];
                            }
                        }
                    }
                }
                break;
        }

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
            label.setText(nombresMedicos[position] );

//            TextView IDlabel =(TextView)row.findViewById(R.id.ID);
//            IDlabel.setText(DataAccess.medicNames[position] );

            return row;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                bottonProcesar(new View(AuthorizeActivity.this));
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.authorize, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public class CustomTimerTask extends TimerTask {

        private Context context;

        // Write Custom Constructor to pass Context
        public CustomTimerTask(Context con) {
            this.context = con;
        }

        Handler  mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                String mString=(String)msg.obj;
                Toast toast =  Toast.makeText(context, mString, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        };

        @Override
        public void run() {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Message msg = new Message();
                    msg.obj = "La Sección Expiró";
                    mHandler.sendMessage(msg);
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
            }).start();

            super.cancel();

        }



    }

}
