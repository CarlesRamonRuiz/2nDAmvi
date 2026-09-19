package cramonjdbc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
		//JDBCSmartCity.fitxaSensor(sc.nextInt());
		System.out.println("UPDATES");
		System.out.println("Marques Dubtoses");
		//JDBCSmartCity.marcaDubtoses(sc.nextInt(), sc.nextDouble(), sc.nextDouble());
		System.out.println("Desactivar sensors silenciosos");
		//JDBCSmartCity.desactivaSensorsSilenciosos(LocalDate.of(sc.nextInt(), sc.nextInt(), sc.nextInt()));
		System.out.println("DELETES");
		System.out.println("Neteja d'errors");
		//JDBCSmartCity.netejaLecturesError(LocalDate.of(sc.nextInt(), sc.nextInt(), sc.nextInt()));
		System.out.println("Eliminar zones buides");
		//JDBCSmartCity.eliminaZonesBuides();
		System.out.println("____________________________________");
		// 3. CONSULTES AMB JOINS
		System.out.println("3. CONSULTES AMB JOINS");
		System.out.println("Sensors sense lectures");
		//JDBCSmartCity.sensorsSenseLectures();
		System.out.println("Lecutures de zona, posa nom de la zona i qualitat");
		//JDBCSmartCity.lecturesDeZona(sc.nextLine(), sc.nextLine());
		System.out.println("____________________________________");
		System.out.println("4. AGREGACIONS AMB GROUP BY");
		System.out.println("Resum de zones");
		//JDBCSmartCity.resumZones();
		System.out.println("Estadistiques per tipus");
		//JDBCSmartCity.estadistiquesPerTipus();
		System.out.println("____________________________________");
		System.out.println("5. CREACIÓ DE NOVA TAULA");
		//JDBCSmartCity.crearNovaTaula();
		System.out.println("Insercions");
		//JDBCSmartCity.inserirManteniment(2, LocalDate.of(2026, 9, 27), "Mateo", "CORRECTIU", 110, null);
		//JDBCSmartCity.inserirManteniment(2, LocalDate.of(2026, 10, 22), "Ivan", "PREVENTIU", 0, null);
		//JDBCSmartCity.inserirManteniment(2, LocalDate.of(2026, 1, 1), "Biel", "CORRECTIU", 210, null);
		System.out.println("____________________________________");
		System.out.println("6. ALTER TABLE");
		System.out.println("Crear columna Ultim manteniment");
		//JDBCSmartCity.ultim_manteniment();
		//JDBCSmartCity.afegir_unique();
		//JDBCSmartCity.afegirDuplicat();
		System.out.println("____________________________________");
		System.out.println("7. PREPARED STATEMENTS");
		JDBCSmartCity.cercaSensors("E", 1, true);
		System.out.println();
		JDBCSmartCity.cercaSensors("C", 4, false);
		System.out.println();
		JDBCSmartCity.cercaSensors("U", 2, true);
		
		System.out.println("Consulta per rang de dates");
		JDBCSmartCity.lecturesEntreDates(1, LocalDateTime.of(LocalDate.of(2026,9, 1), LocalTime.of(7, 10)), LocalDateTime.of(LocalDate.of(2026, 10, 9), LocalTime.of(1, 10)));
		System.out.println("____________________________________");
		System.out.println("8. BATCH PROCESSING");
		
	}

}
