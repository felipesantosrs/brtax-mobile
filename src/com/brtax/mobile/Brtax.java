package com.brtax.mobile;

import java.text.DecimalFormat;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brtax.dto.ProductDTO;
import com.brtax.util.WebService;

public class Brtax extends Activity {

	ImageButton btnImageCapture;
	EditText eanText;
	EditText price;
	ProductDTO p = null;
	Button btnConsult = null;
	Button btnClose = null;

	private ProgressBar bar = null;
	private PopupWindow pwindo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brtax);
		btnImageCapture = (ImageButton) findViewById(R.id.imageBarCode);
		eanText = (EditText) findViewById(R.id.eanText);
		price = (EditText) findViewById(R.id.price);
		btnConsult = (Button) findViewById(R.id.consultButton);
		bar = (ProgressBar) findViewById(R.id.progressBar);
		boolean var = true; 
		

		
		btnConsult.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				if (eanText.getText().toString().equals(null)|| eanText.getText().toString().trim().equals("") ) {
					eanText.setError("Código de barras deve ser informado");
				}
				else if (price.getText().toString().equals(null) || price.getText().toString().trim().equals("")){
				 price.setError("Preço do produto deve ser informado");
				}
				else if (eanText.getText().toString().trim().length()>13){
					 eanText.setError("Código de barras não pode ser maior que 13 caracteres");
				}else{
				new AsyncCallWS().execute();
				}
			}

		});
	}

	public void capture(View view) {
		IntentIntegrator.initiateScan(this, R.layout.capture,
				R.id_capture.viewfinder_view, R.id_capture.preview_view, true);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE:
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			final String result = scanResult.getContents();
			if ((result != null)
					&& (scanResult.getFormatName().toString()
							.contentEquals("EAN_13"))) {
				Handler handler = new Handler();
				handler.post(new Runnable() {
					public void run() {
						Intent it = new Intent(getBaseContext(), Brtax.class);
						it.putExtra("eanText", result);
						startActivity(it);
					}
				});
			} else {
				Toast.makeText(getBaseContext(),
						"Código inválido ou inexistente.", 5).show();
			}
			break;
		default:
		}
		finish();
	}

	public void consultTax(View view) {
		setContentView(R.layout.brtax);
		eanText = (EditText) findViewById(R.id.eanText);
		price = (EditText) findViewById(R.id.price);

	}

	class AsyncCallWS extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			SoapSerializationEnvelope envelope = WebService.invokeWS(eanText
					.getText().toString(), price.getText().toString());

			if (envelope.bodyIn != null) {
				KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
				for (int i = 0; i < ks.getPropertyCount(); i++) {
					SoapObject soapObject = (SoapObject) ks.getProperty(i);

					p = new ProductDTO();

					if (soapObject.getProperty("name") != null) {
						p.setName(soapObject.getProperty("name").toString());
					}

					if (soapObject.getProperty("price") != null) {
						p.setPrice(Double.parseDouble(soapObject.getProperty(
								"price").toString()));
					}

					if (soapObject.getProperty("priceFee") != null) {
						p.setPriceFee(Double.parseDouble(soapObject
								.getProperty("priceFee").toString()));
					}

					if (soapObject.getProperty("tax") != null) {
						p.setTax(Double.parseDouble(soapObject.getProperty(
								"tax").toString()));
					}

					if (soapObject.getProperty("valueTax") != null) {
						p.setValueTax(Double.parseDouble(soapObject
								.getProperty("valueTax").toString()));
					}

				}
			}

			runOnUiThread(new Runnable() {
				public void run() {
					if (p == null) {
						Toast.makeText(Brtax.this,
								"Erro ao carregar o produto.",
								Toast.LENGTH_SHORT).show();

					}else if (p.getName().toString()== null){
						Toast.makeText(Brtax.this,
								"Produto não encontrado.",
								Toast.LENGTH_SHORT).show();
					}
					
				}
			});

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			bar.setVisibility(View.INVISIBLE);
			if (p != null){
			// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater) Brtax.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View promptsView = inflater.inflate(R.layout.toast, (ViewGroup)

		    findViewById(R.id.popup_element));
			pwindo = new PopupWindow(promptsView , 780, 960, true);
			
			TextView resultTextEan = (TextView) promptsView.findViewById(R.id.resultTextEan);
			TextView resultTextName = (TextView) promptsView.findViewById(R.id.resultTextName);
			TextView resultTextPrecoTotal = (TextView) promptsView.findViewById(R.id.resultTextPrecoTotal);
			TextView resultTextValorImposto = (TextView) promptsView.findViewById(R.id.resultTextValorImposto);
			TextView resultTextValorSemImpost = (TextView) promptsView.findViewById(R.id.resultTextValorSemImposto);
			TextView resultTextPorcentagem = (TextView) promptsView.findViewById(R.id.resultTextPorcentagem);

			resultTextEan.setText("Código de Barras: "
					+ eanText.getText().toString());
			resultTextName.setText("Nome produto: " + p.getName());
			resultTextPrecoTotal.setText("Preço produto: " +  format(p.getPrice()));
			resultTextPorcentagem.setText("% Imposto: " + format(p.getTax()));
			resultTextValorImposto.setText("Valor do imposto: "
					+ format(p.getValueTax()));
			resultTextValorSemImpost.setText("Valor sem imposto: "
					+ format(p.getPriceFee()));
			pwindo.showAtLocation(promptsView, Gravity.CENTER, 0, 0);
			btnClose = (Button) promptsView.findViewById(R.id.close);

			btnClose.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					pwindo.dismiss();
				}
			});
			}
		}

		@Override
		protected void onPreExecute() {
			// Make ProgressBar invisible
			bar.setVisibility(View.VISIBLE);
		}
		
		public String format(double x) {  
		    return String.format("%.2f", x);  
		}  

	}
}
