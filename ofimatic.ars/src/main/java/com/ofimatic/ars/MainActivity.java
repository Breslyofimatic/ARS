package com.ofimatic.ars;

import android.content.Intent;
import android.content.res.Configuration;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ofimatic.library.NFC;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MainActivity extends ActionBarActivity {

   NFC nfcClass = new NFC ();
   protected final String TAG = "NfcDemo";
   private NfcAdapter mNfcAdapter;

    private void initUI(){

        setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NFC.MIME_TEXT_PLAIN = "application/com.ofimatic.ars";
        NFC.MIMETYPE = "application/com.ofimatic.ars".getBytes();

        nfcClass.VerificationNFC(mNfcAdapter, this, okProcess(), cancelProcess());

        Boolean read = nfcClass.handleIntent(getIntent());

        if (read)
        {
            new NdefReaderTask().execute(NFC.tag);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initUI();

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
            new NdefReaderTask().execute(NFC.tag);
        }
    }

    /**
     * Proceso para obtener los datos de la tarjeta.
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected void onPreExecute(){
            Log.d("Pre-Execute", "en pre-execute");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            if(ndefMessage!=null){
                NdefRecord[] records = ndefMessage.getRecords();
                for (NdefRecord ndefRecord : records) {

                    if (ndefRecord.getTnf() == NdefRecord.TNF_MIME_MEDIA && Arrays.equals(ndefRecord.getType(), NFC.MIMETYPE)) {
                        try {
                            return readText(ndefRecord);
                        } catch (UnsupportedEncodingException e) {
                            Log.e(TAG, "Unsupported Encoding", e);
                        }
                    }
                }
            }
            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            byte[] payload = record.getPayload();
            return new String(payload);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try{
                    String [] Arreglo = result.split(";");

                    DataAccess.tagID = Long.parseLong(Arreglo[0]);
                    DataAccess.noPoliza =  Arreglo[1];
                    DataAccess.noAfiliado = Arreglo[2];
                    DataAccess.Afiliado = Arreglo[3];
                    DataAccess.clienteCompany = Arreglo[4];
                    DataAccess.Plan = Arreglo[5];
                    DataAccess.FechaNac = Arreglo[6];

                    Intent in = new Intent(MainActivity.this, ReaderActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.putExtra("EXIT", true);
                    startActivity(in);
                }
                catch (Exception e)
                {
                    Toast msj = Toast.makeText(MainActivity.this,"Error Data Incorrecta",Toast.LENGTH_SHORT);
                    msj.setGravity(Gravity.CENTER,0,0);
                    msj.show();
                }
            }
            else
            {
                Toast msj = Toast.makeText(MainActivity.this,"Error Data Incorrecta",Toast.LENGTH_SHORT);
                msj.setGravity(Gravity.CENTER,0,0);
                msj.show();
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
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
