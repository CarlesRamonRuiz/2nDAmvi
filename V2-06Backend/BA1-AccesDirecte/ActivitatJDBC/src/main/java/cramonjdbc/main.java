package cramonjdbc;

import java.util.Scanner;

public class main {
static Scanner sc = new Scanner(System.in); 
	public static void main(String[] args) {
		
		// 1. Configuracio inicial
		JDBCSmartCity.connect();
		
		// 2. OPERACIONS CRUD
		System.out.println("CREAR");
		JDBCSmartCity.registraLectura(sc.nextLine(), sc.nextInt());
	}

}
