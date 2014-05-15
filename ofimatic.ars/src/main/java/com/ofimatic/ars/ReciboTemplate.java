package com.ofimatic.ars;

/**
 * Created by Santiago on 08/04/2014.
 */
public class ReciboTemplate {


    public String recibo (String fecha, String noAprobacion, String noAfiliado, String nombreAfiliado, String nombreServicio, String nombreMedico, String montoServicio, String descuento, String montoPagar )
    {
        String DATARECIBO = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>Recibo ARS</title>\n" +
                "</head>\n" +
           "<style>\n" +
                "\t.celda{\n" +
                "\t\t  background-color:#000000;\n" +
                "\t\t}\n" +
                "\n" +
                "\n" +
                "body\n" +
                "{\n"+
                "\n" +
                "\twidth:300px;\n" +
                "\theight:auto;\n" +
                "  margin: 0 auto;\n" +
                "\t}\n"+
            "</style>\n" +
                "\n" +
                "<body>\n" +
                "<table>\n" +
                " <tr>\n" +
                "    <td height=\"50\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <th colspan=\"3\"><font size=\"22\">ARS</font></th>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"200px\"></td>\n" +
                "    <td><b>Fecha: </b></td>\n" +
                "  </tr>\n" +
                "  <tr><td></td>\n" +
                "    <td>\n" +
                fecha +
                "    </td>  \n" +

                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Aprobación No.: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+noAprobacion+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Afiliado No.: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+noAfiliado+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Nombre: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+nombreAfiliado+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "  \n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Servicio: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+nombreServicio+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Médico: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+nombreMedico+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>\n" +
                "    \t<b> Monto Servicio: </b>\n" +
                "    </td>  \n" +
                "    <td align=\"right\"> "+montoServicio+" </td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td>\n" +
                "    \t<b> Descuento: </b>\n" +
                "    </td>  \n" +
                "    <td align=\"right\"> "+descuento+" </td>\n" +
                "  </tr>\n" +
                "  \n" +
                "  <tr>\n" +
                "    <td>\n" +
                "    </td>  \n" +
                "    <td class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td>\n" +
                "    \t<b> Monto a Pagar: </b>\n" +
                "    </td>  \n" +
                "    <td align=\"right\"> "+montoPagar+" </td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"50\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n";

        return DATARECIBO;
    }


    public String reciboPrint (String fecha, String noAprobacion, String noAfiliado, String nombreAfiliado, String nombreServicio, String nombreMedico, String montoServicio, String descuento, String montoPagar )
    {
        String DATARECIBO = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>Recibo ARS</title>\n" +
                "</head>\n" +
                "<style>\n" +
                "\t.celda{\n" +
                "\t\t  background-color:#000000;\n" +
                "\t\t}\n" +
                "\n" +
                "\n" +
                "body\n" +
                "{\n"+
                "\n" +
                "\twidth:310px;\n" +
                "\t}\n"+
                "</style>\n" +
                "\n" +
                "<body>\n" +
                "<table>\n" +
                "  <tr>\n" +
                "    <th colspan=\"3\"><font size=\"22\">ARS</font></th>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"200px\"></td>\n" +
                "    <td><b>Fecha: </b></td>\n" +
                "  </tr>\n" +
                "  <tr><td></td>\n" +
                "    <td>\n" +
                fecha +
                "    </td>  \n" +

                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Aprobación No.: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+noAprobacion+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Afiliado No.: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+noAfiliado+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Nombre: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+nombreAfiliado+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "  \n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Servicio: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+nombreServicio+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Médico: </b>\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t"+nombreMedico+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>\n" +
                "    \t<b> Monto Servicio: </b>\n" +
                "    </td>  \n" +
                "    <td align=\"right\"> "+montoServicio+" </td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td>\n" +
                "    \t<b> Descuento: </b>\n" +
                "    </td>  \n" +
                "    <td align=\"right\"> "+descuento+" </td>\n" +
                "  </tr>\n" +
                "  \n" +
                "  <tr>\n" +
                "    <td>\n" +
                "    </td>  \n" +
                "    <td class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td>\n" +
                "    \t<b> Monto a Pagar: </b>\n" +
                "    </td>  \n" +
                "    <td align=\"right\"> "+montoPagar+" </td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n";

        return DATARECIBO;
    }

    public String reciboPrintEjemplo (String fecha, String noAprobacion, String noAfiliado, String nombreAfiliado, String nombreServicio, String nombreMedico, String montoServicio, String descuento, String montoPagar )
    {
        String DATARECIBO = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>Recibo ARS</title>\n" +
                "</head>\n" +
                "<style>\n" +
                "\t.celda{\n" +
                "\t\t  background-color:#000000;\n" +
                "\t\t}\n" +
                "body\n" +
                "{\n" +
                "\twidth:310px;\n" +
                "\t}\n" +
                "</style>\n" +
                "\n" +
                "<body>\n" +
                "<table>\n" +
                "  <tr>\n" +
                "    <th colspan=\"3\"><font size=\"22\">ARS</font></th>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"200px\"></td>\n" +
                "    <td width=\"300px\"><b>Fecha:</b> "+fecha+"</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Aprobación No.: </b> "+noAprobacion+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                " \n" +
                "   <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Afiliado No.: </b> "+"    "+noAfiliado+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "\n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Nombre: </b> "+nombreAfiliado+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                " \n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "  \n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Servicio: </b> "+nombreServicio+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                "  \n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan=\"2\">\n" +
                "    \t<b> Médico: </b> "+nombreMedico+"\n" +
                "    </td>  \n" +
                "    <td></td> \n" +
                "  </tr>\n" +
                " \n" +
                "  <tr>\n" +
                "    <td height=\"15\">\n" +
                "    </td>  \n" +
                "    <td></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td colspan=\"3\" class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>\n" +
                "    \t<b> Monto Servicio: </b>\n" +
                "    </td>  \n" +
                "    <td align=\"right\"> "+montoServicio+" </td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td>\n" +
                "    \t<b> Descuento: </b>\n" +
                "    </td>  \n" +
                "    <td align=\"right\"> "+descuento+" </td>\n" +
                "  </tr>\n" +
                "  \n" +
                "  <tr>\n" +
                "    <td>\n" +
                "    </td>  \n" +
                "    <td class=\"celda\"></td>\n" +
                "  </tr>\n" +
                "   <tr>\n" +
                "    <td>\n" +
                "    \t<b> Monto a Pagar: </b>\n" +
                "    </td>  \n" +
                "    <td align=\"right\"> "+montoPagar+" </td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n";

        return DATARECIBO;
    }

}
