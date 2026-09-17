package cramonjdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
				flag1 = true;
			} else if (resultset.getBoolean("actiu") == false) {
				System.out.println("Aquest sensor esta inactiu");
				flag2 = true;
			}

			if (flag1) {
				return -1;
			} else if (flag2) {
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

				// Contem les linees que s'han insertat
				int lineesLlegides = preparedstatement.executeUpdate();

				// Tancar tots
				resultset.close();
				statement.close();
				preparedstatement.close();
				System.out.println("Linies llegides: " + lineesLlegides);
				return lineesLlegides;
			}

		} catch (SQLException e) {

			e.printStackTrace();
			return -1;
		}

	}

	public static void fitxaSensor(int idSensor) {
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			String select = "SELECT s.nom , ts.nom ,z.nom , s.actiu ,count(l.sensor_id) totalLectures, MAX(l.data_lectura) ultimaLectura\n"
					+ "FROM sensors s\n" + "JOIN tipus_sensors ts ON ts.id =s.tipus_sensor_id \n"
					+ "JOIN zones z ON s.zona_id = z.id\n" + "JOIN lectures l ON s.id = l.sensor_id\n"
					+ "WHERE s.id = ?\n" + "GROUP BY s.nom ,ts.nom ,z.nom ,s.actiu;";

			PreparedStatement prepStatement = connection.prepareStatement(select);

			prepStatement.setInt(1, idSensor);

			ResultSet resultset = prepStatement.executeQuery();

			while (resultset.next()) {
				String nom = resultset.getString("s.nom");
				String tsnom = resultset.getString("ts.nom");
				String znom = resultset.getString("z.nom");
				boolean estat = resultset.getBoolean("actiu");
				int totalLectures = resultset.getInt("totalLectures");
				Date ultimaLectura = resultset.getDate("ultimaLectura");
				System.out.println("Nom Sensor: " + nom + "\n Nom de tipus de Sensor: " + tsnom + " Nom de zona:" + znom
						+ " Estat:" + estat + " Numero de lectures:" + totalLectures + " Ultima lectura:"
						+ ultimaLectura);

			}
			prepStatement.close();
			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void marcaDubtoses(int idSensor, double min, double max) {
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			String query = "UPDATE lectures l \n" + "JOIN sensors s ON l.sensor_id = s.id \n"
					+ "SET l.qualitat = 'DUBTOSA'\n"
					+ "WHERE s.id = ? AND l.valor < ? AND l.valor > ? and l.qualitat NOT LIKE 'DUBTOSA' AND l.qualitat NOT LIKE 'ERROR';";

			PreparedStatement prepStatement = connection.prepareStatement(query);

			prepStatement.setInt(1, idSensor);
			prepStatement.setDouble(2, min);
			prepStatement.setDouble(3, max);

			int linies = prepStatement.executeUpdate();

			System.out.println(linies);

			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void desactivaSensorsSilenciosos(LocalDate data) {
		try (Connection connection = DriverManager.getConnection(url, user, password)) {

			String query = "UPDATE sensors s \n" + "JOIN lectures l ON l.sensor_id = s.id \n" + "SET actiu=0\n"
					+ "WHERE l.data_lectura < ? and l.sensor_id = s.id \n" + ";\n" + "";

			PreparedStatement prepStatement = connection.prepareStatement(query);

			String dataS = data.toString();
			
			
			prepStatement.setString(1, dataS);

			int linies = prepStatement.executeUpdate();

			System.out.println(linies);

			prepStatement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void netejaLecturesError(LocalDate data) {
		try(Connection connection = DriverManager.getConnection(url, user, password)) {
			
			String query = "DELETE FROM lectures\n"
					+ "WHERE qualitat = 'ERROR' AND data_lectura < ? ;";
			
			PreparedStatement prepStatement = connection.prepareStatement(query);
			
			String dataS = data.toString();
			
			
			prepStatement.setString(1, dataS);
			
			int linies = prepStatement.executeUpdate();

			System.out.println(linies);

			prepStatement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void eliminaZonesBuides() {
		
	}
	
	
	
}

