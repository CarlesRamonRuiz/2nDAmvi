package cramonjdbc;

import java.util.Scanner;

public class main {
static Scanner sc = new Scanner(System.in); 
	public static void main(String[] args) {
		
		// 1. Configuracio inicial
		System.out.println("1. Configuracio inicial");

		JDBCSmartCity.connect();
		System.out.println("____________________________________");

		// 2. OPERACIONS CRUD
		System.out.println("2. Operacions CRUD");
		System.out.println("CREAR");
		//JDBCSmartCity.registraLectura(sc.nextLine(), sc.nextInt());
		System.out.println("LLEGIR");
		JDBCSmartCity.fitxaSensor(sc.nextInt());
		System.out.println("____________________________________");

	}

}
