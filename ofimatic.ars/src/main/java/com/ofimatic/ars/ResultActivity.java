package com.ofimatic.ars;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bixolon.printer.BixolonPrinter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Set;

import javax.xml.transform.Result;


public class ResultActivity extends ActionBarActivity {

    TextView  tvArs, tvfecha, tvaprobacion, tvafiliadono, tvnombre, tvservicio, tvmedico, tvmonto, tvdescuento, tvmontoapagar;
    TextView tvfechavalue,tvaprobacionvalue, tvafiliadonovalue, tvnombrevalue, tvserviciovalue, tvmedicovalue, tvmontovalue, tvdescuentovalue, tvmontoapagarvalue;
    public static String titulo, fecha , aprobacion, afiliado, nombre, servicio, medico, montoservicio, descuento, montototal;
    public static String titulovalue, fechavalue , aprobacionvalue, afiliadovalue, nombrevalue, serviciovalue, medicovalue, montoServ, Desc, montoTotal;
    static final int REQUEST_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE;
    static final int RESULT_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE - 1;
    static final String FIRMWARE_FILE_NAME = "FirmwareFileName";

    ReciboTemplate reciboTemplate = new ReciboTemplate();
    WebView webview;
    String recibo;
    String connectedDeviceName;
    Bitmap bitmap;

    static BixolonPrinter mBixolonPrinter;

    public static final String TAG = "BixolonPrinterSample";

    private void initUI()
    {
        setContentView(R.layout.activity_result);

         montoServ = priceToString(Double.parseDouble(DataAccess.montoServicio));
         Desc = priceToString(Double.parseDouble(DataAccess.descuentoServicio));
         montoTotal =  priceToString(Double.parseDouble(DataAccess.montoPagar));
        recibo = reciboTemplate.reciboPrintEjemplo(DataAccess.Fecha,DataAccess.noAprobacion.toString(), DataAccess.noAfiliado,DataAccess.Afiliado, DataAccess.nombreServicio, DataAccess.nombreMedico, montoServ, Desc, montoTotal);


//        tvArs = (TextView) findViewById(R.id.textViewARS);
//        tvfecha =(TextView) findViewById(R.id.textViewFECHA);
//        tvaprobacion = (TextView) findViewById(R.id.textViewAPROBACION);
//        tvafiliadono = (TextView) findViewById(R.id.textViewAFILIADO);
//        tvnombre = (TextView) findViewById(R.id.textViewNOMBRE);
//        tvservicio = (TextView) findViewById(R.id.textViewSERVICIO);
//        tvmedico = (TextView) findViewById(R.id.textViewMEDICO);
//        tvmonto = (TextView) findViewById(R.id.textViewMONTO);
//        tvdescuento = (TextView) findViewById(R.id.textViewDESCUENTO);
//        tvmontoapagar = (TextView) findViewById(R.id.textViewMONTOAPAGAR);
//
//
//        tvfechavalue =(TextView) findViewById(R.id.textViewFECHAVALUE);
//        tvaprobacionvalue = (TextView) findViewById(R.id.textViewAPROBACIONVALUE);
//        tvafiliadonovalue = (TextView) findViewById(R.id.textViewAFILIADOVALUE);
//        tvnombrevalue = (TextView) findViewById(R.id.textViewNOMBREVALUE);
//        tvserviciovalue = (TextView) findViewById(R.id.textViewSERVICIOVALUE);
//        tvmedicovalue = (TextView) findViewById(R.id.textViewMEDICOVALUE);
//        tvmontovalue = (TextView) findViewById(R.id.textViewMONTOVALUE);
//        tvdescuentovalue = (TextView) findViewById(R.id.textViewDESCUENTOVALUE);
//        tvmontoapagarvalue = (TextView) findViewById(R.id.textViewMONTOAPAGARVALUE);

        webview = (WebView) findViewById(R.id.webView);
        webview.loadDataWithBaseURL("",recibo.toUpperCase(),"text/html","UTF-8","");

        mBixolonPrinter = new BixolonPrinter(this,mHandlerB, null);


        }

