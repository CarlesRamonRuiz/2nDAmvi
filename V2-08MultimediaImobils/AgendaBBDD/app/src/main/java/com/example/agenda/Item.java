package com.example.agenda;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Item extends Activity implements View.OnClickListener {
    private EditText nom, cognom, adreca, telefon, email, dataNaixement;
    private int posicio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacte_crear);
        nom = (EditText) findViewById(R.id.txtNom);
        cognom = (EditText) findViewById(R.id.txtCognom);
        adreca = (EditText) findViewById(R.id.txtAdreca);
        telefon = (EditText) findViewById(R.id.editTextPhone);
        email = (EditText) findViewById(R.id.txtEmail);
        dataNaixement = (EditText) findViewById(R.id.txtDataNaixement);
        posicio = getIntent().getIntExtra("posicio", -1);

        TextView titol = (TextView) findViewById(R.id.txtCrearContacte);
        if (posicio == -1) {
            titol.setText(R.string.titolCrear);
            findViewById(R.id.btnEsborrar).setVisibility(View.GONE);
        } else {
            titol.setText(R.string.titolEditar);
            // Si es gira la pantalla, Android restaura els EditText.
            if (savedInstanceState == null) {
                nom.setText(getIntent().getStringExtra("nom"));
                cognom.setText(getIntent().getStringExtra("cognom"));
                adreca.setText(getIntent().getStringExtra("adreca"));
                telefon.setText(getIntent().getStringExtra("telefon"));
                email.setText(getIntent().getStringExtra("email"));
                dataNaixement.setText(getIntent().getStringExtra("data"));
            }
        }

        findViewById(R.id.btnCrear).setOnClickListener(this);
        findViewById(R.id.btnCancelar).setOnClickListener(this);
        findViewById(R.id.btnEsborrar).setOnClickListener(this);
        findViewById(R.id.btnTrucar).setOnClickListener(this);
        findViewById(R.id.btnEmail).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCrear) {
            acceptar();
        } else if (v.getId() == R.id.btnCancelar) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (v.getId() == R.id.btnEsborrar) {
            Intent resultat = new Intent();
            resultat.putExtra("posicio", posicio);
            resultat.putExtra("accio", "esborrar");
            setResult(RESULT_OK, resultat);
            finish();
        } else if (v.getId() == R.id.btnTrucar) {
            if (text(telefon).isEmpty()) {
                Toast.makeText(this, R.string.campObligatori, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + text(telefon)));
                startActivity(Intent.createChooser(intent, getString(R.string.trucar)));
            }
        } else if (v.getId() == R.id.btnEmail) {
            if (text(email).isEmpty()) {
                Toast.makeText(this, R.string.campObligatori, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{text(email)});
                startActivity(Intent.createChooser(intent, getString(R.string.enviarEmail)));
            }
        }
    }

    private String text(EditText camp) {
        return camp.getText().toString().trim();
    }

    private void acceptar() {
        if (text(nom).isEmpty()) {
            Toast.makeText(this, R.string.campObligatori, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent resultat = new Intent();
        resultat.putExtra("posicio", posicio);
        resultat.putExtra("accio", "guardar");
        resultat.putExtra("nom", text(nom));
        resultat.putExtra("cognom", text(cognom));
        resultat.putExtra("adreca", text(adreca));
        resultat.putExtra("telefon", text(telefon));
        resultat.putExtra("email", text(email));
        resultat.putExtra("data", text(dataNaixement));
        setResult(RESULT_OK, resultat);
        finish();
    }

}
