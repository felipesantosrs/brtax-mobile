/**
 * BrTax - Mobile
 */


package com.brtax.util;

import java.io.IOException;

import org.ksoap2.serialization.SoapObject;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


/**
 * Classe que invoca o web service
 * @author Felipe
 *
 */
public class WebService {
    //Namespace of the Webservice - can be found in WSDL
    private static String NAMESPACE = "http://brtax.com/";
    //Webservice URL - WSDL File location    
    private static String URL = "http://192.168.25.23:8080/SearchProductBeanService/SearchProductBean?WSDL";
    //SOAP Action URI again Namespace + Web method name
    private static String SOAP_ACTION = "http://brtax.com/searchProductEAN";
    
    private static String WEB_METH_NAME = "searchProductEAN";
    
    /**
     * Método que invoca o web service
     * @param ean - ean informado na interface
     * @param price - priço informado na interface
     * @return response com informações sobre produto
     */
    public static  SoapSerializationEnvelope invokeWS(String ean, String price) {
    	
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, WEB_METH_NAME);
        // Property which holds input parameters
        request.addProperty("ean", ean);
        // Set Name
        request.addProperty("price", price);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        int timeOut=600000000;//
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, timeOut);
       
            // Invoke web service
        try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            // Get the response
      
           return envelope;
 
      
    }
}