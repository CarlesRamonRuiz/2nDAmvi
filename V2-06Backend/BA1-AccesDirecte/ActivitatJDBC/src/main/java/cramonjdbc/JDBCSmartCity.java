package cramonjdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

	// 2
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
		try (Connection connection = DriverManager.getConnection(url, user, password)) {

			String query = "DELETE FROM lectures\n" + "WHERE qualitat = 'ERROR' AND data_lectura < ? ;";

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
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			String query = "DELETE FROM zones \r\n" + "WHERE id NOT IN (SELECT zona_id  FROM sensors);";

			PreparedStatement prepStatement = connection.prepareStatement(query);

			int linies = prepStatement.executeUpdate();

			System.out.println(linies);

			prepStatement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 3
	public static void sensorsSenseLectures() {
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			String query = "SELECT s.nom nomSensor, ts.nom nomTipusSensor, z.nom nomZona\r\n" + "FROM sensors s \r\n"
					+ "JOIN tipus_sensors ts ON s.tipus_sensor_id = ts.id\r\n" + "JOIN zones z ON s.zona_id = z.id\r\n"
					+ "WHERE s.id NOT IN (SELECT l.sensor_id  FROM lectures l) \r\n" + "ORDER BY z.id ,s.nom \r\n"
					+ ";";

			PreparedStatement prepStatement = connection.prepareStatement(query);

			ResultSet resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				String nomS = resultSet.getString(1);
				String nomTS = resultSet.getString(2);
				String nomZ = resultSet.getString(3);

				System.out.println("· Nom sensor: " + nomS + " Nom tipus de sensor: " + nomTS + " Nom de zona: " + nomZ);
			}
			resultSet.close();
			prepStatement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void lecturesDeZona(String nomZona, String qualitat) {
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			String query = "SELECT s.nom, ts.nom , l.valor , ts.unitat_mesura , l.data_lectura \r\n"
					+ "FROM sensors s \r\n"
					+ "JOIN tipus_sensors ts ON s.tipus_sensor_id = ts.id\r\n"
					+ "JOIN zones z  ON s.zona_id = z.id\r\n"
					+ "JOIN lectures l ON s.id = l.sensor_id\r\n"
					+ "WHERE z.nom = ? AND l.qualitat =?\r\n"
					+ "ORDER BY l.data_lectura desc\r\n"
					+ ";";

			PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.setString(1, nomZona);
			prepStatement.setString(2, qualitat);
			ResultSet resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				String nomS = resultSet.getString(1);
				String nomTS = resultSet.getString(2);
				double valor = resultSet.getDouble(3);
				String mesura = resultSet.getString(4);
				Date data = resultSet.getDate(5);
				

				System.out
						.println("· Nom sensor: " + nomS + " Nom tipus de sensor: " + nomTS + " Valor: "+valor+" Unitat de mesura: "+mesura+ " Data:"+data );
			}
			resultSet.close();
			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// 4
	public static void resumZones() {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String query = "SELECT z.nom , count(s.id ) nombreSensors, COUNT(s.actiu) nombreActius\r\n"
					+ "FROM zones z\r\n"
					+ "JOIN sensors s ON z.id =s.zona_id \r\n"
					+ "WHERE s.actiu = 1 AND z.id = s.zona_id \r\n"
					+ "GROUP BY z.nom\r\n"
					+ "ORDER BY nombresensors desc;";

			PreparedStatement prepStatement = connection.prepareStatement(query);

			ResultSet resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				String nomZ = resultSet.getString(1);
				int nombreSensors = resultSet.getInt(2);
				int nombreActius = resultSet.getInt(3);

				System.out.println("· Nom zona: " + nomZ + " Nombre de sensors: " + nombreSensors + " Nombre de sensors actius: " + nombreActius);
			}
			resultSet.close();
			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void estadistiquesPerTipus() {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String query = "SELECT ts.nom , MIN(l.valor ) valorMinim, MAX(l.valor ) valorMaxim, AVG(l.valor) valorAVG\r\n"
					+ "FROM tipus_sensors ts\r\n"
					+ "JOIN sensors s  ON ts.id   = s.tipus_sensor_id  \r\n"
					+ "JOIN lectures l ON s.id = l.sensor_id\r\n"
					+ "WHERE l.qualitat = 'OK'\r\n"
					+ "GROUP BY ts.nom\r\n"
					+ "HAVING COUNT(l.qualitat ='OK')>1\r\n"
					+ "ORDER BY valoravg desc\r\n"
					+ ";";

			PreparedStatement prepStatement = connection.prepareStatement(query);

			ResultSet resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				String nomTS = resultSet.getString(1);
				int valorMinim = resultSet.getInt(2);
				int valorMaxim = resultSet.getInt(3);
				int valorAVG = resultSet.getInt(4);

				System.out.println("· Nom Tipus sensor: " + nomTS + ", Valor Minim: " + valorMinim + ", Valor Màxim: " + valorMaxim+ ", Valor Mitjà: "+ valorAVG);
			}
			resultSet.close();
			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// 5
	public static void crearNovaTaula() {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String query = "CREATE TABLE IF NOT EXISTS manteniment (\r\n"
					+ "	\r\n"
					+ "	id int AUTO_INCREMENT PRIMARY KEY ,\r\n"
					+ "	sensor_id int NOT NULL,\r\n"
					+ "	data_intervencio date NOT null,\r\n"
					+ "	tecnic varchar(100) NOT NULL,\r\n"
					+ "	tipus enum('PREVENTIU','CORRECTIU') NOT NULL ,\r\n"
					+ "	cost Decimal(8,2) NOT NULL ,\r\n"
					+ "	descripcio varchar(255),\r\n"
					+ "	FOREIGN KEY (sensor_id) REFERENCES sensors(id)\r\n"
					+ "\r\n"
					+ ");";

			PreparedStatement prepStatement = connection.prepareStatement(query);
			
			System.out.println("Taula creada.");

			
			prepStatement.close();
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}
	public static void inserirManteniment(int id_sensor, LocalDate data,String tecnic, String tipus, int cost, String desc) {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String query = "INSERT INTO ciutat_sensors.manteniment\r\n"
					+ "(sensor_id, data_intervencio, tecnic, tipus, cost, descripcio)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?);";

			PreparedStatement prepStatement = connection.prepareStatement(query);
			
			String dataS = data.toString() ;
			
			prepStatement.setInt(1, id_sensor);
			prepStatement.setString(2, dataS);
			prepStatement.setString(3, tecnic);
			prepStatement.setString(4, tipus);
			prepStatement.setInt(5, cost);
			prepStatement.setString(6, desc);
			
			int linies = prepStatement.executeUpdate();
			
			System.out.println("Linies inserides: "+linies);
			
			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//6
	public static void ultim_manteniment () {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String query = "ALTER TABLE sensors\r\n"
					+ "ADD column ultim_manteniment Date ;";

			PreparedStatement prepStatement = connection.prepareStatement(query);

			
			prepStatement.executeUpdate();
			
			System.out.println("Columna creada");
			
			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void afegir_unique() {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String query = "ALTER TABLE sensors\r\n"
					+ "MODIFY nom varchar(100) UNIQUE;";

			PreparedStatement prepStatement = connection.prepareStatement(query);

			
			prepStatement.executeUpdate();
			
			System.out.println("Columna modificada");
			
			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void afegirDuplicat() {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String query = "INSERT INTO ciutat_sensors.sensors\r\n"
					+ "(nom, zona_id, tipus_sensor_id, pos_x, pos_y, altura, orientacio, actiu, data_instalacio, ultim_manteniment)\r\n"
					+ "VALUES('TEMP-CENTRE-01', 1, 1, 100, 200, 3.00, 1, 1, CURRENT_DATE(), NULL);";

			PreparedStatement prepStatement = connection.prepareStatement(query);

			
			prepStatement.executeUpdate();
			
			System.out.println("Columna modificada");
			
			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//7
	public static void cercaSensors(String text, Integer idTipus, Boolean nomesActius) {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String query = "SELECT nom  FROM sensors\r\n"
					+ "WHERE (? IS NULL OR actiu = ?) \r\n"
					+ "AND (? IS NULL OR nom LIKE ?) \r\n"
					+ "AND (? IS NULL OR tipus_sensor_id =?);";

			PreparedStatement prepStatement = connection.prepareStatement(query);
			
			prepStatement.setBoolean(1, nomesActius);
			prepStatement.setBoolean(2, nomesActius);
			prepStatement.setString(3, text);
			prepStatement.setString(4, "%"+text+"%");
			prepStatement.setInt(5, idTipus);
			prepStatement.setInt(6, idTipus);
			
			ResultSet resultSet = prepStatement.executeQuery();
			
			while (resultSet.next()) {
				String nomS = resultSet.getString(1);
				System.out.println("Nom de Sensor: "+nomS);
			}
			
			
			
			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void lecturesEntreDates(int idSensor, LocalDateTime inici, LocalDateTime fi) {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String query = "SELECT s.nom , sum((CASE WHEN l.qualitat = 'OK' THEN 1 ELSE 0 END)) qOk, Sum((CASE WHEN l.qualitat = 'ERROR' THEN 1 ELSE 0 END)) qError, Sum((CASE WHEN l.qualitat = 'DUBTOSA' THEN 1 ELSE 0 END)) qDubtosa  FROM  lectures l\r\n"
					+ "JOIN sensors s ON l.sensor_id = s.id\r\n"
					+ "WHERE l.data_lectura BETWEEN ? AND ? AND l.sensor_id = ? \r\n"
					+ "GROUP BY s.nom \r\n"
					+ ";";

			PreparedStatement prepStatement = connection.prepareStatement(query);
			String iniciS = inici.toString();
			String fiS = fi.toString();
			prepStatement.setString(1, iniciS);
			prepStatement.setString(2, fiS);
			prepStatement.setInt(3, idSensor);
			

			
			ResultSet resultSet = prepStatement.executeQuery();
			
			while (resultSet.next()) {
				String nomS = resultSet.getString(1);
				System.out.println("Nom de Sensor: "+nomS);
			}
			
			
			resultSet.close();
			prepStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//8

}
