-- DATOS DEMO
-- Los UUID NO se especifican: PostgreSQL los genera.
-- Este script obtiene las claves generadas mediante consultas por datos
-- únicos, por lo que sirve como carga inicial de desarrollo.

BEGIN;

INSERT INTO reservas.cuenta (password_hash, estado_cuenta, fecha_verificacion)
VALUES ('DEMO_HASH_CLIENTE', 'ACTIVA', CURRENT_TIMESTAMP);

INSERT INTO reservas.cuenta (password_hash, estado_cuenta, fecha_verificacion)
VALUES ('DEMO_HASH_GESTOR', 'ACTIVA', CURRENT_TIMESTAMP);

INSERT INTO reservas.perfil_cliente (id_cuenta, nombre)
SELECT id_cuenta, 'Cliente Demo'
FROM reservas.cuenta
WHERE password_hash = 'DEMO_HASH_CLIENTE';

INSERT INTO reservas.perfil_gestor (id_cuenta, nombre)
SELECT id_cuenta, 'Gestor Demo'
FROM reservas.cuenta
WHERE password_hash = 'DEMO_HASH_GESTOR';

INSERT INTO reservas.correo (id_cuenta, correo, es_principal)
SELECT id_cuenta, 'cliente.demo@demo.local', TRUE
FROM reservas.cuenta
WHERE password_hash = 'DEMO_HASH_CLIENTE';

INSERT INTO reservas.correo (id_cuenta, correo, es_principal)
SELECT id_cuenta, 'gestor.demo@demo.local', TRUE
FROM reservas.cuenta
WHERE password_hash = 'DEMO_HASH_GESTOR';

INSERT INTO reservas.telefono (id_cuenta, numero_telefono, es_principal)
SELECT id_cuenta, '70000001', TRUE
FROM reservas.cuenta
WHERE password_hash = 'DEMO_HASH_CLIENTE';

INSERT INTO reservas.restaurante_org (nombre)
VALUES ('Restaurante Demo');

INSERT INTO reservas.restaurante_sucursal
(id_restaurante_org, direccion, capacidad, tipo_cocina)
SELECT id_restaurante_org, 'San Salvador, El Salvador', 60, 'Internacional'
FROM reservas.restaurante_org
WHERE nombre = 'Restaurante Demo';

INSERT INTO reservas.gestor_restaurante (id_cuenta, id_sucursal)
SELECT pg.id_cuenta, rs.id_sucursal
FROM reservas.perfil_gestor pg
CROSS JOIN reservas.restaurante_sucursal rs
WHERE pg.nombre = 'Gestor Demo'
  AND rs.direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.mesa
(id_sucursal, numero, capacidad, ubicacion)
SELECT id_sucursal, 1, 2, 'Salón principal'
FROM reservas.restaurante_sucursal
WHERE direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.mesa
(id_sucursal, numero, capacidad, ubicacion)
SELECT id_sucursal, 2, 4, 'Salón principal'
FROM reservas.restaurante_sucursal
WHERE direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.mesa
(id_sucursal, numero, capacidad, ubicacion)
SELECT id_sucursal, 3, 6, 'Terraza'
FROM reservas.restaurante_sucursal
WHERE direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.horario_restaurante
(id_sucursal, dia_semana, hora_apertura, hora_cierre)
SELECT id_sucursal, d, '10:00', '22:00'
FROM reservas.restaurante_sucursal
CROSS JOIN generate_series(1,7) AS d
WHERE direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.politica_reserva
(id_sucursal, tipo, descripcion, casos_aplicacion)
SELECT id_sucursal, 'DURACION_ESTANDAR',
       'Duración estándar de reserva para desarrollo.',
       'Reservas generales'
FROM reservas.restaurante_sucursal
WHERE direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.plato
(id_sucursal, nombre, descripcion, precio, categoria)
SELECT id_sucursal, 'Pasta Demo', 'Plato de demostración', 8.50, 'Principal'
FROM reservas.restaurante_sucursal
WHERE direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.plato
(id_sucursal, nombre, descripcion, precio, categoria)
SELECT id_sucursal, 'Ensalada Demo', 'Ensalada de demostración', 5.00, 'Entrada'
FROM reservas.restaurante_sucursal
WHERE direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.bebida
(id_sucursal, nombre, descripcion)
SELECT id_sucursal, 'Bebida Demo', 'Bebida de demostración'
FROM reservas.restaurante_sucursal
WHERE direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.menu
(id_sucursal, nombre, descripcion, temporada, cantidad_personas, precio)
SELECT id_sucursal, 'Menú Demo', 'Menú para pruebas de API', 'General', 1, 13.50
FROM reservas.restaurante_sucursal
WHERE direccion = 'San Salvador, El Salvador';

INSERT INTO reservas.menu_plato (id_menu, id_plato, cantidad, orden)
SELECT m.id_menu, p.id_plato, 1, 1
FROM reservas.menu m
JOIN reservas.plato p ON p.nombre = 'Pasta Demo'
WHERE m.nombre = 'Menú Demo';

INSERT INTO reservas.menu_bebida (id_menu, id_bebida, cantidad, orden)
SELECT m.id_menu, b.id_bebida, 1, 2
FROM reservas.menu m
JOIN reservas.bebida b ON b.nombre = 'Bebida Demo'
WHERE m.nombre = 'Menú Demo';

COMMIT;
