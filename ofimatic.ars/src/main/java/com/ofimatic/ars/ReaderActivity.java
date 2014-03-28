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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ofimatic.library.NFC;

import org.json.JSONObject;


public class ReaderActivity extends ActionBarActivity {

    DataAccess dataAccess = new DataAccess();
    String error;
    ProgressDialog progressdialog;
    TextView noPolizatv;
    TextView noAfiliadotv;
    TextView afiliadotv;

    /*****************************************/
    //Faltan estos textview en el diseño.
    TextView clienteCompanytv;
    TextView plantv;
    TextView fechanactv;

    ImageView iconFechaCorrectatab1;
    TextView estatus;
    Button btndetalles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        StrictMode.enableDefaults();

        noPolizatv = (TextView) findViewById(R.id.tvNoPoliza);
        noAfiliadotv = (TextView) findViewById(R.id.tvNoAfiliado);
        afiliadotv = (TextView) findViewById(R.id.tvAfiliado);

        iconFechaCorrectatab1 = (ImageView) findViewById(R.id.tab1imgVencimiento);
        estatus = (TextView) findViewById(R.id.tab1Estatus);
        btndetalles = (Button)findViewById(R.id.buttonDetalles);

        noAfiliadotv.setText(DataAccess.noAfiliado);
        noPolizatv.setText(DataAccess.noPoliza);
        afiliadotv.setText(DataAccess.Afiliado);

    }


    public void findPerson(View V) {
        new GetPerson().execute((Void[]) null);
    }

    /**
     * Proceso para obtener los datos de una persona.
     */
    class GetPerson extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Modo estricto para los hilos.
            StrictMode.enableDefaults();
            //Progress Dialogo aparece un Mensaje mientras se Busca el producto seleccionado
            progressdialog = new ProgressDialog(ReaderActivity.this);
            progressdialog.setMessage(getString(R.string.Buscando));
            progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressdialog.setIndeterminate(false);
            progressdialog.setCancelable(false);
            progressdialog.show();
        }

        protected Void doInBackground(Void ... arg0) {
            // Actualiza la UI desde un hilo
            try {
                DataAccess.noPoliza = NFC.Arreglo[0];
                JSONObject json =  dataAccess.getProfile(ReaderActivity.this);
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
                        //Validación del Tag ID:
                        if (NFC.CardID == dataAccess.tagID){
                            Intent intent = new Intent(ReaderActivity.this, ReaderOnlineActivity.class);
                            startActivity(intent);
                        }
                        else {
                            iconFechaCorrectatab1.setVisibility(View.VISIBLE);
                            iconFechaCorrectatab1.setImageResource(R.drawable.ic_error);
                            estatus.setVisibility(View.VISIBLE);
                            estatus.setText(getString(R.string.Invalida));
                            estatus.setTextColor(Color.parseColor("#B00B0A"));
                            btndetalles.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Toast msj = Toast.makeText(ReaderActivity.this, getString(R.string.NotFound), Toast.LENGTH_LONG);
                        msj.setGravity(Gravity.CENTER,0,0);
                        msj.show();
                    }
                }
                else{
                    Toast msj = Toast.makeText(ReaderActivity.this, getString(R.string.NoConection), Toast.LENGTH_LONG);
                    msj.setGravity(Gravity.CENTER,0,0);
                    msj.show();

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reader, menu);
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
