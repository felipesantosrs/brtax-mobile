package com.brtax.mobile;



import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brtax.dto.ProductDTO;
import com.brtax.util.WebService;

public class Brtax extends Activity {

	ImageButton btnImageCapture;
	EditText eanText;
	EditText price;
	private TextView r;
	ProductDTO p = null;
	Button btnConsult = null;
	

	private ProgressBar bar= null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brtax);
		btnImageCapture = (ImageButton)findViewById( R.id.imageBarCode );
		eanText = (EditText)findViewById( R.id.eanText);
		price = (EditText)findViewById( R.id.price);
		r = (TextView)findViewById( R.id.result);
		
	}
	
	
	
	public void capture(View view) {
		IntentIntegrator.initiateScan(this,
                R.layout.capture,
                R.id_capture.viewfinder_view,
                R.id_capture.preview_view,
                true);
	}
	
	public void consultTax(View view){
		setContentView(R.layout.brtax);
		eanText = (EditText)findViewById( R.id.eanText);
		price = (EditText)findViewById( R.id.price);
		r = (TextView)findViewById( R.id.result);
		btnConsult = (Button)findViewById(R.id.consultButton);
		bar=(ProgressBar) findViewById(R.id.progressBar);

		btnConsult.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new AsyncCallWS().execute();
			}
		

		});
	
		
		

		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    switch (requestCode) {
	        case IntentIntegrator.REQUEST_CODE:
	            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
	            final String result = scanResult.getContents();
	            if ((result != null) && (scanResult.getFormatName().toString().contentEquals("EAN_13"))) {
	            	Handler  handler = new Handler();
	            	handler.post(new Runnable() {
	                    public void run() {
	                        Intent it = new Intent(getBaseContext(), Brtax.class);
	                        it.putExtra("eanText", result);
	                        startActivity(it);
	                    }
	                });
	            } else {
	                Toast.makeText(getBaseContext(), "Código inválido ou inexistente.", 5).show();
	            }
	            break;
	        default:
	    }	
	
}
	class AsyncCallWS extends AsyncTask<Void, Void, Void> {
		AsyncCallWS(){
			
		}
		@Override
		protected Void doInBackground(Void... params) {
	    	SoapSerializationEnvelope envelope = WebService.invokeWS(eanText.toString(), Double.parseDouble(price.toString()));
	    	
	    	if (envelope.bodyIn != null) {
				KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
				for (int i = 0; i < ks.getPropertyCount(); i++) {
					SoapObject soapObject = (SoapObject) ks.getProperty(i);

					p = new ProductDTO();

					if (soapObject.getProperty("name") != null) {
						p.setName(soapObject.getProperty("name").toString());
					}

					if (soapObject.getProperty("price") != null) {
						p.setPrice(Double.parseDouble(soapObject.getProperty("price").toString()));
					}

					if (soapObject.getProperty("priceFee") != null) {
						p.setPriceFee(Double.parseDouble(soapObject.getProperty("priceFee").toString()));
					}

					if (soapObject.getProperty("tax") != null) {
						p.setTax(Double.parseDouble(soapObject.getProperty("tax").toString()));
					}

					if (soapObject.getProperty("valueTax") != null) {
						p.setValueTax(Double.parseDouble(soapObject.getProperty("valueTax").toString()));
					}
					
		
				
				}
			}

			runOnUiThread(new Runnable() {
				public void run() {
					if (p==null) {
						Toast.makeText(Brtax.this,"Erro ao carregar o produto.",
								Toast.LENGTH_SHORT).show();
						
					}
				}
			});

	    	
	    	return null;
	    }


	    @Override
	    protected void onPostExecute(Void result) {

	        //Make ProgressBar invisible
	    	bar.setVisibility(View.INVISIBLE);
	 
	    	r.setVisibility(View.VISIBLE);
	    	Toast.makeText(Brtax.this,"Nome produto: "+ p.getName(),
					Toast.LENGTH_LONG).show();
	    	Toast.makeText(Brtax.this,"Preço produto: "+ p.getPrice(),
					Toast.LENGTH_LONG).show();
	    	Toast.makeText(Brtax.this,"Porcentagem Imposto: "+ p.getTax(),
					Toast.LENGTH_LONG).show();
	    	Toast.makeText(Brtax.this,"Valor do imposto: "+ p.getValueTax(),
					Toast.LENGTH_LONG).show();
	    	Toast.makeText(Brtax.this,"Valor sem imposto: "+ p.getPriceFee(),
					Toast.LENGTH_LONG).show();
	    	
	    }

	    @Override
	    protected void onPreExecute() {
	                    //Make ProgressBar invisible
	        bar.setVisibility(View.VISIBLE);
	    }
	}
}