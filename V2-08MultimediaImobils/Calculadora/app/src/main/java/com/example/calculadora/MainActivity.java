
package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Guarda la operacion
    String operacioActual = "";

    // Guarda el numero almacenado en memoria
    double memoria = 0;


    boolean resultatCalculat = false;

    TextView operacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        operacio = findViewById(R.id.Resultat);
        operacio.setText("0");

        // NUMEROS

        View.OnClickListener listenerNumeros = v -> {


            Button boton = (Button) v;


            String numero = boton.getText().toString();

            if (resultatCalculat) {
                operacioActual = "";
                resultatCalculat = false;
            }

            if (operacioActual.equals("0")) {
                operacioActual = numero;
            } else {
                operacioActual += numero;
            }


            operacio.setText(operacioActual);
        };


        int[] botonesNumeros = {
                R.id.n0,
                R.id.n1,
                R.id.n2,
                R.id.n3,
                R.id.n4,
                R.id.n5,
                R.id.n6,
                R.id.n7,
                R.id.n8,
                R.id.n9
        };

        // Todos los numeros utilizan el mismo listener
        for (int id : botonesNumeros) {
            Button boton = findViewById(id);
            boton.setOnClickListener(listenerNumeros);
        }


        View.OnClickListener listenerOperaciones = v -> {

            int id = v.getId();


            if (id == R.id.nSuma) {
                afegirOperador("+");


            } else if (id == R.id.nResta) {
                afegirOperador("-");


            } else if (id == R.id.nMultiplica) {
                afegirOperador("*");


            } else if (id == R.id.nDividir) {
                afegirOperador("/");


            } else if (id == R.id.nEquals) {

                Double resultat = calcular();

                if (resultat != null) {


                    operacioActual = formatNumero(resultat);

                    operacio.setText(operacioActual);

                    resultatCalculat = true;
                }


            } else if (id == R.id.nBorrar) {

                if (!operacioActual.isEmpty()) {

                    operacioActual = operacioActual.substring(
                            0,
                            operacioActual.length() - 1
                    );
                }

                mostrarPantalla();
                resultatCalculat = false;


            } else if (id == R.id.nC) {

                operacioActual = "";
                resultatCalculat = false;

                mostrarPantalla();


            } else if (id == R.id.nCe) {

                int posicion = buscarOperador();

                if (posicion != -1) {


                    operacioActual = operacioActual.substring(
                            0,
                            posicion + 1
                    );

                } else {


                    operacioActual = "";
                }

                resultatCalculat = false;
                mostrarPantalla();





            } else if (id == R.id.nMPlus) {

                Double valor = obtenirValorActual();

                if (valor != null) {
                    memoria += valor;
                }

            } else if (id == R.id.nMMinus) {

                Double valor = obtenirValorActual();

                if (valor != null) {
                    memoria -= valor;
                }

            } else if (id == R.id.nMC) {

                memoria = 0;

            } else if (id == R.id.nMR) {

                String numeroMemoria = formatNumero(memoria);

                int posicion = buscarOperador();

                if (posicion != -1) {


                    operacioActual = operacioActual.substring(
                            0,
                            posicion + 1
                    ) + numeroMemoria;

                } else {


                    operacioActual = numeroMemoria;
                }

                resultatCalculat = false;
                mostrarPantalla();
            }
        };

        int[] botonesOperaciones = {
                R.id.nSuma,
                R.id.nResta,
                R.id.nMultiplica,
                R.id.nDividir,
                R.id.nEquals,
                R.id.nBorrar,
                R.id.nC,
                R.id.nCe,
                R.id.nMPlus,
                R.id.nMMinus,
                R.id.nMC,
                R.id.nMR
        };

        // Todos utilizan el segundo listener
        for (int id : botonesOperaciones) {
            Button boton = findViewById(id);
            boton.setOnClickListener(listenerOperaciones);
        }
    }



    private void mostrarPantalla() {

        if (operacioActual.isEmpty()) {
            operacio.setText("0");
        } else {
            operacio.setText(operacioActual);
        }
    }



    private int buscarOperador() {


        for (int i = 1; i < operacioActual.length(); i++) {

            char caracter = operacioActual.charAt(i);

            if (caracter == '+' ||
                    caracter == '-' ||
                    caracter == '*' ||
                    caracter == '/') {

                return i;
            }
        }


        return -1;
    }

    private void afegirOperador(String operador) {

        if (operacioActual.isEmpty()) {
            return;
        }

        int posicion = buscarOperador();

        if (posicion != -1) {

            if (posicion == operacioActual.length() - 1) {

                operacioActual = operacioActual.substring(
                        0, posicion
                ) + operador;

            } else {

                Double resultat = calcular();

                if (resultat == null) {
                    return;
                }

                operacioActual = formatNumero(resultat)
                        + operador;
            }

        } else {

            operacioActual += operador;
        }

        resultatCalculat = false;
        mostrarPantalla();
    }

    private Double calcular() {

        int posicion = buscarOperador();

        if (posicion == -1) {
            return null;
        }

        if (posicion == operacioActual.length() - 1) {
            return null;
        }

        try {

            String op1 = operacioActual.substring(
                    0, posicion
            );

            String op2 = operacioActual.substring(
                    posicion + 1
            );

            char operador = operacioActual.charAt(posicion);

            double num1 = Double.parseDouble(op1);
            double num2 = Double.parseDouble(op2);

            double resultat;

            switch (operador) {

                case '+':
                    resultat = num1 + num2;
                    break;

                case '-':
                    resultat = num1 - num2;
                    break;

                case '*':
                    resultat = num1 * num2;
                    break;

                case '/':

                    if (num2 == 0) {
                        operacio.setText("Error: division por 0");
                        return null;
                    }

                    resultat = num1 / num2;
                    break;

                default:
                    return null;
            }

            return resultat;

        } catch (NumberFormatException e) {

            operacio.setText("Error");
            return null;
        }
    }

    // ========================================
    // OBTENER EL VALOR ACTUAL PARA LA MEMORIA
    // ========================================

    private Double obtenirValorActual() {

        int posicion = buscarOperador();

        if (posicion != -1) {
            return calcular();
        }

        if (operacioActual.isEmpty()) {
            return null;
        }

        try {

            return Double.parseDouble(operacioActual);

        } catch (NumberFormatException e) {

            return null;
        }
    }

    private String formatNumero(double numero) {

        if (numero == 0) {
            return "0";
        }

        if (numero == (int) numero) {
            return String.valueOf((int) numero);
        }

        return String.valueOf(numero);
    }
}
