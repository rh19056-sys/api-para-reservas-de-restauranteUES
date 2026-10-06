-- Pruebas de transacciones PostgreSQL.
-- Ejecutar después del DDL y DML.

BEGIN;

INSERT INTO reservas.restaurante_org (nombre)
VALUES ('Restaurante TX OK');

COMMIT;

BEGIN;

INSERT INTO reservas.restaurante_org (nombre)
VALUES ('Restaurante TX ROLLBACK');

ROLLBACK;

BEGIN;

INSERT INTO reservas.restaurante_org (nombre)
VALUES ('Restaurante TX SAVEPOINT');

SAVEPOINT punto_prueba;

INSERT INTO reservas.restaurante_org (nombre)
VALUES ('Registro que será revertido');

ROLLBACK TO SAVEPOINT punto_prueba;

COMMIT;

SELECT id_restaurante_org, nombre
FROM reservas.restaurante_org
WHERE nombre LIKE 'Restaurante TX%';
