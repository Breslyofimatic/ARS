package com.ofimatic.ars;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ofimatic.library.NFC;

public class MainActivity extends ActionBarActivity {

   NFC nfcClass = new NFC ();
   private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcClass.VerificationNFC(mNfcAdapter, MainActivity.this, okProcess(), cancelProcess());

        Boolean read = nfcClass.handleIntent(getIntent());

        if (read)
        {
            DataAccess.noPoliza =  NFC.Arreglo[0];
            DataAccess.noAfiliado = NFC.Arreglo[1];
            DataAccess.Afiliado = NFC.Arreglo[2];
            DataAccess.clienteCompany =  NFC.Arreglo[3];
            DataAccess.Plan =  NFC.Arreglo[4];
            DataAccess.FechaNac =  NFC.Arreglo[5];

            Intent in = new Intent(MainActivity.this, ReaderActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.putExtra("EXIT", true);
            startActivity(in);
        }
    }

    /**
     * Proceso para la confirmacion del mensaje.
     */
    public Runnable okProcess(){
        return new Runnable() {
            public void run() {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                Toast msj = Toast.makeText(MainActivity.this, "Despues de activar NFC regrese a la aplicación.", Toast.LENGTH_LONG);
                msj.show();
            }
        };
    }

    /**
     * Proceso para Cancelación del mensaje.
     */
    public Runnable cancelProcess(){
        return new Runnable() {
            public void run() {
                Toast msj = Toast.makeText(MainActivity.this, "Recuerde esta aplicación funciona correctamente con NFC.", Toast.LENGTH_SHORT);
                msj.setGravity(Gravity.CENTER,0,0);
                msj.show();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        NFC.setupForegroundDispatch(MainActivity.this, mNfcAdapter);

    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        NFC.stopForegroundDispatch(MainActivity.this, mNfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Boolean read = nfcClass.handleIntent(intent);

        if (read)
        {
            DataAccess.noPoliza =  NFC.Arreglo[0];
            DataAccess.noAfiliado = NFC.Arreglo[1];
            DataAccess.Afiliado = NFC.Arreglo[2];
            DataAccess.clienteCompany =  NFC.Arreglo[3];
            DataAccess.Plan =  NFC.Arreglo[4];
            DataAccess.FechaNac =  NFC.Arreglo[5];

            Intent in = new Intent(MainActivity.this, ReaderActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.putExtra("EXIT", true);
            startActivity(in);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
