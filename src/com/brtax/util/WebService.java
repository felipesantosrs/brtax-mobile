package com.brtax.util;

import java.io.IOException;

import org.ksoap2.serialization.SoapObject;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;



public class WebService {
    //Namespace of the Webservice - can be found in WSDL
    private static String NAMESPACE = "http://brtax.com/";
    //Webservice URL - WSDL File location    
    private static String URL = "http://192.168.25.23:8080/BRTAXService/BRTAXPortType?WSDL";
    //SOAP Action URI again Namespace + Web method name
    private static String SOAP_ACTION = "http://brtax.com/searchProductEAN";
    
    private static String WEB_METH_NAME = "searchProductEAN";
    
    public static  SoapSerializationEnvelope invokeWS(String ean, double price) {
    	 SoapSerializationEnvelope resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, WEB_METH_NAME);
        // Property which holds input parameters
        PropertyInfo productInformation = new PropertyInfo();
        PropertyInfo productPrice = new PropertyInfo();
        // Set Name
        productInformation.setName("ean");
        // Set Value
        productInformation.setValue("ean");
        // Set dataType
        productInformation.setType(String.class);
        // Add the property to request object
        request.addProperty(productInformation);
        productPrice.setName("price");
        // Set Value
        productPrice.setValue("price");
        // Set dataType
        productPrice.setType(Double.class);
        request.addProperty(productPrice);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
 
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