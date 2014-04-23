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
import java.math.BigInteger;
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

    protected final String TAG_MEDICO = "Medico";
    protected final String TAG_IDMEDICO = "IDMedico";
    protected final String TAG_NOMBREMEDICO= "NombreMedico";

    protected final String TAG_SERVICIO = "Servicio";
    protected final String TAG_IDSERVICIO = "IDServicio";
    protected final String TAG_NOMBRESERVICIO= "NombreServicio";
    protected final String TAG_DESCUENTOSERVICIO = "DescuentoServicio";

    protected final String TAG_AUTORIZACION = "autorizacion";
    protected final String TAG_NOAPROBACION = "NoAprobacion";
    protected final String TAG_MONTOSERVICIO = "MontoServicio";
    protected final String TAG_DESCUENTO = "Descuento";
    protected final String TAG_MONTOPAGAR= "MontoPagar";
    protected final String TAG_Fecha= "Fecha";

    protected static String noPoliza;
    protected static BigInteger tagID;
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

    protected static String idServicio;
    protected static String nombreServicio;
    protected static String idMedico;
    protected static String nombreMedico;
   // protected static String monto;

    protected static Integer noAprobacion;
    protected static String montoServicio;
    protected static String descuentoServicio;
    protected static String montoPagar;
    protected static String Fecha;

    protected static String urlSelectAfiliado = "http://ofimatic-mobile.com/PHPConnections/ARS/SelectAfiliado.php";
    protected static String urlAllMedic = "http://ofimatic-mobile.com/PHPConnections/ARS/SelectAllMedico.php";
    protected static String urlAllServices = "http://ofimatic-mobile.com/PHPConnections/ARS/SelectAllServices.php";
    protected  static String urlspAuthorize = "http://ofimatic-mobile.com/PHPConnections/ARS/CallspAutorization.php";

    protected static Context context;

    public static Boolean encontrado = false;

    JsonParser js= new JsonParser();
    public static String[] servicesName;
    public static String[] medicNames;
    public  static JSONArray servicesArray;
    public static JSONArray medicoArray;
    public static String descuento;


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

        JSONObject jsonObject = js.makeHttpResquest(urlSelectAfiliado, "GET", params, context);

        int success;

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
            tagID = new BigInteger(jsonafiliado.getString(TAG_ID));
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
     * Obtiene la informacion del documento consultado.
     */
    public JSONObject getPayAuthorize(Context context)
    {
        this.context = context;
        // Pasando Parametros
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("NoAfiliado", noAfiliado));
        params.add(new BasicNameValuePair("IDServicio" , idServicio));
        params.add(new BasicNameValuePair("IDMedico", idMedico));
        params.add(new BasicNameValuePair("MontoServicio", montoServicio));

        // Chequea el log para el json response
        Log.d("Data Details", js.toString());

        JSONObject jsonObject = js.makeHttpResquest(urlspAuthorize, "GET", params, context);

        int success=0;

        // json success tag
        try {
            success = jsonObject.getInt(TAG_SUCCESS);

            if (success == 1) {

                encontrado=true;
                // Obtencion del producto.
                JSONArray payObj = jsonObject
                        .getJSONArray(TAG_AUTORIZACION); // JSON Array

                // Obtencion del primer producto en el array
                JSONObject jsonPayAuto = payObj.getJSONObject(0);

                noAprobacion = jsonPayAuto.getInt(TAG_NOAPROBACION);
                Afiliado =jsonPayAuto.getString(TAG_NOMBREAFILIADO);
                nombreServicio =jsonPayAuto.getString(TAG_NOMBRESERVICIO);
                nombreMedico =jsonPayAuto.getString(TAG_NOMBREMEDICO);

                montoServicio = jsonPayAuto.getString(TAG_MONTOSERVICIO);
                descuentoServicio =  jsonPayAuto.getString(TAG_DESCUENTO);
                montoPagar = jsonPayAuto.getString(TAG_MONTOPAGAR);
                Fecha = jsonPayAuto.getString(TAG_Fecha);

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
     * Obtiene la informacion del documento consultado.
     */
    public JSONObject getAllMedic(Context context)
    {
        this.context = context;
        // Pasando Parametros
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("NoAfiliado", ""));

        // Chequea el log para el json response
        Log.d("Data Details", js.toString());

        JSONObject jsonObject = js.makeHttpResquest(urlAllMedic, "GET", params, context);

        int success;

        // json success tag
        try {
            success = jsonObject.getInt(TAG_SUCCESS);

            if (success == 1) {

                encontrado=true;
                // Obtencion del producto.
                medicoArray = jsonObject.getJSONArray(TAG_MEDICO); // JSON Array

                /*final String[]*/
                medicNames = new String[medicoArray.length()];

                // looping through All Contacts
                for(int i = 0; i < medicoArray.length(); i++){

                    JSONObject c = medicoArray.getJSONObject(i);
                    // Storing each json item in variable
                    medicNames[i]=c.getString(TAG_NOMBREMEDICO);
                    //System.out.println("Hello events "+items);
                }

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


    public String findServicesID(int index)
    {
        String id;

        try{
            JSONObject c = servicesArray.getJSONObject(index);
            id = c.getString(TAG_IDSERVICIO);
            descuento = c.getString(TAG_DESCUENTOSERVICIO);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return  id;
    }

    public String [] ListMedics(String serviceID)
    {
        List<String> names = new ArrayList<String>();

        //String names[] = new String[medicoArray.length()];
        try{
            for(int i = 0; i < medicoArray.length(); i++){

                JSONObject c = medicoArray.getJSONObject(i);

                if (c.getString(TAG_IDSERVICIO).equals(serviceID)) {
                    names.add(c.getString(TAG_IDMEDICO) + ";" + c.getString(TAG_NOMBREMEDICO));
                }
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return  names.toArray(new String[names.size()]);
    }

   /* public String findMedicID(int index)
    {
        String id;
        try{
            JSONObject c = medicoArray.getJSONObject(index);
            id = c.getString(TAG_IDMEDICO);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return  id;
    }*/

    /**
     * Obtiene la informacion del documento consultado.
     */
    public JSONObject getAllServices(Context context)
    {
        this.context = context;
        // Pasando Parametros
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("NoAfiliado", ""));

        // Chequea el log para el json response
        Log.d("Data Details", js.toString());

        JSONObject jsonObject = js.makeHttpResquest(urlAllServices, "GET", params, context);

        int success=0;

        // json success tag
        try {
            success = jsonObject.getInt(TAG_SUCCESS);

            if (success == 1) {

                encontrado=true;
                // Obtencion del producto.
                servicesArray = jsonObject.getJSONArray(TAG_SERVICIO); // JSON Array

                /*final String[]*/
                servicesName = new String[servicesArray.length()];

                // looping through All Contacts
                for(int i = 0; i < servicesArray.length(); i++){

                    JSONObject c = servicesArray.getJSONObject(i);
                    // Storing each json item in variable

                    servicesName[i]=c.getString(TAG_NOMBRESERVICIO);
                    //System.out.println("Hello events "+items);
                }

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
        params.add(new BasicNameValuePair("NoDocumento",noPoliza));
        //params.add(new BasicNameValuePair("tagID", this.tagID);
        params.add(new BasicNameValuePair("NombreAfiliado",Afiliado));
        params.add(new BasicNameValuePair("NoAfiliado",noAfiliado));
        params.add(new BasicNameValuePair("Cliente",clienteCompany));
        params.add(new BasicNameValuePair("Plan",Plan));
        params.add(new BasicNameValuePair("FechaNacimiento",FechaNac));
        params.add(new BasicNameValuePair("TipoSangre",tipoSangre));
        params.add(new BasicNameValuePair("FechaEmision",fechaEmision));
        params.add(new BasicNameValuePair("FechaSistema",fechaEmision));
        // params.add(new BasicNameValuePair("Foto",this.foto));
        return js.makeHttpResquest(urlSelectAfiliado, "POST", params, context);
    }




}
