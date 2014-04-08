package com.ofimatic.ars;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ResultActivity extends ActionBarActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ReciboTemplate reciboTemplate = new ReciboTemplate();

        String recibo = reciboTemplate.recibo(DataAccess.Fecha,DataAccess.noAprobacion.toString(), DataAccess.noAfiliado,DataAccess.Afiliado, DataAccess.nombreServicio, DataAccess.nombreMedico, DataAccess.montoServicio,DataAccess.descuentoServicio,DataAccess.montoPagar);

        webview = (WebView) findViewById(R.id.webView);

        webview.loadDataWithBaseURL("",recibo.toUpperCase(),"text/html","UTF-8","");
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
