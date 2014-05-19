package com.ofimatic.ars;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.bluetooth.BluetoothDevice;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
import com.bixolon.printer.BixolonPrinter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Set;

public class ResultActivity extends ActionBarActivity {


    public static String  afiliado;
    public static String  montoServ, Desc, montoTotal;
    ReciboTemplate reciboTemplate = new ReciboTemplate();
    WebView webview;
    String recibo;
    String connectedDeviceName;
    Bitmap bitmap;
    static BixolonPrinter mBixolonPrinter;

    private void initUI()
    {
        setContentView(R.layout.activity_result);

         montoServ = priceToString(Double.parseDouble(DataAccess.montoServicio));
         Desc = priceToString(Double.parseDouble(DataAccess.descuentoServicio));
         montoTotal =  priceToString(Double.parseDouble(DataAccess.montoPagar));
         recibo = reciboTemplate.reciboPrint(DataAccess.Fecha,DataAccess.noAprobacion.toString(), DataAccess.noAfiliado,DataAccess.Afiliado, DataAccess.nombreServicio, DataAccess.nombreMedico, montoServ, Desc, montoTotal);
        //Instancia del webview
        webview = (WebView) findViewById(R.id.webView);
        webview.loadDataWithBaseURL("",recibo.toUpperCase(),"text/html","UTF-8","");
        //Instancia de la Impresora.
        mBixolonPrinter = new BixolonPrinter(this,mHandlerB, null);
        }

    @TargetApi(11)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Para version de HoneyComb hace estas configuraciones para la impresora
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
        //Desconecta la impresora
        mBixolonPrinter.disconnect();
    }

    public static String priceToString(Double price) {
        //formato para el valor del monto
        return new DecimalFormat("###,###.00").format(price);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initUI();
    }

    public void TestPrint(View V) throws IOException {

       //Busca la impresora por bluetooth
        try {
            mBixolonPrinter.findBluetoothPrinters();
           }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @TargetApi(11)
    private final void setStatus(int resId) {
        //Status --> este es el estado que sale debajo del titulo.
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }
    @TargetApi(11)
    private final void setStatus(CharSequence subtitle) {
        //Status --> este es el estado que sale debajo del titulo.
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subtitle);
    }

    private void dispatchMessage(Message msg) {
        //Errores de la impresora
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

            case BixolonPrinter.PROCESS_EXECUTE_DIRECT_IO:
                buffer = new StringBuffer();
                data = msg.getData();
                byte[] response = data.getByteArray(BixolonPrinter.KEY_STRING_DIRECT_IO);
                for (int i = 0; i < response.length && response[i] != 0; i++) {
                    buffer.append(Integer.toHexString(response[i]) + " ");
                }
                Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                break;
        }

    };

    //TODO: Por Busqueda por bluethooth y conexión
    private final Handler mHandlerB = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    //TODO:Validación si el dispositivo esta apariado con la impresora
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "Dispositivo no apariado con la impresora", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    Set<BluetoothDevice> bluetoothDeviceSet =
                            (Set<BluetoothDevice>) msg.obj;
                    for (BluetoothDevice device : bluetoothDeviceSet) {
                        if (device.getName().equals("SPP-R300")) {
                            // TODO: Conectado con la impresora
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

                            bitmap = Bitmap.createBitmap(webview.getWidth(), webview.getHeight(),
                                    Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            webview.draw(canvas);
                           // MediaStore.Images.Media.insertImage( getContentResolver(), bitmap,"image" + ".png", "drawing");

                            mBixolonPrinter.printBitmap(bitmap,1,450, 70, false);
                            ResultActivity.mBixolonPrinter.lineFeed(3, false);
                            ResultActivity.mBixolonPrinter.cutPaper(true);
                            ResultActivity.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);

                            webview.destroyDrawingCache();

                            break;
                       case BixolonPrinter.STATE_NONE:
                // TODO: Processing when printer is not connected
                           setStatus("");
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