package com.ofimatic.ars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ofimatic.library.NFC;
import com.ofimatic.library.TimeOutApp;
import java.util.Timer;
import java.util.TimerTask;


public class ReaderActivity extends ActionBarActivity {

    DataAccess dataAccess = new DataAccess();
    String error;
    ProgressDialog progressdialog;
    TextView noPolizatv;
    TextView noAfiliadotv;
    TextView afiliadotv;
    TextView clienteCompanytv;
    TextView plantv;
    TextView fechanactv;
    ImageView iconFechaCorrectatab1;
    TextView estatus;
    Button btndetalles;
    public static TimerTask timerTask;

    private void initUI(){
        setContentView(R.layout.activity_reader);

        StrictMode.enableDefaults();

        //Timer para la Aplicación
        Timer timer = new Timer();
        timerTask = new TimeOutApp(ReaderActivity.this, MainActivity.class);
        timer.schedule(timerTask, 60000);

        noPolizatv = (TextView) findViewById(R.id.tvNoPoliza);
        noAfiliadotv = (TextView) findViewById(R.id.tvNoAfiliado);
        afiliadotv = (TextView) findViewById(R.id.tvAfiliado);
        clienteCompanytv = (TextView) findViewById(R.id.tvClienteCompany);
        plantv = (TextView) findViewById(R.id.tvPlan);
        fechanactv = (TextView) findViewById(R.id.tvFechanac);

        iconFechaCorrectatab1 = (ImageView) findViewById(R.id.tab1imgVencimiento);
        estatus = (TextView) findViewById(R.id.tab1Estatus);
        btndetalles = (Button)findViewById(R.id.buttonDetalles);

        noAfiliadotv.setText(DataAccess.noAfiliado);
        noPolizatv.setText(DataAccess.noPoliza);
        afiliadotv.setText(DataAccess.Afiliado);
        clienteCompanytv.setText(DataAccess.clienteCompany);
        plantv.setText(DataAccess.Plan);
        fechanactv.setText(DataAccess.FechaNac);

        try{
                //Validacion del tag ID
            if (NFC.CardID.equals(DataAccess.tagID))
            {
                iconFechaCorrectatab1.setVisibility(View.VISIBLE);
                iconFechaCorrectatab1.setImageResource(R.drawable.ic_good_or_tick);
                estatus.setVisibility(View.VISIBLE);
                estatus.setText(getString(R.string.Activo));
                estatus.setTextColor(Color.parseColor("#2A9E29"));
            }
            else {
                iconFechaCorrectatab1.setVisibility(View.VISIBLE);
                iconFechaCorrectatab1.setImageResource(R.drawable.ic_banned);
                estatus.setVisibility(View.VISIBLE);
                estatus.setText(getString(R.string.Invalida));
                estatus.setTextColor(Color.parseColor("#B00B0A"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast msj = Toast.makeText(ReaderActivity.this, getString(R.string.Invalida), Toast.LENGTH_LONG);
            msj.setGravity(Gravity.CENTER,0,0);
            msj.show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        if ( ReaderActivity.timerTask!=null)
        {
            ReaderActivity.timerTask.cancel();
        }
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initUI();

    }

    public void findPerson(View V) {
        new GetAfiliado().execute((Void[]) null);
    }

    /**
     * TODO: Proceso para obtener los datos de una persona.
     */
    class GetAfiliado extends AsyncTask<Void, Void, Void> {

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
                dataAccess.getProfile(ReaderActivity.this);
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
                        if (NFC.CardID.equals(dataAccess.tagID)){
                            timerTask.cancel();
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
}
