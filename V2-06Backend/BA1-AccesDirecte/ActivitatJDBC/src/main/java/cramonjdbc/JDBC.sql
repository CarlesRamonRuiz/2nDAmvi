USE ciutat_sensors;

SELECT s.nom, ts.nom , l.valor , ts.unitat_mesura , l.data_lectura 
FROM zones z
JOIN sensors s  ON z.id  = s.zona_id 
JOIN tipus_sensors ts ON s.tipus_sensor_id = ts.id
JOIN lectures l ON s.id = l.sensor_id
WHERE z.nom = "Centre" AND l.qualitat = "OK"
ORDER BY l.data_lectura desc
;

SELECT z.nom , count(s.id ) nombreSensors, COUNT(s.actiu) nombreActius
FROM zones z
JOIN sensors s ON z.id =s.zona_id 
WHERE s.actiu = 1 AND z.id = s.zona_id 
GROUP BY z.nom
ORDER BY nombresensors desc;

SELECT ts.nom , MIN(l.valor ) valorMinim, MAX(l.valor ) valorMaxim, AVG(l.valor) valorAVG
FROM tipus_sensors ts
JOIN sensors s  ON ts.id   = s.tipus_sensor_id  
JOIN lectures l ON s.id = l.sensor_id
WHERE l.qualitat = 'OK'
GROUP BY ts.nom
HAVING COUNT(l.qualitat ='OK')>1
ORDER BY valoravg desc
;
CREATE TABLE IF NOT EXISTS manteniment (
	
	id int AUTO_INCREMENT PRIMARY KEY ,
	sensor_id int NOT NULL,
	data_intervencio date NOT null,
	tecnic varchar(100) NOT NULL,
	tipus enum('PREVENTIU','CORRECTIU') NOT NULL ,
	cost Decimal(8,2) NOT NULL ,
	descripcio varchar(255),
	FOREIGN KEY (sensor_id) REFERENCES sensors(id)

);
INSERT INTO ciutat_sensors.manteniment
(sensor_id, data_intervencio, tecnic, tipus, cost, descripcio)
VALUES(?, ?, ?, ?, ?, ?);

ALTER TABLE sensors
ADD column ultim_manteniment Date ;
ALTER TABLE sensors
drop column ultim_manteniment;

ALTER TABLE sensors
MODIFY nom varchar(100) UNIQUE;

INSERT INTO ciutat_sensors.sensors
(nom, zona_id, tipus_sensor_id, pos_x, pos_y, altura, orientacio, actiu, data_instalacio, ultim_manteniment)
VALUES('TEMP-CENTRE-01', 1, 1, 100, 200, 3.00, 1, 1, CURRENT_DATE(), NULL);

SELECT nom  FROM sensors
WHERE (? IS NULL OR actiu = ?) 
AND (? IS NULL OR nom LIKE ?) 
AND (? IS NULL OR tipus_sensor_id =?) ;

SELECT s.nom , sum((CASE WHEN l.qualitat = 'OK' THEN 1 ELSE 0 END)) qOk, Sum((CASE WHEN l.qualitat = 'ERROR' THEN 1 ELSE 0 END)) qError, Sum((CASE WHEN l.qualitat = 'DUBTOSA' THEN 1 ELSE 0 END)) qDubtosa  FROM  lectures l
JOIN sensors s ON l.sensor_id = s.id
WHERE l.data_lectura BETWEEN ? AND ? AND l.sensor_id 
GROUP BY s.nom 
;

