package com.example.proyectoformulario.utilidades;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PreferenciaActivity extends FragmentActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new Preferencias())
                .commit();
    }

}
