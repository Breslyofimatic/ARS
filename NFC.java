package com.ofimatic.ars;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.ofimatic.library.DialogHandler;

/**
 * Created by Administrator on 3/24/2014.
 */
public class NFC extends Activity {

    DialogHandler dialogo = new DialogHandler();

    protected final String TAG = "NfcDemo";
    public static String MIME_TEXT_PLAIN;
    public static byte[] MIMETYPE;
    public static long CardID;
    public static Tag tag;

    /**
     * Hace una verificacion de estado del dispositivo si tiene NFC y si esta desactivado.
     */
    public void VerificationNFC (final NfcAdapter mNfcAdapter, final Activity activity, Runnable okProcess, Runnable cancelProcess)
    {
        if (activity.getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

       if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC, This device doesn't support NFC
            Toast msj = Toast.makeText(activity, "Este dispositivo no soporta NFC.", Toast.LENGTH_LONG);
            msj.setGravity(Gravity.CENTER,0,0);
            msj.show();
        }

        if (!mNfcAdapter.isEnabled())
            dialogo.Confirm(activity, "NFC Desactivado", "�Desea activar NFC?", "No", "Si",
                    R.drawable.ic_launcher, okProcess, cancelProcess);
    }

    /**
     * Verifica si la Intencion es NDEF Discovered.
     */
    public Boolean handleIntent(Intent intent) throws NullPointerException{
        Boolean read = false;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                CardID = getDec(tag.getId());
                read = true;
            } else {
                Log.d(TAG, "Wrong mime type: " + type);
                Toast msj = Toast.makeText(this,"Mime erroneo",Toast.LENGTH_SHORT);
                msj.setGravity(Gravity.CENTER,0,0);
                msj.show();
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            // In case we would still use the Tech Discovered Intent
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    read = true;
                }
            }
        }
        return read;
    }

    /**
     * Convercion para obtener decimales.
     */
    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (byte aByte : bytes) {
            long value = aByte & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    /**
     * @param activity The corresponding requesting the foreground dispatch.
     * @param adapter The used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Context act = activity.getApplicationContext();
        if(act != null){
            final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

            IntentFilter[] filters = new IntentFilter[1];
            String[][] techList = new String[][]{

                    new String[]{

                            NfcA.class.getName(),
                            Ndef.class.getName(),
                            MifareClassic.class.getName()
                    }
            };

            // Notice that this is the same filter as in our manifest.
            filters[0] = new IntentFilter();
            filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filters[0].addCategory(Intent.CATEGORY_DEFAULT);
            try {
                filters[0].addDataType(MIME_TEXT_PLAIN);
            } catch (IntentFilter.MalformedMimeTypeException e) {
                throw new RuntimeException("Verificar mime type.");
            }
            adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
        }
    }

    /**
     * @param activity The corresponding requesting to stop the foreground dispatch.
     * @param adapter The used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
}
