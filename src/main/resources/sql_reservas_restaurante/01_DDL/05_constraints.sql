-- Restricciones adicionales de integridad.
-- La lógica de negocio compleja se mantiene en la API.

CREATE UNIQUE INDEX uq_correo_principal_por_cuenta
    ON reservas.correo(id_cuenta)
    WHERE es_principal = TRUE;

    --Queremos permitir varios correos, pero que a la vez solo uno pueda
    --ser principal.

ALTER TABLE reservas.reserva_mesa
    ADD CONSTRAINT uq_reserva_mesa_reserva
    UNIQUE (id_reserva, id_mesa);

CREATE INDEX idx_correo_cuenta ON reservas.correo(id_cuenta);
CREATE INDEX idx_telefono_cuenta ON reservas.telefono(id_cuenta);
CREATE INDEX idx_mesa_sucursal ON reservas.mesa(id_sucursal);
CREATE INDEX idx_horario_sucursal ON reservas.horario_restaurante(id_sucursal);
CREATE INDEX idx_politica_sucursal ON reservas.politica_reserva(id_sucursal);
CREATE INDEX idx_menu_sucursal ON reservas.menu(id_sucursal);
CREATE INDEX idx_plato_sucursal ON reservas.plato(id_sucursal);
CREATE INDEX idx_bebida_sucursal ON reservas.bebida(id_sucursal);
CREATE INDEX idx_evento_sucursal ON reservas.evento(id_sucursal);
CREATE INDEX idx_reserva_cliente ON reservas.reserva(id_cuenta_cliente);
CREATE INDEX idx_reserva_sucursal ON reservas.reserva(id_sucursal);
CREATE INDEX idx_reserva_evento ON reservas.reserva(id_evento);
CREATE INDEX idx_pago_reserva ON reservas.pago(id_reserva);
