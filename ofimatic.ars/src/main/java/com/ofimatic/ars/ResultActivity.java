package com.ofimatic.ars;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ResultActivity extends ActionBarActivity {


    TextView noAprobacion;
    TextView nomAfiliado;
    TextView nomServ;
    TextView nomMedico;
    TextView montoServ;
    TextView descuento;
    TextView montoPagar;
    TextView fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        noAprobacion = (TextView) findViewById(R.id.tvNoAprobacionRes);
        nomAfiliado = (TextView) findViewById(R.id.tvNombreRes);
        nomServ = (TextView) findViewById(R.id.tvServicioRes);
        nomMedico = (TextView) findViewById(R.id.tvMedicoRes);
        montoServ = (TextView) findViewById(R.id.tvMontoServRes);
        descuento = (TextView) findViewById(R.id.tvDescuentoRes);
        montoPagar = (TextView) findViewById(R.id.tvMontoPagarRes);
        fecha = (TextView) findViewById(R.id.tvFechaRes);

        noAprobacion.setText(DataAccess.noAprobacion.toString());
        nomAfiliado.setText(DataAccess.Afiliado);
        nomServ.setText(DataAccess.nombreServicio);
        nomMedico.setText(DataAccess.nombreMedico);
        montoServ.setText(DataAccess.montoServicio);
        descuento.setText(DataAccess.descuentoServicio);
        montoPagar.setText(DataAccess.montoPagar);
        fecha.setText(DataAccess.Fecha);

//        result = (TextView) findViewById(R.id.tvResult1);
//        result.setText("ID del Afiliado: " + AuthorizeActivity.afiliado +" "+ "Total: "+AuthorizeActivity.total +" "+ "Descuento: "+ AuthorizeActivity.desc );

    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.result, menu);
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

}
