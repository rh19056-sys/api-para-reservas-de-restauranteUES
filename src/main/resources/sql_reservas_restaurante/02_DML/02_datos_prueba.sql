-- Datos mínimos adicionales para pruebas.
-- Mantener separado de los datos demo.
-- Los IDs continúan siendo autogenerados.

INSERT INTO reservas.preferencia_cliente (id_cuenta, tipo, detalle)
SELECT id_cuenta, 'ALERGIA', 'Sin maní'
FROM reservas.perfil_cliente
WHERE nombre = 'Cliente Demo';

INSERT INTO reservas.evento (id_sucursal, titulo, tipo, fecha_evento)
SELECT id_sucursal, 'Evento Demo', 'PRUEBA', CURRENT_TIMESTAMP + INTERVAL '7 days'
FROM reservas.restaurante_sucursal
WHERE direccion = 'San Salvador, El Salvador';
