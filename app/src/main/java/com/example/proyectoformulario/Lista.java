package com.example.proyectoformulario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import datos.Persona;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.BufferType;

public class Lista extends Activity {
    private TextView resultado;
    private ListView listaPersonas;
    private List<Persona> personas;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        intent = new Intent();
        setResult(200, intent);

        resultado = (TextView) findViewById(R.id.resultado);
        listaPersonas = (ListView) findViewById(R.id.listaPersonas);

        personas = getIntent().getParcelableArrayListExtra("lista");

        AdaptadorPersona adaptadorPersona = new AdaptadorPersona(this);
        listaPersonas.setAdapter(adaptadorPersona);

        listaPersonas
                .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                        Toast.makeText(Lista.this, "Click largo", Toast.LENGTH_LONG).show();

                        String opcionSeleccionada =
                                ((TextView) v.findViewById(R.id.listaNombre))
                                        .getText().toString();
                        resultado.setText("Click largo selecciono a " + opcionSeleccionada);
                        return true;
                    }
                });

        listaPersonas.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                Persona persona = (Persona) a.getAdapter().getItem(position);
                String opcionSeleccionada = persona.getNombre() + " "
                        + persona.getCedula();
                resultado.setText("Opci�n seleccionada: " + opcionSeleccionada);
                intent.putExtra("resultado", opcionSeleccionada);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista, menu);
        return true;
    }


    class AdaptadorPersona extends ArrayAdapter<Persona> {
        Activity context;

        public AdaptadorPersona(Activity context) {
            super(context, R.layout.lista_persona, personas);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View item = convertView;
            Mantenedor mant;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.lista_persona, null);

                mant = new Mantenedor();
                mant.listaNombre = (TextView) item
                        .findViewById(R.id.listaNombre);
                mant.listaCedula = (TextView) item
                        .findViewById(R.id.listaCedula);
                mant.listaFechaNac = (TextView) item
                        .findViewById(R.id.listaFechaNac);
                mant.listaEstadoCivil = (TextView) item
                        .findViewById(R.id.listaEstadoCivil);
                mant.listaDiscapacitado = (TextView) item
                        .findViewById(R.id.listaDiscapacitado);
                mant.listaEstatura = (TextView) item
                        .findViewById(R.id.listaEstatura);

                item.setTag(mant);

            } else {
                mant = (Mantenedor) item.getTag();
            }

            // mant.listaNombre.setText(personas.get(position).getNombre());
            mant.listaNombre.setText(
                    Html.fromHtml("<i><b>" + personas.get(position).getNombre()
                            + "</i></b>"), BufferType.SPANNABLE);

            mant.listaCedula.setText(personas.get(position).getCedula());
            mant.listaFechaNac.setText(new SimpleDateFormat("dd/MM/yyyy")
                    .format(personas.get(position).getFechaNac()));
            mant.listaEstadoCivil.setText(personas.get(position)
                    .getEstadoCivil());
            mant.listaDiscapacitado.setText(personas.get(position)
                    .getDiscapacitado() ? "discapacitado" : "");
            mant.listaEstatura.setText(personas.get(position).getEstatura()
                    .toString());

            return item;
        }
    }

    static class Mantenedor {
        TextView listaNombre;
        TextView listaCedula;
        TextView listaFechaNac;
        TextView listaEstadoCivil;
        TextView listaDiscapacitado;
        TextView listaEstatura;
    }

}
