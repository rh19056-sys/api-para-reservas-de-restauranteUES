CREATE OR REPLACE VIEW reservas.vw_reservas_detalle AS
SELECT
    r.id_reserva,
    pc.nombre AS cliente,
    c.id_cuenta AS id_cuenta_cliente,
    ro.nombre AS restaurante,
    rs.id_sucursal,
    rs.direccion AS sucursal,
    r.numero_comensales,
    r.hora_inicio,
    r.hora_fin,
    r.estado_reserva,
    r.fecha_creacion
FROM reservas.reserva r
JOIN reservas.perfil_cliente pc
    ON pc.id_cuenta = r.id_cuenta_cliente
JOIN reservas.cuenta c
    ON c.id_cuenta = pc.id_cuenta
JOIN reservas.restaurante_sucursal rs
    ON rs.id_sucursal = r.id_sucursal
JOIN reservas.restaurante_org ro
    ON ro.id_restaurante_org = rs.id_restaurante_org;
