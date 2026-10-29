package com.example.spinnerintents;

import android.app.Activity;
import android.os.Bundle;


import android.content.Intent;
import android.view.Menu;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {

    Button cmdEnviar;
    String[] ArrayProba = new String[] {"Uno","Dos","Tres"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlazamos el sprinner al array declarado en tiempo de ejecucion
        Spinner spiProva =(Spinner)findViewById(R.id.spiProva);
        ArrayAdapter<String> AA= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ArrayProba );
        spiProva.setAdapter(AA);




        cmdEnviar= (Button) findViewById(R.id.cmdEnviar);
        cmdEnviar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EditText campoNombre = (EditText) findViewById(R.id.txtNombre);
                String nombre = campoNombre.getText().toString();

                EditText campoEmail = (EditText) findViewById(R.id.txtmail);
                String email = campoEmail.getText().toString();

                Spinner tipoConsulta = (Spinner) findViewById(R.id.spiForma);
                String tipo = tipoConsulta.getSelectedItem().toString();

                EditText campoMensaje = (EditText) findViewById(R.id.txtMensaje);
                String mensaje = campoMensaje.getText().toString();

                CheckBox checkboxUsarCorreo = (CheckBox) findViewById(R.id.cbUsarMail);
                boolean bUsarCorreo = checkboxUsarCorreo.isChecked();

                /** Enviaremos un eMail a trav�s de una Activity de la aplicaci�n de correo, con un Intent*/
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");

                /** Usar el eMail de destino*/
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{"carlesramonruiz.com"});

                /** Usar el propio eMail para recibir una copia*/
                if (bUsarCorreo)
                    emailIntent.putExtra(android.content.Intent.EXTRA_CC,
                            new String[]{email});


                /** Estableceremos el asunto*/
                String emailAsunto = "Comentarios Formulario";
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, emailAsunto);

                /** A�adimos el texto*/
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);

                /** No a�adiremos adjuntos. Pero se indica la forma de hacerlo*/
                //emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

                startActivity(Intent.createChooser
                        (emailIntent, "Enviar correo..."));
                // CreateChooser fa que si hi ha m�s d�una aplicaci� per enviar correu, es pugi escollir quina fer servir.



            }
        });
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


}