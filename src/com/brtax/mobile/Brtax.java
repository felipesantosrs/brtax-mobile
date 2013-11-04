package com.brtax.mobile;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class Brtax extends Activity {

	ImageButton btnImageCapture;
	EditText eanText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brtax);
		btnImageCapture = (ImageButton)findViewById( R.id.imageBarCode );
		eanText = (EditText)findViewById( R.id.eanText);
		
	}
	
	public void capture(View view) {
		IntentIntegrator.initiateScan(this,
                R.layout.capture,
                R.id_capture.viewfinder_view,
                R.id_capture.preview_view,
                true);
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

	

}
