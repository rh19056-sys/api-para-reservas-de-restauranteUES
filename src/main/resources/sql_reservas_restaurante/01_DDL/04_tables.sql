-- IDs de entidades: UUID autogenerado por PostgreSQL.
-- No se deben proporcionar manualmente en los INSERT normales.

CREATE TABLE reservas.cuenta (
    id_cuenta UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    password_hash VARCHAR(255) NOT NULL,
    estado_cuenta reservas.estado_cuenta NOT NULL DEFAULT 'PENDIENTE_VERIFICACION',
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMPTZ,
    fecha_verificacion TIMESTAMPTZ
);

CREATE TABLE reservas.perfil_cliente (
    id_cuenta UUID PRIMARY KEY
        REFERENCES reservas.cuenta(id_cuenta) ON DELETE CASCADE,
    nombre VARCHAR(150) NOT NULL
);

CREATE TABLE reservas.perfil_gestor (
    id_cuenta UUID PRIMARY KEY
        REFERENCES reservas.cuenta(id_cuenta) ON DELETE CASCADE,
    nombre VARCHAR(150) NOT NULL
);

CREATE TABLE reservas.correo (
    id_correo UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_cuenta UUID NOT NULL
        REFERENCES reservas.cuenta(id_cuenta) ON DELETE CASCADE,
    correo VARCHAR(255) NOT NULL,
    estado_contacto reservas.estado_contacto NOT NULL DEFAULT 'ACTIVO',
    es_principal BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_correo_valor UNIQUE (correo)
);

CREATE TABLE reservas.telefono (
    id_telefono UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_cuenta UUID NOT NULL
        REFERENCES reservas.cuenta(id_cuenta) ON DELETE CASCADE,
    numero_telefono VARCHAR(30) NOT NULL,
    estado_contacto reservas.estado_contacto NOT NULL DEFAULT 'ACTIVO',
    es_principal BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_telefono_valor UNIQUE (numero_telefono)
);

CREATE TABLE reservas.preferencia_cliente (
    id_preferencia UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_cuenta UUID NOT NULL
        REFERENCES reservas.perfil_cliente(id_cuenta) ON DELETE CASCADE,
    tipo VARCHAR(100) NOT NULL,
    detalle TEXT NOT NULL
);

CREATE TABLE reservas.restaurante_org (
    id_restaurante_org UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(200) NOT NULL,
    estado_organizacion reservas.estado_organizacion NOT NULL DEFAULT 'ACTIVA'
);

CREATE TABLE reservas.restaurante_sucursal (
    id_sucursal UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_restaurante_org UUID NOT NULL
        REFERENCES reservas.restaurante_org(id_restaurante_org) ON DELETE CASCADE,
    direccion VARCHAR(300) NOT NULL,
    capacidad INTEGER NOT NULL,
    tipo_cocina VARCHAR(100),
    estado_sucursal reservas.estado_sucursal NOT NULL DEFAULT 'ACTIVA',
    CONSTRAINT ck_sucursal_capacidad CHECK (capacidad > 0)
);

CREATE TABLE reservas.gestor_restaurante (
    id_cuenta UUID NOT NULL
        REFERENCES reservas.perfil_gestor(id_cuenta) ON DELETE CASCADE,
    id_sucursal UUID NOT NULL
        REFERENCES reservas.restaurante_sucursal(id_sucursal) ON DELETE CASCADE,
    estado_gestor reservas.estado_gestor NOT NULL DEFAULT 'ACTIVO',
    PRIMARY KEY (id_cuenta, id_sucursal)
);

CREATE TABLE reservas.mesa (
    id_mesa UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_sucursal UUID NOT NULL
        REFERENCES reservas.restaurante_sucursal(id_sucursal) ON DELETE CASCADE,
    numero INTEGER NOT NULL,
    capacidad INTEGER NOT NULL,
    ubicacion VARCHAR(100),
    estado_mesa reservas.estado_mesa NOT NULL DEFAULT 'DISPONIBLE',
    CONSTRAINT ck_mesa_numero CHECK (numero > 0),
    CONSTRAINT ck_mesa_capacidad CHECK (capacidad > 0),
    CONSTRAINT uq_mesa_sucursal_numero UNIQUE (id_sucursal, numero)
);

CREATE TABLE reservas.horario_restaurante (
    id_horario UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_sucursal UUID NOT NULL
        REFERENCES reservas.restaurante_sucursal(id_sucursal) ON DELETE CASCADE,
    dia_semana SMALLINT NOT NULL,
    hora_apertura TIME NOT NULL,
    hora_cierre TIME NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    CONSTRAINT ck_horario_dia CHECK (dia_semana BETWEEN 1 AND 7),
    CONSTRAINT ck_horario_horas CHECK (hora_cierre > hora_apertura),
    CONSTRAINT ck_horario_fechas CHECK (
        fecha_fin IS NULL OR fecha_inicio IS NULL OR fecha_fin >= fecha_inicio
    )
);

