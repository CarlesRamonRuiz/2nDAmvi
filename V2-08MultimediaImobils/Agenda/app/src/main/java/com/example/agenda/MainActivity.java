package com.example.agenda;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	String[] datos= new String[] {"Element 1","Element 2","Element 3","Element 4","Element 5","Element 5","Element 7","Element 8"};
	final Titular[] datosE= new Titular[] {new Titular("Titulo 1","Substitulo 1"),
									 new Titular("Titulo 2","Substitulo 2"),
									 new Titular("Titulo 3","Substitulo 3"),
									 new Titular("Titulo 4","Substitulo 4"),
									 new Titular("Titulo 5","Substitulo 5")};
    @Override
    
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conectem al ListView com el Spinner per  sortir  molt simple,.
        
        ArrayAdapter<String> adaptador = new  ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, datos);
        
        ListView lstOpcions = (ListView) findViewById(R.id.lstOpcions);
        
        lstOpcions.setAdapter(adaptador);
        
        // Anem a controlar la pulsaci  sobre un element del ListView
        
        lstOpcions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {				
				// TODO Auto-generated method stub
				// int arg2 -->  Indica la posisi  de l'element de l'array
				
				
				
			}
		});
        

        
        // Anem a fer-ho amb una vista personalitzada (Fem la clase AdaptadorTitulares propia)
        // que construir  la vista personalitzada a partir de la classe Titular i el Layout listitem_titular.
        
        AdaptadorTitulares adaptadorTitulares = new AdaptadorTitulares(this);
        
        ListView lstOpciones = (ListView) findViewById(R.id.lstOpcions);
        
        lstOpciones.setAdapter(adaptadorTitulares);
    
        lstOpciones.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), datosE[arg2].getTitulo(), Toast.LENGTH_SHORT).show();
			}
		});
         
    } // onCreate

  
    // ************* PErsonalizamos el Array Adapter 
    
    class AdaptadorTitulares extends ArrayAdapter  {
 
		Activity context;
		public AdaptadorTitulares(Activity context) {
			super(context, R.layout.listitem_titular, datosE);
			this.context = (Activity) context;			
		}
    	
		
		// GetView s'executa per cada element de l'array de dades i el que fa
		// es "inflar" el layout del XML que hem creat
		
    	@Override    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		// TODO Auto-generated method stub
    		// return super.getView(position, convertView, parent);
    		Log.i("Niko","1");
    		// Inflem el Lauoyt    		    		    				
    		LayoutInflater inflater = context.getLayoutInflater();
    		// Sobre el layout crear (inflat) dupliquem el layour creat amb els objectes, view personals.
    		View item = inflater.inflate(R.layout.listitem_titular, null);
    		// OJOOOO!!!!! hemos de hacer el findViewById del item que tenemos inflado.
    		TextView lblTitulo = (TextView) item.findViewById(R.id.lblTitulo);
    		
    		lblTitulo.setText(datosE[position].getTitulo().toString());    	
    		TextView lblSubTitulo = (TextView) item.findViewById(R.id.lblSubtitulo);
    		// Log.i("Niko","3->"+datosE[position].getSubtitulo().toString() );
    		lblSubTitulo.setText(datosE[position].getSubtitulo().toString());
    		// Log.i("Niko","4");
    		return (item);    		
    	}
    	
    }
    
    
    
}
