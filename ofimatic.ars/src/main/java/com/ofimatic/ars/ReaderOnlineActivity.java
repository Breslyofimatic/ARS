package com.ofimatic.ars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


public class ReaderOnlineActivity extends ActionBarActivity {

    DataAccess dataAccess = new DataAccess();
    String error;
    ProgressDialog progressdialog;

    TextView noafiliado;
    TextView nopoliza;
    TextView nombre;
    TextView cliente;
    TextView activo;
    TextView plan;
    TextView nss;
    TextView tiposangre;
    TextView direccion;
    TextView telefono;
    TextView fechanac;
    TextView fechaemision;
    TextView estatus;
    ImageView foto;
    ImageView iconFechaCorrectaFPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_online);

        noafiliado = (TextView) findViewById(R.id.tvNoAfiliadoRO);
        nopoliza = (TextView) findViewById(R.id.tvNoPolizaRO);
        nombre = (TextView) findViewById(R.id.tvAfiliadoRO);
        cliente = (TextView) findViewById(R.id.tvClienteRO);
        plan = (TextView) findViewById(R.id.tvPlanRO);
        nss = (TextView) findViewById(R.id.tvNSSRO);
        tiposangre = (TextView) findViewById(R.id.tvTipoSangreRO);
        direccion = (TextView) findViewById(R.id.tvDireccionRO);
        telefono = (TextView) findViewById(R.id.tvTelefonoRO);
        fechanac = (TextView) findViewById(R.id.tvFechanacRO);
        fechaemision = (TextView) findViewById(R.id.tvFechaentRO);
        foto = (ImageView) findViewById(R.id.FPMimageView);
        estatus = (TextView) findViewById(R.id.tvEstatusRO);

        iconFechaCorrectaFPM = (ImageView) findViewById(R.id.FPMimgVencimiento);

        noafiliado.setText(DataAccess.noAfiliado);
        nopoliza.setText(DataAccess.noPoliza);
        nombre.setText(DataAccess.Afiliado);
        cliente.setText(DataAccess.clienteCompany);
        plan.setText(DataAccess.Plan);
        nss.setText(DataAccess.NSS);
        tiposangre.setText(DataAccess.tipoSangre);
        fechanac.setText(DataAccess.FechaNac);
        direccion.setText(DataAccess.Direccion);
        telefono.setText(DataAccess.Telefono);
        fechaemision.setText(DataAccess.fechaEmision);


        try {

            if (DataAccess.Activo == 1)
            {
                iconFechaCorrectaFPM.setVisibility(View.VISIBLE);
                iconFechaCorrectaFPM.setImageResource(R.drawable.ic_good_or_tick);
                estatus.setVisibility(View.VISIBLE);
                estatus.setText(getString(R.string.Activo));
                estatus.setTextColor(Color.parseColor("#2A9E29"));
            }
            else {
                iconFechaCorrectaFPM.setVisibility(View.VISIBLE);
                iconFechaCorrectaFPM.setImageResource(R.drawable.ic_banned);
                estatus.setVisibility(View.VISIBLE);
                estatus.setText(getString(R.string.Inactivo));
                estatus.setTextColor(Color.parseColor("#B00B0A"));
            }

            foto.setImageBitmap(DataAccess.foto);

        } catch (Exception e) {
            e.printStackTrace();
            Toast msj = Toast.makeText(this, getString(R.string.ErrorSystem), Toast.LENGTH_LONG);
            msj.setGravity(Gravity.CENTER,0,0);
            finish();
        }
    }


    public void AutorizarServ (View v)
    {
//        Intent intent = new Intent(ReaderOnlineActivity.this, AuthorizeActivity.class);
//        startActivity(intent);
        new GetAfiliado().execute((Void[]) null);
    }

    /**
     * Proceso para obtener los datos de una persona.
     */
    class GetAfiliado extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Modo estricto para los hilos.
            StrictMode.enableDefaults();
            //Progress Dialogo aparece un Mensaje mientras se Busca el producto seleccionado
            progressdialog = new ProgressDialog(ReaderOnlineActivity.this);
            progressdialog.setMessage(getString(R.string.Buscando));
            progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressdialog.setIndeterminate(false);
            progressdialog.setCancelable(false);
            progressdialog.show();
        }

        protected Void doInBackground(Void ... arg0) {
            // Actualiza la UI desde un hilo
            try {
                //DataAccess.noPoliza = NFC.Arreglo[0];
                JSONObject json =  dataAccess.getAllServices(ReaderOnlineActivity.this);
                JSONObject jsonMedics = dataAccess.getAllMedic(ReaderOnlineActivity.this);
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

                            Intent intent = new Intent(ReaderOnlineActivity.this, AuthorizeActivity.class);
                            startActivity(intent);

                    }
                    else {
                        Toast msj = Toast.makeText(ReaderOnlineActivity.this, getString(R.string.NotFound), Toast.LENGTH_LONG);
                        msj.setGravity(Gravity.CENTER,0,0);
                        msj.show();
                    }
                }
                else{
                    Toast msj = Toast.makeText(ReaderOnlineActivity.this, getString(R.string.NoConection), Toast.LENGTH_LONG);
                    msj.setGravity(Gravity.CENTER,0,0);
                    msj.show();

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reader_online, menu);
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
