package com.example.proyectoformulario.utilidades;



import com.example.proyectoformulario.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class Preferencias extends PreferenceFragment {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.preferencias);
    }
}
