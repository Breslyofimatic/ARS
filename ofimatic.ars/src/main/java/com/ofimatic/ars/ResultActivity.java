package com.ofimatic.ars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


public class ResultActivity extends ActionBarActivity {

    WebView webview;

    ReciboTemplate reciboTemplate = new ReciboTemplate();

    String recibo = reciboTemplate.recibo(DataAccess.Fecha,DataAccess.noAprobacion.toString(), DataAccess.noAfiliado,DataAccess.Afiliado, DataAccess.nombreServicio, DataAccess.nombreMedico, DataAccess.montoServicio,DataAccess.descuentoServicio,DataAccess.montoPagar);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        webview = (WebView) findViewById(R.id.webView);

        webview.loadDataWithBaseURL("",recibo.toUpperCase(),"text/html","UTF-8","");

    }

    public void TestPrint(View V) throws IOException {

        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath(), "test.html");
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(recibo);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent print = new Intent(Intent.ACTION_VIEW);
        print.setPackage("com.blackspruce.lpd");
        print.setDataAndType(Uri.fromFile(file), "text/html");
        startActivity(print);



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
