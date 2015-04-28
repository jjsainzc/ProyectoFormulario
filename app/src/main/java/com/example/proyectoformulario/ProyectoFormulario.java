package com.example.proyectoformulario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import com.example.proyectoformulario.utilidades.EntradaSalida;
import com.example.proyectoformulario.utilidades.PreferenciaActivity;

import datos.Persona;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProyectoFormulario extends Activity {
	private EditText nombre;
	private EditText cedula;
	private EditText fechaNac;
	private Spinner estadoCivil;
	private CheckBox discapacitado;
	private EditText estatura;

	private List<Persona> personas;
	private Persona persona;
	private String estadoCivilSel;
	
	private Boolean leerArchivoInicio;   

	protected static final int LISTA = 1;
	
	private static final String AUTHORITY = "com.example.proyectoformulario.archivo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proyecto_formulario);

		nombre = (EditText) findViewById(R.id.nombre);
		cedula = (EditText) findViewById(R.id.cedula);
		fechaNac = (EditText) findViewById(R.id.fechaNac);
		estadoCivil = (Spinner) findViewById(R.id.estadoCivil);
		discapacitado = (CheckBox) findViewById(R.id.discapacitado);
		estatura = (EditText) findViewById(R.id.estatura);

		estadoCivilSel = "Casado";

		personas = new ArrayList<Persona>();

		ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(
				this, R.array.estado_civil,
				android.R.layout.simple_spinner_item);
		adaptador.setDropDownViewResource(android.R.layout.simple_spinner_item);
		estadoCivil.setAdapter(adaptador);

		estadoCivil
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							android.view.View v, int position, long id) {
						estadoCivilSel = getResources().getStringArray(
								R.array.estado_civil)[position];
					}

					public void onNothingSelected(AdapterView<?> parent) {
						estadoCivilSel = "";
					}
				});
		
		leerPreferencias(); 
		if (leerArchivoInicio) {
			try {
				personas = EntradaSalida.<Persona> leerArchivoObjeto(
						getBaseContext().getFileStreamPath("archivo.bin"));
			} catch (ClassNotFoundException e) {
			} catch (IOException e) {
			}
		}

	}
	
	
	
	
	public void alerta(String s) {
		new AlertDialog.Builder(this)
		.setMessage(s)
		.setTitle("ERROR ")
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int id) {
						dialog.dismiss();
					}
				}).create().show();
	}
	
	private void leerPreferencias() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);

		leerArchivoInicio = pref.getBoolean("leerAutomatico", false);

	}

	public void guardar(View v) {
		Date fecha;
		Float estatura;
		
		try {
			estatura = Float.parseFloat(this.estatura.getText().toString());
		}
		catch (NumberFormatException e) {
			alerta("Estatura invalida");
			return;
		}
		
		try {

			fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaNac.getText()
					.toString());
		} catch (ParseException e) {
			alerta("Fecha invalida");
			return;
		}


		persona = new Persona(
				nombre.getText().toString(), 
				cedula.getText().toString(), 
				fecha, 
				estadoCivilSel, 
				discapacitado.isChecked(),
				estatura);

		personas.add(persona);

		try {
			EntradaSalida.<Persona>escribirArchivoObjeto( 
					getBaseContext().getFileStreamPath("archivo.bin"),
					personas);
		} catch (Exception e) {
			Toast.makeText(this, "ERROR " + e.toString(), Toast.LENGTH_LONG)
					.show();
		}

		Log.i("TRAZA", "Paso 2");
		nombre.setText("");
		cedula.setText("");
		fechaNac.setText("");
		estadoCivil.setSelection(0);
		discapacitado.setChecked(false);
		this.estatura.setText("0.0");

	}

	public void lista(View v) {
		Intent intent = new Intent(this, Lista.class);
		intent.putParcelableArrayListExtra("lista",
				(ArrayList<? extends Parcelable>) personas);
		startActivityForResult(intent, LISTA);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case LISTA:
			Toast.makeText(
					this,
					String.valueOf(resultCode) + " "
							+ data.getStringExtra("resultado"),
					Toast.LENGTH_LONG).show();
			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.proyecto_formulario, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemPreferencia:
			startActivity(new Intent(this,PreferenciaActivity.class));
			break;
		case R.id.action_settings:
			try {
				
				personas = EntradaSalida.<Persona> leerArchivoObjeto(
						getBaseContext().getFileStreamPath("archivo.bin"));
				return true;
			} catch (Exception e) {
				Toast.makeText(this, "ERROR " + e.toString(), Toast.LENGTH_LONG)
						.show();
				return false;
			}
			finally {
				break;
			}
			
		default:
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

}