CREATE TABLE reservas.politica_reserva (
    id_politica UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_sucursal UUID NOT NULL
        REFERENCES reservas.restaurante_sucursal(id_sucursal) ON DELETE CASCADE,
    tipo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_inicio DATE,
    fecha_fin DATE,
    casos_aplicacion TEXT
);

CREATE TABLE reservas.menu (
    id_menu UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_sucursal UUID NOT NULL
        REFERENCES reservas.restaurante_sucursal(id_sucursal) ON DELETE CASCADE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    temporada VARCHAR(100),
    cantidad_personas INTEGER,
    precio NUMERIC(12,2) NOT NULL DEFAULT 0,
    fecha_inicio DATE,
    fecha_fin DATE,
    estado_menu reservas.estado_menu NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT ck_menu_precio CHECK (precio >= 0),
    CONSTRAINT ck_menu_personas CHECK (
        cantidad_personas IS NULL OR cantidad_personas > 0
    ),
    CONSTRAINT ck_menu_fechas CHECK (
        fecha_fin IS NULL OR fecha_inicio IS NULL OR fecha_fin >= fecha_inicio
    )
);

CREATE TABLE reservas.plato (
    id_plato UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_sucursal UUID NOT NULL
        REFERENCES reservas.restaurante_sucursal(id_sucursal) ON DELETE CASCADE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(12,2) NOT NULL DEFAULT 0,
    categoria VARCHAR(100),
    estado_producto reservas.estado_producto NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT ck_plato_precio CHECK (precio >= 0)
);

CREATE TABLE reservas.bebida (
    id_bebida UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_sucursal UUID NOT NULL
        REFERENCES reservas.restaurante_sucursal(id_sucursal) ON DELETE CASCADE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    estado_bebida reservas.estado_producto NOT NULL DEFAULT 'ACTIVO'
);

CREATE TABLE reservas.vino (
    id_bebida UUID PRIMARY KEY
        REFERENCES reservas.bebida(id_bebida) ON DELETE CASCADE,
    marca VARCHAR(150),
    stock INTEGER NOT NULL DEFAULT 0,
    condiciones_conservacion TEXT,
    anada INTEGER,
    CONSTRAINT ck_vino_stock CHECK (stock >= 0),
    CONSTRAINT ck_vino_anada CHECK (
        anada IS NULL OR anada BETWEEN 1800 AND 2100
    )
);

CREATE TABLE reservas.menu_plato (
    id_menu UUID NOT NULL
        REFERENCES reservas.menu(id_menu) ON DELETE CASCADE,
    id_plato UUID NOT NULL
        REFERENCES reservas.plato(id_plato) ON DELETE RESTRICT,
    cantidad INTEGER NOT NULL DEFAULT 1,
    orden INTEGER,
    PRIMARY KEY (id_menu, id_plato),
    CONSTRAINT ck_menu_plato_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_menu_plato_orden CHECK (orden IS NULL OR orden > 0)
);

CREATE TABLE reservas.menu_bebida (
    id_menu UUID NOT NULL
        REFERENCES reservas.menu(id_menu) ON DELETE CASCADE,
    id_bebida UUID NOT NULL
        REFERENCES reservas.bebida(id_bebida) ON DELETE RESTRICT,
    cantidad INTEGER NOT NULL DEFAULT 1,
    orden INTEGER,
    PRIMARY KEY (id_menu, id_bebida),
    CONSTRAINT ck_menu_bebida_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_menu_bebida_orden CHECK (orden IS NULL OR orden > 0)
);

CREATE TABLE reservas.evento (
    id_evento UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_sucursal UUID NOT NULL
        REFERENCES reservas.restaurante_sucursal(id_sucursal) ON DELETE CASCADE,
    titulo VARCHAR(200) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    fecha_evento TIMESTAMPTZ NOT NULL,
    estado_evento reservas.estado_evento NOT NULL DEFAULT 'PROGRAMADO'
);

CREATE TABLE reservas.evento_menu (
    id_evento UUID NOT NULL
        REFERENCES reservas.evento(id_evento) ON DELETE CASCADE,
    id_menu UUID NOT NULL
        REFERENCES reservas.menu(id_menu) ON DELETE RESTRICT,
    cantidad INTEGER NOT NULL DEFAULT 1,
    precio_unitario NUMERIC(12,2) NOT NULL DEFAULT 0,
    PRIMARY KEY (id_evento, id_menu),
    CONSTRAINT ck_evento_menu_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_evento_menu_precio CHECK (precio_unitario >= 0)
);

CREATE TABLE reservas.evento_plato (
    id_evento UUID NOT NULL
        REFERENCES reservas.evento(id_evento) ON DELETE CASCADE,
    id_plato UUID NOT NULL
        REFERENCES reservas.plato(id_plato) ON DELETE RESTRICT,
    cantidad INTEGER NOT NULL DEFAULT 1,
    precio_unitario NUMERIC(12,2) NOT NULL DEFAULT 0,
    PRIMARY KEY (id_evento, id_plato),
    CONSTRAINT ck_evento_plato_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_evento_plato_precio CHECK (precio_unitario >= 0)
);

