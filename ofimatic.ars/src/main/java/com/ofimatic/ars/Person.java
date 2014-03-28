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

public class Person {

    protected final String TAG_SUCCESS="success";
    protected final String TAG_PERSON = "person";
    protected final String TAG_DOCUMENTO= "NoDocumento";
    protected final String TAG_ID = "TagID";
    protected final String TAG_NOMBRES = "Nombres";
    protected final String TAG_APELLIDOS = "Apellidos";
    protected final String TAG_TELEFONO = "Telefono";
    protected final String TAG_DIRECCION = "Direccion";
    protected final String TAG_NACIONALIDAD = "Nacionalidad";
    protected final String TAG_TIPOSANGRE = "TipodeSangre";
    protected final String TAG_FECHAVENCIMIENTO = "FechaVencimiento";
    protected final String TAG_FECHASISTEMA = "FechaSistema";
    protected final String TAG_FOTO = "Foto";

    protected static String noDocumento;
    protected static long tagID;
    protected static String nombres;
    protected static String apellidos;
    protected static String telefono;
    protected static String direccion;
    protected static String nacionalidad;
    protected static String tipoSangre;
    protected static String fechaVencimiento;
    protected static String fechaSistema;
    protected static Bitmap foto;
    protected static String url = "http://ofimatic-mobile.com/PHPConnections/Migration/SelectPerson.php";
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
        params.add(new BasicNameValuePair("NoDocumento",this.noDocumento));

        // Chequea el log para el json response
        Log.d("Person Details", js.toString());

        JSONObject jsonObject = js.makeHttpResquest(this.url, "GET", params, context);

        int success=0;

        // json success tag
        try {
            success = jsonObject.getInt(TAG_SUCCESS);

        if (success == 1) {

           encontrado=true;
            // Obtencion del producto.
            JSONArray personObj = jsonObject
                    .getJSONArray(TAG_PERSON); // JSON Array

            // Obtencion del primer producto en el array
            JSONObject person = personObj.getJSONObject(0);

            noDocumento=person.getString(TAG_DOCUMENTO);
            tagID = person.getLong(TAG_ID);
            nombres=person.getString(TAG_NOMBRES);
            apellidos=person.getString(TAG_APELLIDOS);
            nacionalidad=person.getString(TAG_NACIONALIDAD);
            fechaVencimiento=person.getString(TAG_FECHAVENCIMIENTO);
            telefono=person.getString(TAG_TELEFONO);
            direccion=person.getString(TAG_DIRECCION);
            tipoSangre=person.getString(TAG_TIPOSANGRE);
            fechaSistema = person.getString(TAG_FECHASISTEMA);

            //Set Image from server.
            byte[] rawImage = Base64.decode(person.getString(TAG_FOTO), Base64.DEFAULT);
            foto = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
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
        params.add(new BasicNameValuePair("NoDocumento",this.noDocumento));
        //params.add(new BasicNameValuePair("tagID", this.tagID);
        params.add(new BasicNameValuePair("Nombres",this.nombres));
        params.add(new BasicNameValuePair("Apellidos",this.apellidos));
        params.add(new BasicNameValuePair("Telefono",this.telefono));
        params.add(new BasicNameValuePair("Direccion",this.direccion));
        params.add(new BasicNameValuePair("Nacionalidad",this.nacionalidad));
        params.add(new BasicNameValuePair("TipoSangre",this.tipoSangre));
        params.add(new BasicNameValuePair("FechaVencimiento",this.fechaVencimiento));
        params.add(new BasicNameValuePair("FechaSistema",this.fechaSistema));
        // params.add(new BasicNameValuePair("Foto",this.foto));
        return js.makeHttpResquest(this.url, "POST", params, context);
    }




}
