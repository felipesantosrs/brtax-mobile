/**
 * BrTax - Mobile
 */

package com.brtax.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Classe que realiza o splash da tela
 * 
 * @author Felipe
 * 
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, MenuBrTax.class));
				SplashActivity.this.finish();
			}
		}, 3000);
	}

}
