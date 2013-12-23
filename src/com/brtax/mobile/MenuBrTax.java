/**
 * BrTax - Mobile
 */

package com.brtax.mobile;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Classe que representa o menu
 * 
 * @author Felipe
 * 
 */
public class MenuBrTax extends ListActivity {
	private final String[] data = { "Consulta Imposto Unitário", };

	/**
	 * Método que realiza a criação da lista do menu
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data));
	}

	/**
	 * Método que realiza seta valores da lista
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		switch (position) {
		case 0:
			startActivity(new Intent(this, Brtax.class));
			break;
		default:
			Toast.makeText(this, data[position], Toast.LENGTH_SHORT).show();
			break;
		}

	}

}
