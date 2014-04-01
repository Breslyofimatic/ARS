package com.ofimatic.ars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.ofimatic.library.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataAccess {

    protected final String TAG_SUCCESS="success";
    protected final String TAG_AFILIADO = "Afiliado";
    protected final String TAG_NOAFILIADO = "NoAfiliado";
    protected final String TAG_ID = "TagID";
    protected final String TAG_NOPOLIZA = "NoPoliza";
    protected final String TAG_NOMBREAFILIADO = "NombreAfiliado";
    protected final String TAG_CLIENTE = "Cliente";
    protected final String TAG_ACTIVO = "Activo";
    protected final String TAG_PLAN = "Plan";
    protected final String TAG_NSS = "NSS";
    protected final String TAG_TIPOSANGRE = "TipoSangre";
    protected final String TAG_DIRECCION = "Direccion";
    protected final String TAG_TELEFONO = "Telefono";

    protected final String TAG_FECHANACIMIENTO = "FechaNacimiento";
    protected final String TAG_FECHAEMISION = "FechaEmision";
    protected final String TAG_FOTO = "Foto";


    protected static String noPoliza;
    protected static long tagID;
    protected static String Afiliado;
    protected static String noAfiliado;
    protected static String clienteCompany;
    protected static int Activo;
    protected static String Plan;
    protected static String NSS;
    protected static String FechaNac;
    protected static String tipoSangre;
    protected static String Direccion;
    protected static String Telefono;
    protected static String fechaEmision;
    protected static Bitmap foto;
    protected static String url = "http://ofimatic-mobile.com/PHPConnections/ARS/SelectAfiliado.php";
    protected static Context context;

    public static Boolean encontrado = false;

    JsonParser js= new JsonParser();

    /**
     * Obtiene la informacion del documento consultado.
     */
    public JSONObject getProfile(Context context)
    {
        this.context = context;
        // Pasando Parametros
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("NoAfiliado", noAfiliado));

        // Chequea el log para el json response
         Log.d("Data Details", js.toString());

        JSONObject jsonObject = js.makeHttpResquest(this.url, "GET", params, context);

        int success=0;

        // json success tag
        try {
            success = jsonObject.getInt(TAG_SUCCESS);

        if (success == 1) {

           encontrado=true;
            // Obtencion del producto.
            JSONArray afiliadoObj = jsonObject
                    .getJSONArray(TAG_AFILIADO); // JSON Array

            // Obtencion del primer producto en el array
            JSONObject jsonafiliado = afiliadoObj.getJSONObject(0);

            noAfiliado =jsonafiliado.getString(TAG_NOAFILIADO);
            tagID = jsonafiliado.getLong(TAG_ID);
            noPoliza =jsonafiliado.getString(TAG_NOPOLIZA);

            Afiliado =jsonafiliado.getString(TAG_NOMBREAFILIADO);
            clienteCompany =jsonafiliado.getString(TAG_CLIENTE);
            Activo = jsonafiliado.getInt(TAG_ACTIVO);
            Plan =jsonafiliado.getString(TAG_PLAN);
            NSS = jsonafiliado.getString(TAG_NSS);
            tipoSangre=jsonafiliado.getString(TAG_TIPOSANGRE);
            Direccion=jsonafiliado.getString(TAG_DIRECCION);
            Telefono = jsonafiliado.getString(TAG_TELEFONO);
            FechaNac =jsonafiliado.getString(TAG_FECHANACIMIENTO);
            fechaEmision = jsonafiliado.getString(TAG_FECHAEMISION);

            //Set Image from server.
            byte[] rawImage = Base64.decode(jsonafiliado.getString(TAG_FOTO), Base64.DEFAULT);
            foto = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);

//            //Set Image from server.
//            byte[] rawImage = Base64.decode(person.getString(TAG_FOTO), Base64.DEFAULT);
//            foto = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
        }
        else{
            //registro no encontrado
            encontrado=false;
        }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

    /**
     * Guarda la informacion de un documento.
     */
    public JSONObject setProfile(Context context)
    {
        this.context = context;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("NoDocumento",this.noPoliza));
        //params.add(new BasicNameValuePair("tagID", this.tagID);
        params.add(new BasicNameValuePair("NombreAfiliado",this.Afiliado));
        params.add(new BasicNameValuePair("NoAfiliado",this.noAfiliado));
        params.add(new BasicNameValuePair("Cliente",this.clienteCompany));
        params.add(new BasicNameValuePair("Plan",this.Plan));
        params.add(new BasicNameValuePair("FechaNacimiento",this.FechaNac));
        params.add(new BasicNameValuePair("TipoSangre",this.tipoSangre));
        params.add(new BasicNameValuePair("FechaEmision",this.fechaEmision));
        params.add(new BasicNameValuePair("FechaSistema",this.fechaEmision));
        // params.add(new BasicNameValuePair("Foto",this.foto));
        return js.makeHttpResquest(this.url, "POST", params, context);
    }




}
