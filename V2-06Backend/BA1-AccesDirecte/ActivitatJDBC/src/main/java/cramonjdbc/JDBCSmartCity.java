package cramonjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCSmartCity {
	static String url = "jdbc:mysql://localhost:3306/ciutat_sensors";
	static String user = "root";
	static String password = "";
	static Scanner sc = new Scanner(System.in);

	public static void connect() {

		// Intentar establir connexió
		try {
			// Carregar el driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Establir connexió
			Connection connection = DriverManager.getConnection(url, user, password);
			System.out.println("Connexió exitosa!");

			// Crear un Statement per executar la consulta SQL
			Statement statement = connection.createStatement();

			String query = "SHOW TABLES";
			ResultSet resultSet = statement.executeQuery(query);

			// Mostrar les bases de dades
			System.out.println("Llistat de bases de dades:");
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1)); // Mostrar el nombre de la base de dades
			}

			// Tancar ResultSet, Statement i la connexió
			connection.close();
			statement.close();
			resultSet.close();

		} catch (ClassNotFoundException e) {
			System.out.println("No s'ha pogut carregar el driver JDBC");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error al connectar amb la base de dades");
			e.printStackTrace();
		}

	}

	public static int registraLectura(String nomSensor, double valor) {
		try (Connection connection = DriverManager.getConnection(url, user, password)) {

			boolean flag1 = false;
			boolean flag2 = false;
			String select = "SELECT * FROM sensors s WHERE s.nom = '" + nomSensor + "';";
			Statement statement = connection.createStatement();
			ResultSet resultset = statement.executeQuery(select);
			if (!resultset.next()) {
			    System.out.println("Aquest sensor no existeix");
			    return -1;
			}

			if (resultset.getBoolean("actiu")==false) {
			    System.out.println("Aquest sensor esta inactiu");
			    return -1;
			}
			if (flag1) {
				System.out.println("Aquest sensor no existeix");
				return -1;
			} else if (flag2) {
				System.out.println("Aquest sensor esta inactiu");
				return -1;
			} else {
				// Consulta
				String create = "INSERT INTO lectures\n" + "(sensor_id,\n" + "    valor,\n" + "    data_lectura,\n"
						+ "    qualitat)\n" + "Select s.id, ?, current_timestamp(), 'OK'\n" + "from sensors s\n"
						+ "Where s.nom = ?\n" + ";";
				// Obrim prepared statement
				PreparedStatement preparedstatement = connection.prepareStatement(create);

				// Posem els valor on han de ser
				preparedstatement.setDouble(1, valor);
				preparedstatement.setString(2, nomSensor);
				// Executem la consulta
				preparedstatement.execute();

				// Tancar tots
				preparedstatement.close();
				return 1;
			}

		} catch (SQLException e) {

			e.printStackTrace();
			return -1;
		}

	}

}