    @TargetApi(11)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB || Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB_MR1) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBixolonPrinter.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_FIRMWARE && resultCode == RESULT_CODE_SELECT_FIRMWARE) {
            final String binaryFilePath = data.getStringExtra(FIRMWARE_FILE_NAME);
            mHandlerB.obtainMessage(MESSAGE_START_WORK).sendToTarget();
            new Thread(new Runnable() {

                public void run() {
                    mBixolonPrinter.updateFirmware(binaryFilePath);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mHandlerB.obtainMessage(MESSAGE_END_WORK).sendToTarget();
                }
            }).start();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public static String priceToString(Double price) {
        return new DecimalFormat("###,###.00").format(price);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initUI();
    }

    public void TestPrint(View V) throws IOException {

       //findBluetooth
        try {
            //PrintTemplate();
            mBixolonPrinter.findBluetoothPrinters();
           }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//        File root = android.os.Environment.getExternalStorageDirectory();
//        File file = new File(root.getAbsolutePath(), "test.html");
//        try {
//            FileOutputStream f = new FileOutputStream(file);
//            PrintWriter pw = new PrintWriter(f);
//            pw.println(recibo);
//            pw.flush();
//            pw.close();
//            f.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Intent print = new Intent(Intent.ACTION_VIEW);
//        print.setPackage("com.blackspruce.lpd");
//        print.setDataAndType(Uri.fromFile(file), "text/html");
//        startActivity(print);

    }


public void PrintTemplate()
{
    titulo = Html.fromHtml("<h2>"+tvArs.getText().toString()+"</h2>").toString();
    fecha = Html.fromHtml("<p>"+tvfecha.getText().toString()+"</p>").toString();
    aprobacion = Html.fromHtml("<p>"+tvaprobacion.getText().toString()+"</p>").toString();
    afiliado = Html.fromHtml("<p>"+tvafiliadono.getText().toString()+"</p>").toString();
    nombre = Html.fromHtml("<p>"+tvnombre.getText().toString()+"</p>").toString();
    servicio = Html.fromHtml("<p>"+tvservicio.getText().toString()+"</p>").toString();
    medico = Html.fromHtml("<p>"+tvmedico.getText().toString()+"</p>").toString();
    montoservicio = Html.fromHtml("<b>"+tvmonto.getText().toString()+"</b>").toString();
    descuento = Html.fromHtml("<b>"+tvdescuento.getText().toString()+"</b>").toString();
    montototal = Html.fromHtml("<b>"+tvmontoapagar.getText().toString()+"</b>").toString();

    tvfechavalue.setText(Html.fromHtml("<p>"+DataAccess.Fecha+"</p>"));
    tvaprobacionvalue.setText(Html.fromHtml("<p>"+DataAccess.noAprobacion+"</p>"));
    tvafiliadonovalue.setText(Html.fromHtml("<p>"+DataAccess.noAfiliado+"</p>"));
    tvnombrevalue.setText(Html.fromHtml("<p>"+DataAccess.Afiliado+"</p>"));
    tvserviciovalue.setText(Html.fromHtml("<p>"+DataAccess.nombreServicio+"</p>"));
    tvmedicovalue.setText(Html.fromHtml("<p>"+DataAccess.nombreMedico+"</p>"));
    tvmontovalue.setText(Html.fromHtml("<p>"+montoServ+"</p>"));
    tvdescuentovalue.setText(Html.fromHtml("<p>"+Desc+"</p>"));
    tvmontoapagarvalue.setText(Html.fromHtml("<p>"+montoTotal+"</p>"));


    fechavalue = tvfechavalue.getText().toString();
    aprobacionvalue = tvaprobacionvalue.getText().toString();
    afiliadovalue = tvafiliadonovalue.getText().toString();
    nombrevalue = tvnombrevalue.getText().toString();
    serviciovalue = tvserviciovalue.getText().toString();
    medicovalue = tvmedicovalue.getText().toString();
    montoServ = tvmontovalue.getText().toString();
    Desc = tvdescuentovalue.getText().toString();
    montoTotal = tvmontoapagarvalue.getText().toString();

//
//    //tvfecha.setText(Html.fromHtml("<p>FECHA:</p>"));
//    fecha = Html.fromHtml("<p>"+tvfecha.getText().toString()+"</p>").toString();
//    tvfechavalue.setText("<p>"+DataAccess.Fecha+"</p>");
//
//    //tvaprobacion.setText(Html.fromHtml("\n"+"<p><b>APROBACION N0.:</b></p>" +"<p>"+DataAccess.noAprobacion.toString()+"</p>"));
//
//    aprobacion = Html.fromHtml("<p>"+tvaprobacion.getText().toString()+"</p>").toString(); //tvaprobacion.getText().toString();
//    tvaprobacionvalue.setText("<p>"+DataAccess.noAprobacion+"</p>");
//
//    //tvafiliadono.setText(Html.fromHtml("\n"+"<p><b>AFILIADO N0.: </b>"+"\n"+DataAccess.noAfiliado+"</p>"));
//
//    tvnombre.setText(Html.fromHtml("\n"+"<p><b>N0MBRE:</b></p> "+"\n"+"<p>" +DataAccess.Afiliado+"</p>"));
//    tvservicio.setText(Html.fromHtml("\n"+"<p><b>SERVICIO:</b></p> "+"\n"+"<p>"+DataAccess.nombreServicio+"</p>"));
//    tvmedico.setText(Html.fromHtml("\n"+"<p><b>MEDICO:</b></p> "+"\n"+"<p>"+DataAccess.nombreMedico+"</p>"));
//        tvmonto.setText(Html.fromHtml("\n"+"<p>MONTO:             "+montoServ+"</p>"));
//    tvdescuento.setText(Html.fromHtml("\n"+"<p>DESCUENTO:         "+Desc+"</p>"));
//  tvmontoapagar.setText(Html.fromHtml("\n"+"<p><b>MONTO A PAGAR:  "+montoTotal+"</b></p>"));
//
//    fechavalue = tvfechavalue.getText().toString();
//    afiliado = tvafiliadono.getText().toString();
//    nombre = tvnombre.getText().toString();
//    servicio = tvservicio.getText().toString();
//    medico = tvmedico.getText().toString();



    int sizetitulo = ResultActivity.mBixolonPrinter.TEXT_SIZE_VERTICAL2 | ResultActivity.mBixolonPrinter.TEXT_SIZE_HORIZONTAL2;
    int size = ResultActivity.mBixolonPrinter.TEXT_SIZE_VERTICAL1 | ResultActivity.mBixolonPrinter.TEXT_SIZE_HORIZONTAL1;
    int aligCENTER = ResultActivity.mBixolonPrinter.ALIGNMENT_CENTER;
    int aligRIGHT = ResultActivity.mBixolonPrinter.ALIGNMENT_RIGHT;
    int aligLEFT = ResultActivity.mBixolonPrinter.ALIGNMENT_LEFT;
    int atributobold = ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A | ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED;
    int atributoUnderline = ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A | ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_UNDERLINE2;
    int atributo = ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A;

    //titulo del recibo
    ResultActivity.mBixolonPrinter.printText(ResultActivity.titulo, aligCENTER, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, sizetitulo,false  );
    //Fecha
    //tvfecha.getText().toString()
    ResultActivity.mBixolonPrinter.printText(fecha, aligRIGHT, atributobold, size , false);
    ResultActivity.mBixolonPrinter.printText(fechavalue, aligRIGHT, atributo, size , false);

    //Cuerpo del recibo
    //(tvaprobacion.getText().toString() +"     " +titulo
    ResultActivity.mBixolonPrinter.printText(aprobacion, aligLEFT, atributobold, size, false);
    ResultActivity.mBixolonPrinter.printText((aprobacionvalue), aligLEFT,  atributo, size, false);

    ResultActivity.mBixolonPrinter.printText(afiliado, aligLEFT, atributobold, size, false);
    ResultActivity.mBixolonPrinter.printText(afiliadovalue, aligLEFT, atributo, size, false);

    ResultActivity.mBixolonPrinter.printText(nombre, aligLEFT, atributobold, size, false);
    ResultActivity.mBixolonPrinter.printText(nombrevalue, aligLEFT, atributo, size, false);

    ResultActivity.mBixolonPrinter.printText(servicio, aligLEFT, atributobold, size, false);
    ResultActivity.mBixolonPrinter.printText(serviciovalue, aligLEFT, atributo, size, false);

    ResultActivity.mBixolonPrinter.printText(medico, aligLEFT, atributobold, size, false);
    ResultActivity.mBixolonPrinter.printText(medicovalue, aligLEFT, atributo, size, false);

    ResultActivity.mBixolonPrinter.printText(montoservicio, aligLEFT, atributobold, size, false);
    ResultActivity.mBixolonPrinter.printText("   "+montoServ, aligRIGHT, atributo, size, false);

    ResultActivity.mBixolonPrinter.printText(descuento, aligLEFT, atributobold, size, false);
    ResultActivity.mBixolonPrinter.printText("   "+Desc, aligRIGHT, atributoUnderline, size, false);

    ResultActivity.mBixolonPrinter.printText(montototal, aligLEFT, atributobold, size, false);
    ResultActivity.mBixolonPrinter.printText("   "+montoTotal, aligRIGHT, atributo, size, false);









//    ResultActivity.mBixolonPrinter.printText(tvservicio.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
//    ResultActivity.mBixolonPrinter.printText(tvmedico.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
//    ResultActivity.mBixolonPrinter.printText(tvmonto.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
//    ResultActivity.mBixolonPrinter.printText(tvdescuento.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
//    ResultActivity.mBixolonPrinter.printText(tvmontoapagar.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);


    ResultActivity.mBixolonPrinter.lineFeed(3, false);
//    MainActivity.mBixolonPrinter.formFeed(true);
    ResultActivity.mBixolonPrinter.cutPaper(true);
    ResultActivity.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);


    /*
    *
    ResultActivity.mBixolonPrinter.printText(tvafiliadono.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
    ResultActivity.mBixolonPrinter.printText(tvnombre.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
    ResultActivity.mBixolonPrinter.printText(tvservicio.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
    ResultActivity.mBixolonPrinter.printText(tvmedico.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
    ResultActivity.mBixolonPrinter.printText(tvmonto.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
    ResultActivity.mBixolonPrinter.printText(tvdescuento.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);
    ResultActivity.mBixolonPrinter.printText(tvmontoapagar.getText().toString(), aligLEFT, ResultActivity.mBixolonPrinter.TEXT_ATTRIBUTE_FONT_A, size, false);

    * */
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


    @TargetApi(11)
    private final void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }
    @TargetApi(11)
    private final void setStatus(CharSequence subtitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subtitle);
    }

    static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
    static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;

    private void dispatchMessage(Message msg) {
        switch (msg.arg1) {
            case BixolonPrinter.PROCESS_GET_STATUS:
                if (msg.arg2 == BixolonPrinter.STATUS_NORMAL) {
                    Toast.makeText(getApplicationContext(), "No error", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuffer buffer = new StringBuffer();
                    if ((msg.arg2 & BixolonPrinter.STATUS_COVER_OPEN) == BixolonPrinter.STATUS_COVER_OPEN) {
                        buffer.append("Cover is open.\n");
                    }
                    if ((msg.arg2 & BixolonPrinter.STATUS_PAPER_NOT_PRESENT) == BixolonPrinter.STATUS_PAPER_NOT_PRESENT) {
                        buffer.append("Paper end sensor: paper not present.\n");
                    }

                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                }
                break;

            case BixolonPrinter.PROCESS_GET_TPH_THEMISTOR_STATUS:
                if (msg.arg2 == BixolonPrinter.STATUS_TPH_OVER_HEATING) {
                    Toast.makeText(getApplicationContext(), "The status of TPH thermistor is overheating.", Toast.LENGTH_SHORT).show();
                }
                break;

            case BixolonPrinter.PROCESS_GET_POWER_MODE:
                if (msg.arg2 == BixolonPrinter.STATUS_SMPS_MODE) {
                    Toast.makeText(getApplicationContext(), "SMPS mode", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Battery mode", Toast.LENGTH_SHORT).show();
                }
                break;

            case BixolonPrinter.PROCESS_GET_BATTERY_VOLTAGE_STATUS:
                if (msg.arg2 == BixolonPrinter.STATUS_BATTERY_LOW_VOLTAGE) {
                    Toast.makeText(getApplicationContext(), "Low voltage", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Normal voltage", Toast.LENGTH_SHORT).show();
                }
                break;

            case BixolonPrinter.PROCESS_GET_RECEIVE_BUFFER_DATA_SIZE:
                Toast.makeText(getApplicationContext(), "Size of data on receive buffer: " + msg.arg2 + " bytes", Toast.LENGTH_SHORT).show();
                break;

            case BixolonPrinter.PROCESS_GET_PRINTER_ID:
                Bundle data = msg.getData();
                Toast.makeText(getApplicationContext(), data.getString(BixolonPrinter.KEY_STRING_PRINTER_ID), Toast.LENGTH_SHORT).show();
                break;

            case BixolonPrinter.PROCESS_GET_BS_CODE_PAGE:
                data = msg.getData();
                Toast.makeText(getApplicationContext(), data.getString(BixolonPrinter.KEY_STRING_CODE_PAGE), Toast.LENGTH_SHORT).show();
                break;

            case BixolonPrinter.PROCESS_GET_PRINT_SPEED:
                switch (msg.arg2) {
                    case BixolonPrinter.PRINT_SPEED_LOW:
                        Toast.makeText(getApplicationContext(), "Print speed: low", Toast.LENGTH_SHORT).show();
                        break;
                    case BixolonPrinter.PRINT_SPEED_MEDIUM:
                        Toast.makeText(getApplicationContext(), "Print speed: medium", Toast.LENGTH_SHORT).show();
                        break;
                    case BixolonPrinter.PRINT_SPEED_HIGH:
                        Toast.makeText(getApplicationContext(), "Print speed: high", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case BixolonPrinter.PROCESS_GET_PRINT_DENSITY:
                switch (msg.arg2) {
                    case BixolonPrinter.PRINT_DENSITY_LIGHT:
                        Toast.makeText(getApplicationContext(), "Print density: light", Toast.LENGTH_SHORT).show();
                        break;
                    case BixolonPrinter.PRINT_DENSITY_DEFAULT:
                        Toast.makeText(getApplicationContext(), "Print density: default", Toast.LENGTH_SHORT).show();
                        break;
                    case BixolonPrinter.PRINT_DENSITY_DARK:
                        Toast.makeText(getApplicationContext(), "Print density: dark", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case BixolonPrinter.PROCESS_GET_POWER_SAVING_MODE:
                String text = "Power saving mode: ";
                if (msg.arg2 == 0) {
                    text += false;
                } else {
                    text += true + "\n(Power saving time: " + msg.arg2 + ")";
                }
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                break;

            case BixolonPrinter.PROCESS_AUTO_STATUS_BACK:
                StringBuffer buffer = new StringBuffer(0);
                if ((msg.arg2 & BixolonPrinter.AUTO_STATUS_COVER_OPEN) == BixolonPrinter.AUTO_STATUS_COVER_OPEN) {
                    buffer.append("Cover is open.\n");
                }
                if ((msg.arg2 & BixolonPrinter.AUTO_STATUS_NO_PAPER) == BixolonPrinter.AUTO_STATUS_NO_PAPER) {
                    buffer.append("Paper end sensor: no paper present.\n");
                }

                if (buffer.capacity() > 0) {
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No error.", Toast.LENGTH_SHORT).show();
                }
                break;

//            case BixolonPrinter.PROCESS_GET_NV_IMAGE_KEY_CODES:
//                data = msg.getData();
//                int[] value = data.getIntArray(BixolonPrinter.NV_IMAGE_KEY_CODES);
//
//                Intent intent = new Intent();
//                intent.setAction(ACTION_GET_DEFINEED_NV_IMAGE_KEY_CODES);
//                intent.putExtra(EXTRA_NAME_NV_KEY_CODES, value);
//                sendBroadcast(intent);
//                break;

            case BixolonPrinter.PROCESS_EXECUTE_DIRECT_IO:
                buffer = new StringBuffer();
                data = msg.getData();
                byte[] response = data.getByteArray(BixolonPrinter.KEY_STRING_DIRECT_IO);
                for (int i = 0; i < response.length && response[i] != 0; i++) {
                    buffer.append(Integer.toHexString(response[i]) + " ");
                }

                Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                break;

//            case BixolonPrinter.PROCESS_MSR_TRACK:
//                intent = new Intent();
//                intent.setAction(ACTION_GET_MSR_TRACK_DATA);
//                intent.putExtra(EXTRA_NAME_MSR_TRACK_DATA, msg.getData());
//                sendBroadcast(intent);
//                break;
//
//            case BixolonPrinter.PROCESS_GET_MSR_MODE:
//                intent = new Intent(MainActivity.this, MsrActivity.class);
//                intent.putExtra(EXTRA_NAME_MSR_MODE, msg.arg2);
//                startActivity(intent);
//                break;
        }

    };


    //Por bluethooth Dinamico
    private final Handler mHandlerB = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "Dispositivo no apariado", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    Set<BluetoothDevice> bluetoothDeviceSet =
                            (Set<BluetoothDevice>) msg.obj;
                    for (BluetoothDevice device : bluetoothDeviceSet) {
                        if (device.getName().equals("SPP-R300")) {
                            // TODO: Connect printer
                            mBixolonPrinter.connect(device.getAddress());
                            break;
                        }
                    }
                    }
                    break;

                case BixolonPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BixolonPrinter.STATE_CONNECTING:
// TODO: Processing when connection to printer is being tried
                            setStatus(R.string.title_connecting);
                            break;
                        case BixolonPrinter.STATE_CONNECTED:
// TODO: Processing when printer connection is completed
                            setStatus(getString(R.string.title_connected_to, connectedDeviceName));

//                            Bitmap bitmap = Bitmap.createBitmap(webview.getWidth(), webview.getHeight(),
//                                    Bitmap.Config.ARGB_8888);
//                            Canvas canvas = new Canvas(bitmap);
//                            webview.draw(canvas);
//                            MediaStore.Images.Media.insertImage(
//                                    getContentResolver(), bitmap,
//                                    "image" + ".png", "drawing");
                            bitmap = Bitmap.createBitmap(webview.getWidth(), webview.getHeight(),
                                    Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            webview.draw(canvas);
                           // MediaStore.Images.Media.insertImage( getContentResolver(), bitmap,"image" + ".png", "drawing");

                            mBixolonPrinter.printBitmap(bitmap,1,450, 70, false);
                            ResultActivity.mBixolonPrinter.lineFeed(3, false);
//    MainActivity.mBixolonPrinter.formFeed(true);
                            ResultActivity.mBixolonPrinter.cutPaper(true);
                            ResultActivity.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);

                            webview.destroyDrawingCache();
                           // PrintTemplate();
                          // mBixolonPrinter.disconnect();
                            break;
                       case BixolonPrinter.STATE_NONE:
// TODO: Processing when printer is not connected
                           setStatus("");
                           // Toast.makeText(getApplicationContext(), "Impresora desconectada",
                                 //   Toast.LENGTH_SHORT).show();
                           // mBixolonPrinter.disconnect();
                            break;
                    }
                    break;

                case BixolonPrinter.MESSAGE_READ:
                            dispatchMessage(msg);
                    return true;


                case BixolonPrinter.MESSAGE_DEVICE_NAME:
                    connectedDeviceName =
                            msg.getData().getString(
                                    BixolonPrinter.KEY_STRING_DEVICE_NAME);
                    break;
                case BixolonPrinter.MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(BixolonPrinter.KEY_STRING_TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;


                case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
                    Toast.makeText(getApplicationContext(), "Impresion Completada", Toast.LENGTH_SHORT).show();
                    mBixolonPrinter.disconnect();
                    break;
            }
            return true;
        }
    });

}