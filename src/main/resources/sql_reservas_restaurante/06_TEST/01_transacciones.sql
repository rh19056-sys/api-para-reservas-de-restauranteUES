-- Pruebas de transacciones PostgreSQL.
-- Ejecutar después del DDL y DML.

BEGIN;

INSERT INTO reservas.restaurante_org (nombre, direccion)
VALUES ('Restaurante TX OK', 'Dirección de prueba TX');

COMMIT;

BEGIN;

INSERT INTO reservas.restaurante_org (nombre, direccion)
VALUES ('Restaurante TX ROLLBACK', 'Dirección de prueba TX');

ROLLBACK;

BEGIN;

INSERT INTO reservas.restaurante_org (nombre, direccion)
VALUES ('Restaurante TX SAVEPOINT', 'Dirección de prueba TX');

SAVEPOINT punto_prueba;

INSERT INTO reservas.restaurante_org (nombre, direccion)
VALUES ('Registro que será revertido', 'Dirección de prueba TX');

ROLLBACK TO SAVEPOINT punto_prueba;

COMMIT;

SELECT id_restaurante_org, nombre
FROM reservas.restaurante_org
WHERE nombre LIKE 'Restaurante TX%';