CREATE TABLE reservas.evento_bebida (
    id_evento UUID NOT NULL
        REFERENCES reservas.evento(id_evento) ON DELETE CASCADE,
    id_bebida UUID NOT NULL
        REFERENCES reservas.bebida(id_bebida) ON DELETE RESTRICT,
    cantidad INTEGER NOT NULL DEFAULT 1,
    precio_unitario NUMERIC(12,2) NOT NULL DEFAULT 0,
    PRIMARY KEY (id_evento, id_bebida),
    CONSTRAINT ck_evento_bebida_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_evento_bebida_precio CHECK (precio_unitario >= 0)
);

CREATE TABLE reservas.reserva (
    id_reserva UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_cuenta_cliente UUID NOT NULL
        REFERENCES reservas.perfil_cliente(id_cuenta) ON DELETE RESTRICT,
    id_sucursal UUID NOT NULL
        REFERENCES reservas.restaurante_sucursal(id_sucursal) ON DELETE RESTRICT,
    id_politica UUID
        REFERENCES reservas.politica_reserva(id_politica) ON DELETE SET NULL,
    id_evento UUID
        REFERENCES reservas.evento(id_evento) ON DELETE SET NULL,
    numero_comensales INTEGER NOT NULL,
    hora_inicio TIMESTAMPTZ NOT NULL,
    hora_fin TIMESTAMPTZ NOT NULL,
    estado_reserva reservas.estado_reserva NOT NULL DEFAULT 'PENDIENTE',
    motivo_cancelacion TEXT,
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_reserva_comensales CHECK (numero_comensales > 0),
    CONSTRAINT ck_reserva_horas CHECK (hora_fin > hora_inicio)
);

CREATE TABLE reservas.reserva_mesa (
    id_reserva UUID NOT NULL
        REFERENCES reservas.reserva(id_reserva) ON DELETE CASCADE,
    id_mesa UUID NOT NULL
        REFERENCES reservas.mesa(id_mesa) ON DELETE RESTRICT,
    PRIMARY KEY (id_reserva, id_mesa)
);

CREATE TABLE reservas.reserva_plato (
    id_reserva UUID NOT NULL
        REFERENCES reservas.reserva(id_reserva) ON DELETE CASCADE,
    id_plato UUID NOT NULL
        REFERENCES reservas.plato(id_plato) ON DELETE RESTRICT,
    cantidad INTEGER NOT NULL,
    precio_unitario NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL,
    PRIMARY KEY (id_reserva, id_plato),
    CONSTRAINT ck_reserva_plato_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_reserva_plato_precios CHECK (
        precio_unitario >= 0 AND subtotal >= 0
    )
);

CREATE TABLE reservas.reserva_menu (
    id_reserva UUID NOT NULL
        REFERENCES reservas.reserva(id_reserva) ON DELETE CASCADE,
    id_menu UUID NOT NULL
        REFERENCES reservas.menu(id_menu) ON DELETE RESTRICT,
    cantidad INTEGER NOT NULL,
    precio_unitario NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL,
    PRIMARY KEY (id_reserva, id_menu),
    CONSTRAINT ck_reserva_menu_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_reserva_menu_precios CHECK (
        precio_unitario >= 0 AND subtotal >= 0
    )
);

CREATE TABLE reservas.reserva_bebida (
    id_reserva UUID NOT NULL
        REFERENCES reservas.reserva(id_reserva) ON DELETE CASCADE,
    id_bebida UUID NOT NULL
        REFERENCES reservas.bebida(id_bebida) ON DELETE RESTRICT,
    cantidad INTEGER NOT NULL,
    precio_unitario NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL,
    PRIMARY KEY (id_reserva, id_bebida),
    CONSTRAINT ck_reserva_bebida_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_reserva_bebida_precios CHECK (
        precio_unitario >= 0 AND subtotal >= 0
    )
);

CREATE TABLE reservas.pago (
    id_pago UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_reserva UUID NOT NULL
        REFERENCES reservas.reserva(id_reserva) ON DELETE RESTRICT,
    metodo_pago reservas.metodo_pago NOT NULL,
    monto NUMERIC(12,2) NOT NULL,
    estado_pago reservas.estado_pago NOT NULL DEFAULT 'PENDIENTE',
    fecha_pago TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_pago_monto CHECK (monto > 0)
);

CREATE TABLE reservas.comprobante_pago (
    id_comprobante UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_pago UUID NOT NULL UNIQUE
        REFERENCES reservas.pago(id_pago) ON DELETE CASCADE,
    numero_comprobante VARCHAR(100) NOT NULL UNIQUE,
    fecha_emision TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    formato reservas.formato_comprobante NOT NULL,
    referencia VARCHAR(255),
    desglose JSONB
);
