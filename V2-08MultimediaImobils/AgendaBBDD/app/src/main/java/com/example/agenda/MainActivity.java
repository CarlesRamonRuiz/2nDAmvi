package com.example.agenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final int EDITAR_CONTACTE = 1;
    private ArrayList<Contacte> AgendaContactes = new ArrayList<Contacte>();
    private ListView lstOpcions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //El if es el que s'executa per primera vegada quan s'obra la app
        if (savedInstanceState == null) {
            AgendaContactes.add(new Contacte("Carles", "Ramon Ruiz", "Carrer Copenhagen, n7, 1-1", "+34 676 76 67 76", "carlesramonruiz@gmail.com", "12-10-2007"));
            AgendaContactes.add(new Contacte("Christian", "Mellado Martin", "Avinguda Copenhagen, n7, 4-3", "+34 692 30 44 31", "chmelladomarti@gmail.com", "12-10-2004"));
        } else {
            // Recuperem els contactes si Android ha recreat l'Activity.
            int total = savedInstanceState.getInt("total");
            for (int i = 0; i < total; i++) {
                AgendaContactes.add(new Contacte(
                        savedInstanceState.getString("nom" + i),
                        savedInstanceState.getString("cognom" + i),
                        savedInstanceState.getString("adreca" + i),
                        savedInstanceState.getString("telefon" + i),
                        savedInstanceState.getString("email" + i),
                        savedInstanceState.getString("data" + i)));
            }
        }

        lstOpcions = (ListView) findViewById(R.id.lstOpcions);
        lstOpcions.setAdapter(new AdaptadorTitulares(this));
        lstOpcions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obrirContacte(position);
            }
        });
        findViewById(R.id._btnAfegirContacte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obrirContacte(-1);
            }
        });
    }

    private void obrirContacte(int position) {
        Intent intent = new Intent(this, Item.class);
        intent.putExtra("posicio", position);
        if (position >= 0) {
            Contacte contacte = AgendaContactes.get(position);
            intent.putExtra("nom", contacte.getNom());
            intent.putExtra("cognom", contacte.getCognom());
            intent.putExtra("adreca", contacte.getAdreca());
            intent.putExtra("telefon", contacte.getTelefon());
            intent.putExtra("email", contacte.getEmail());
            intent.putExtra("data", contacte.getData_naixement());
        }
        startActivityForResult(intent, EDITAR_CONTACTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITAR_CONTACTE && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("posicio", -1);
            String accio = data.getStringExtra("accio");
            if ("esborrar".equals(accio)) {
                if (position >= 0 && position < AgendaContactes.size()) {
                    AgendaContactes.remove(position);
                }
            } else {
                Contacte contacte = new Contacte(data.getStringExtra("nom"),
                        data.getStringExtra("cognom"), data.getStringExtra("adreca"),
                        data.getStringExtra("telefon"), data.getStringExtra("email"),
                        data.getStringExtra("data"));
                if (position == -1) {
                    AgendaContactes.add(contacte);
                } else if (position >= 0 && position < AgendaContactes.size()) {
                    AgendaContactes.set(position, contacte);
                }
            }
            // Tornem a mostrar la llista amb les dades actualitzades.
            lstOpcions.setAdapter(new AdaptadorTitulares(this));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("total", AgendaContactes.size());
        for (int i = 0; i < AgendaContactes.size(); i++) {
            Contacte contacte = AgendaContactes.get(i);
            outState.putString("nom" + i, contacte.getNom());
            outState.putString("cognom" + i, contacte.getCognom());
            outState.putString("adreca" + i, contacte.getAdreca());
            outState.putString("telefon" + i, contacte.getTelefon());
            outState.putString("email" + i, contacte.getEmail());
            outState.putString("data" + i, contacte.getData_naixement());
        }
    }

    class AdaptadorTitulares extends ArrayAdapter<Contacte> {
        private Activity context;

        public AdaptadorTitulares(Activity context) {
            super(context, R.layout.contacte_modificar, AgendaContactes);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.contacte_modificar, parent, false);
            Contacte contacte = AgendaContactes.get(position);
            TextView titol = (TextView) item.findViewById(R.id.lblTitulo);
            TextView subtitol = (TextView) item.findViewById(R.id.lblSubtitulo);
            titol.setText(contacte.getNom() + " " + contacte.getCognom());
            subtitol.setText(contacte.getTelefon() + "\n" + contacte.getEmail());
            return item;
        }
    }
}
