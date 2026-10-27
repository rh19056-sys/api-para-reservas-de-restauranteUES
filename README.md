<h1 align="center">
  API PARA RESERVAS DE RESTAURANTES UES
</h1>

<p align="center">
  <strong>
    Sistema backend para la gestión integral de reservas de restaurantes
  </strong>
</p>

<p align="center">
  Gestión de clientes · Restaurantes · Sucursales · Mesas · Menús · Eventos · Pagos
</p>

<br>

<p align="center">
  <img src="https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white&labelColor=ED8B00&color=ED8B00&scale=2" height="60" alt="Java 25">
  <img src="https://img.shields.io/badge/PostgreSQL_17-4169E1?style=for-the-badge&logo=postgresql&logoColor=white&scale=2" height="60" alt="PostgreSQL 17">
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white&scale=2" height="60" alt="Maven">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white&scale=2" height="60" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Postman-F76935?style=for-the-badge&logo=postman&logoColor=white&scale=2" height="60" alt="Postman">
  <img src="https://img.shields.io/badge/JSON-333333?style=for-the-badge&logo=json&logoColor=white&scale=2" height="60" alt="JSON">
  <img src="https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white&scale=2" height="60" alt="JUnit 5">
  <img src="https://img.shields.io/badge/Mockito-78A641?style=for-the-badge&scale=2" height="60" alt="Mockito">
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white&scale=2" height="60" alt="Git">
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white&scale=2" height="60" alt="GitHub">
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-7B61FF?style=for-the-badge&logo=intellijidea&logoColor=white&scale=2" height="60" alt="IntelliJ IDEA">
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white&scale=2" height="60" alt="HTML5">
</p>
<br>

<hr>

**API para Reservas de Restaurante UES** es un sistema backend orientado a la gestión de reservas en restaurantes.

El sistema permite modelar y administrar diferentes elementos del proceso de reserva, incluyendo:

- Cuentas de clientes y gestores.
- Información de contacto.
- Organizaciones y sucursales de restaurantes.
- Mesas y horarios.
- Políticas de reserva.
- Reservas.
- Menús, platos y bebidas.
- Eventos.
- Pagos y comprobantes.
- Preferencias de clientes.

El proyecto se desarrolla inicialmente para ejecución local, utilizando PostgreSQL como sistema de persistencia y Postman como herramienta principal para realizar y probar solicitudes HTTP contra la API.

---

# 2. Objetivo

El objetivo del proyecto es desarrollar una API REST capaz de proporcionar una base organizada y escalable para la gestión de reservas de restaurantes.

El sistema contempla principalmente dos perfiles:

##  Cliente

Permite realizar operaciones relacionadas con:

- Creación de cuenta.
- Autenticación.
- Gestión de datos de contacto.
- Consulta de restaurantes y sucursales.
- Creación de reservas.
- Selección de mesas.
- Selección de menús.
- Selección de platos y bebidas.
- Consulta y cancelación de reservas.
- Registro de preferencias.
- Realización de pagos.
- Obtención de comprobantes.

##  Gestor

Permite administrar elementos relacionados con una o varias sucursales:

- Sucursales.
- Mesas.
- Horarios.
- Menús.
- Políticas de reserva.
- Eventos.
- Platos.
- Bebidas.

---

# 3. Mapa del proyecto

## 3.1 Mapa general del sistema

```text
                         ENTORNO LOCAL
                              │
             ┌────────────────┴────────────────┐
             │                                 │
             ▼                                 ▼
      ┌─────────────┐                   ┌─────────────┐
      │   Postman   │                   │    JSON     │
      │ HTTP Client │                   │  Request    │
      └──────┬──────┘                   └──────┬──────┘
             │                                 │
             └──────────────┬──────────────────┘
                            │
                       HTTP Request
                            │
                            ▼
                 ┌─────────────────────┐
                 │      API JAVA       │
                 │    localhost:8081   │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │     Controller      │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │       Service       │
                 │   Lógica de negocio │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │     Repository      │
                 └──────────┬──────────┘
                            │
                           JDBC
                            │
                            ▼
                 ┌─────────────────────┐
                 │     PostgreSQL      │
                 │    localhost:5432   │
                 └─────────────────────┘
```

## 3.2 Flujo de una solicitud

```text
Postman
   │
   │ HTTP Request
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   │ JDBC
   ▼
PostgreSQL
   │
   │ Resultado
   ▼
Repository
   │
   ▼
Service
   │
   ▼
Controller
   │
   │ JSON Response
   ▼
Postman
```

---

# 4. Estructura actual del proyecto

```text
api-para-reservas-de-restauranteUES/
│
├── [CONTROL DE VERSIONES Y CONFIGURACIÓN]
│   │
│   ├── .git/
│   ├── .idea/
│   ├── .gitignore
│   ├── pom.xml
│   └── README.md
│
├── [DOCUMENTACIÓN DEL DISEÑO]
│   │
│   ├── Diagrama_casos_de_uso.html
│   ├── Diagrama_de_Clases.md
│   ├── POO_DER.html
│   └── POO_UML.html
│
└── src/
    │
    ├── main/
    │   │
    │   ├── java/sv/edu/ues/reservas/
    │   │   │
    │   │   ├── ApiParaReservasDeRestauranteUesApplication.java
    │   │   │
    │   │   ├── config/
    │   │   │   └── PasswordConfig.java
    │   │   │
    │   │   ├── controller/
    │   │   │   ├── CuentaController.java
    │   │   │   ├── CorreoController.java
    │   │   │   ├── TelefonoController.java
    │   │   │   ├── PerfilClienteController.java
    │   │   │   ├── PerfilGestorController.java
    │   │   │   ├── PreferenciaClienteController.java
    │   │   │   ├── RestauranteOrgController.java
    │   │   │   ├── RestauranteSucursalController.java
    │   │   │   ├── ReservaController.java
    │   │   │   ├── EventoController.java
    │   │   │   ├── MenuController.java
    │   │   │   ├── PlatoController.java
    │   │   │   └── BebidaController.java
    │   │   │
    │   │   ├── domain/
    │   │   │   │
    │   │   │   ├── entity/
    │   │   │   │   │
    │   │   │   │   ├── CUENTAS Y PERFILES
    │   │   │   │   │   ├── Cuenta.java
    │   │   │   │   │   ├── Correo.java
    │   │   │   │   │   ├── Telefono.java
    │   │   │   │   │   ├── PerfilCliente.java
    │   │   │   │   │   ├── PerfilGestor.java
    │   │   │   │   │   └── PreferenciaCliente.java
    │   │   │   │   │
    │   │   │   │   ├── RESTAURANTES
    │   │   │   │   │   ├── RestauranteOrg.java
    │   │   │   │   │   └── RestauranteSucursal.java
    │   │   │   │   │
    │   │   │   │   ├── RESERVAS Y MESAS
    │   │   │   │   │   ├── Reserva.java
    │   │   │   │   │   ├── ReservaMesa.java
    │   │   │   │   │   ├── ReservaMesaId.java
    │   │   │   │   ├── Mesa.java
    │   │   │   │   └── PoliticaReserva.java
    │   │   │   │   │
    │   │   │   │   ├── CATÁLOGO
    │   │   │   │   │   ├── Menu.java
    │   │   │   │   │   ├── Plato.java
    │   │   │   │   │   ├── Bebida.java
    │   │   │   │   │   ├── Vino.java
    │   │   │   │   │   ├── MenuPlato.java
    │   │   │   │   │   ├── MenuPlatoId.java
    │   │   │   │   │   ├── MenuBebida.java
    │   │   │   │   │   └── MenuBebidaId.java
    │   │   │   │   │
    │   │   │   │   └── EVENTOS
    │   │   │   │       ├── Evento.java
    │   │   │   │       ├── EventoPlato.java
    │   │   │   │       ├── EventoPlatoId.java
    │   │   │   │       ├── EventoBebida.java
    │   │   │   │       ├── EventoBebidaId.java
    │   │   │   │       ├── EventoMenu.java
    │   │   │   │       └── EventoMenuId.java
    │   │   │   │
    │   │   │   └── enums/
    │   │   │       ├── EstadoContacto.java
    │   │   │       ├── EstadoCuenta.java
    │   │   │       ├── EstadoEvento.java
    │   │   │       ├── EstadoMenu.java
    │   │   │       ├── EstadoOrganizacion.java
    │   │   │       ├── EstadoProducto.java
    │   │   │       ├── EstadoReserva.java
    │   │   │       └── EstadoSucursal.java
    │   │   │
    │   │   ├── dto/
    │   │   │   │
    │   │   │   ├── cuenta/
    │   │   │   │   ├── CuentaCrearRequest.java
    │   │   │   │   ├── CuentaActualizarRequest.java
    │   │   │   │   └── CuentaResponse.java
    │   │   │   │
    │   │   │   ├── correo/
    │   │   │   │   ├── CorreoCrearRequest.java
    │   │   │   │   ├── CorreoActualizarRequest.java
    │   │   │   │   └── CorreoResponse.java
    │   │   │   │
    │   │   │   ├── telefono/
    │   │   │   │   ├── TelefonoCrearRequest.java
    │   │   │   │   ├── TelefonoActualizarRequest.java
    │   │   │   │   └── TelefonoResponse.java
    │   │   │   │
    │   │   │   ├── perfilcliente/
    │   │   │   │   ├── PerfilClienteCrearRequest.java
    │   │   │   │   ├── PerfilClienteActualizarRequest.java
    │   │   │   │   └── PerfilClienteResponse.java
    │   │   │   │
    │   │   │   ├── perfilgestor/
    │   │   │   │   ├── PerfilGestorCrearRequest.java
    │   │   │   │   ├── PerfilGestorActualizarRequest.java
    │   │   │   │   └── PerfilGestorResponse.java
    │   │   │   │
    │   │   │   ├── preferenciacliente/
    │   │   │   │   ├── PreferenciaClienteCrearRequest.java
    │   │   │   │   ├── PreferenciaClienteActualizarRequest.java
    │   │   │   │   └── PreferenciaClienteResponse.java
    │   │   │   │
    │   │   │   ├── restaurante/
    │   │   │   │   ├── RestauranteOrgCrearRequest.java
    │   │   │   │   ├── RestauranteOrgActualizarRequest.java
    │   │   │   │   └── RestauranteOrgResponse.java
    │   │   │   │
    │   │   │   ├── restaurantesucursal/
    │   │   │   │   ├── RestauranteSucursalCrearRequest.java
    │   │   │   │   ├── RestauranteSucursalActualizarRequest.java
    │   │   │   │   └── RestauranteSucursalResponse.java
    │   │   │   │
    │   │   │   ├── reserva/
    │   │   │   │   ├── ReservaCrearRequest.java
    │   │   │   │   ├── ReservaActualizarRequest.java
    │   │   │   │   └── ReservaResponse.java
    │   │   │   │
    │   │   │   ├── evento/
    │   │   │   │   ├── EventoCrearRequest.java
    │   │   │   │   ├── EventoActualizarRequest.java
    │   │   │   │   ├── EventoItemRequest.java
    │   │   │   │   ├── EventoItemResponse.java
    │   │   │   │   └── EventoResponse.java
    │   │   │   │
    │   │   │   └── catalogo/
    │   │   │       ├── MenuCrearRequest.java
    │   │   │       ├── MenuActualizarRequest.java
    │   │   │       ├── MenuResponse.java
    │   │   │       ├── MenuPlatoRequest.java
    │   │   │       ├── MenuPlatoResponse.java
    │   │   │       ├── MenuBebidaRequest.java
    │   │   │       ├── MenuBebidaResponse.java
    │   │   │       ├── PlatoCrearRequest.java
    │   │   │       ├── PlatoActualizarRequest.java
    │   │   │       ├── PlatoResponse.java
    │   │   │       ├── BebidaCrearRequest.java
    │   │   │       ├── BebidaActualizarRequest.java
    │   │   │       ├── BebidaResponse.java
    │   │   │       ├── VinoCrearRequest.java
    │   │   │       ├── VinoActualizarRequest.java
    │   │   │       └── VinoResponse.java
    │   │   │
    │   │   ├── exception/
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   └── RecursoNoEncontradoException.java
    │   │   │
    │   │   ├── repository/
    │   │   │   ├── CuentaRepository.java
    │   │   │   ├── CorreoRepository.java
    │   │   │   ├── TelefonoRepository.java
    │   │   │   ├── PerfilClienteRepository.java
    │   │   │   ├── PerfilGestorRepository.java
    │   │   │   ├── PreferenciaClienteRepository.java
    │   │   │   ├── RestauranteOrgRepository.java
    │   │   │   ├── RestauranteSucursalRepository.java
    │   │   │   ├── ReservaRepository.java
    │   │   │   ├── ReservaMesaRepository.java
    │   │   │   ├── MesaRepository.java
    │   │   │   ├── PoliticaReservaRepository.java
    │   │   │   ├── MenuRepository.java
    │   │   │   ├── MenuPlatoRepository.java
    │   │   │   ├── MenuBebidaRepository.java
    │   │   │   ├── PlatoRepository.java
    │   │   │   ├── BebidaRepository.java
    │   │   │   ├── VinoRepository.java
    │   │   │   ├── EventoRepository.java
    │   │   │   ├── EventoPlatoRepository.java
    │   │   │   ├── EventoBebidaRepository.java
    │   │   │   └── EventoMenuRepository.java
    │   │   │
    │   │   └── service/
    │   │       ├── CuentaService.java
    │   │       ├── CorreoService.java
    │   │       ├── TelefonoService.java
    │   │       ├── PerfilClienteService.java
    │   │       ├── PerfilGestorService.java
    │   │       ├── PreferenciaClienteService.java
    │   │       ├── RestauranteOrgService.java
    │   │       ├── RestauranteSucursalService.java
    │   │       ├── ReservaService.java
    │   │       ├── EventoService.java
    │   │       └── CatalogoService.java
    │   │
    │   └── resources/
    │       ├── application.properties
    │       └── sql_reservas_restaurante/
    │           ├── 01_DDL/
    │           │   ├── 01_database.sql
    │           │   ├── 02_schema.sql
    │           │   ├── 03_enums.sql
    │           │   ├── 04_tables.sql
    │           │   ├── 05_constraints.sql
    │           │   └── 06_grants.sql
    │           │
    │           ├── 02_DML/
    │           │   ├── 01_datos_demo.sql
    │           │   └── 02_datos_prueba.sql
    │           │
    │           ├── 03_TRIGGERS/
    │           │   ├── 01_updated_at.sql
    │           │   └── 02_validaciones.sql
    │           │
    │           ├── 04_VIEWS/
    │           │   └── 01_reservas_detalle.sql
    │           │
    │           ├── 05_PROCEDURES/
    │           │   └── 01_procedures.sql
    │           │
    │           ├── 06_TEST/
    │           │   └── 01_transacciones.sql
    │           │
    │           └── EJECUTAR_DESDE_NOTEPAD_Y_CMD.txt
    │
    └── test/
        └── java/sv/edu/ues/reservas/service/
            └── CatalogoServiceTest.java
```

## controller/

Contiene los controladores encargados de recibir solicitudes HTTP y exponer los endpoints de la API.

```text
Postman
   ↓
Controller
```

## dto/

Contiene los objetos utilizados para transportar información entre la API y sus consumidores.

```text
HTTP Request / Response
          ↓
         DTO
```

## domain/entity/

Contiene las entidades JPA que representan los elementos principales del dominio y permiten su persistencia en PostgreSQL.

```text
domain/
├── entity/
│   ├── Cuenta
│   ├── Correo
│   ├── Telefono
│   ├── PerfilCliente
│   ├── PerfilGestor
│   ├── PreferenciaCliente
│   │
│   ├── RestauranteOrg
│   ├── RestauranteSucursal
│   ├── Mesa
│   ├── PoliticaReserva
│   │
│   ├── Reserva
│   ├── ReservaMesa
│   ├── ReservaMesaId
│   │
│   ├── Menu
│   ├── MenuPlato
│   ├── MenuPlatoId
│   ├── MenuBebida
│   ├── MenuBebidaId
│   ├── Plato
│   ├── Bebida
│   ├── Vino
│   │
│   ├── Evento
│   ├── EventoPlato
│   ├── EventoPlatoId
│   ├── EventoBebida
│   ├── EventoBebidaId
│   ├── EventoMenu
│   └── EventoMenuId
│
└── enums/
    ├── EstadoContacto
    ├── EstadoCuenta
    ├── EstadoEvento
    ├── EstadoMenu
    ├── EstadoOrganizacion
    ├── EstadoProducto
    ├── EstadoReserva
    └── EstadoSucursal
```

## domain/enums/

Contiene las enumeraciones que definen los estados y categorías controlados del sistema, utilizados por las entidades y la lógica de negocio.

### Enumeraciones disponibles

- `EstadoContacto`
- `EstadoCuenta`
- `EstadoEvento`
- `EstadoMenu`
- `EstadoOrganizacion`
- `EstadoProducto`
- `EstadoReserva`
- `EstadoSucursal`

## service/

Contiene la lógica de negocio:

```text
Controller
    ↓
 Service
    ↓
Repository
```

## repository/

Contiene la interacción con PostgreSQL.

```text
Service
    ↓
Repository
    ↓
PostgreSQL
```

## config/

Contiene la configuración necesaria para la aplicación.

## exception/

Contiene excepciones y mecanismos de manejo de errores.

---

# 5. Modelo general del sistema

```text
                     SISTEMA DE RESERVAS
                              │
          ┌───────────────────┼───────────────────┐
          │                   │                   │
          ▼                   ▼                   ▼
       CUENTAS           RESTAURANTES          RESERVAS
          │                   │                   │
     ┌────┴────┐         ┌────┴────┐       ┌────┼─────┐
     │         │         │         │       │    │     │
  Cliente    Gestor   Organización Sucursal Mesa Menú Pago
                                │
                      ┌─────────┼─────────┐
                      ▼         ▼         ▼
                   Horarios  Políticas  Eventos
```

## Cuenta

```text
Cuenta
├── PerfilCliente
└── PerfilGestor

Cuenta
├── Correo
└── Telefono
```

## Restaurante

```text
RestauranteOrg
       │
       └── RestauranteSucursal
               ├── Mesa
               ├── HorarioRestaurante
               ├── PoliticaReserva
               ├── Menu
               ├── Plato
               ├── Bebida
               └── Evento
```

## Reserva

```text
Reserva
├── ReservaMesa
├── ReservaPlato
├── ReservaMenu
├── ReservaBebida
└── Pago
     └── ComprobantePago
```

---

# 📋 6. Requisitos previos

Para ejecutar localmente el proyecto se requiere:

- Java 25
- Maven
- PostgreSQL 17
- Postman
- Git
- IntelliJ IDEA u otro IDE compatible con Maven

Verificar las instalaciones:

```bash
java -version
mvn -version
psql --version
git --version
```

---

# 📥 7. Instalación del proyecto

Clonar el repositorio:

```bash
git clone <URL_DEL_REPOSITORIO>
```

Entrar al proyecto:

```bash
cd api-para-reservas-de-restauranteUES
```

Validar Maven:

```bash
mvn validate
```

Compilar:

```bash
mvn clean install
```

---

# 🗄️ 8. Configuración de PostgreSQL

Configuración conceptual:

```text
Host:      localhost
Port:      5433
Database:  <nombre_de_la_base_de_datos>
Username:  <usuario>
Password:  <contraseña>
```

Flujo de conexión:

```text
API Java
   │
   │ JDBC
   ▼
localhost:5433
   │
   ▼
PostgreSQL
```

Los valores reales deberán configurarse según el entorno local del equipo.

---

# ▶️ 9. Ejecución local

Orden recomendado:

```text
1. Iniciar PostgreSQL
          ↓
2. Verificar la base de datos
          ↓
3. Abrir el proyecto
          ↓
4. Cargar dependencias Maven
          ↓
5. Ejecutar la aplicación Java
          ↓
6. Verificar API disponible
          ↓
7. Abrir Postman
          ↓
8. Realizar solicitudes HTTP
          ↓
9. Verificar respuestas JSON
```

## Iniciar desde IntelliJ IDEA

1. Abrir el proyecto.
2. Esperar a que Maven descargue dependencias.
3. Verificar configuración de Java.
4. Verificar conexión con PostgreSQL.
5. Ejecutar la clase principal.
6. Esperar el inicio del servidor.

URL inicial:

```text
http://localhost:8081
```

---

# 📮 10. Pruebas con Postman

Ejemplo de solicitud:

```http
POST http://localhost:8081/api/v1/reservas
Content-Type: application/json
```

```json
{
  "idCuentaCliente": "{{idCuentaCliente}}",
  "idSucursal": "{{idSucursal}}",
  "idPolitica": null,
  "idEvento": null,
  "numeroComensales": 4,
  "horaInicio": "2026-10-25T19:00:00-06:00",
  "horaFin": "2026-10-25T21:00:00-06:00",
  "idsMesas": ["{{idMesa}}"]
}
```

Los registros referenciados deben existir y pertenecer a la sucursal correspondiente.

---

# 🍽️ 11. Catálogo gastronómico

La API permite administrar menús, platos, bebidas y vinos por sucursal.

## Menús

| Método | Ruta |
|----------|----------|
| GET | `/api/v1/menus?idSucursal={id}` |
| POST | `/api/v1/menus` |
| GET | `/api/v1/menus/{id}` |
| PUT | `/api/v1/menus/{id}` |
| DELETE | `/api/v1/menus/{id}` |

## Platos

| Método | Ruta |
|----------|----------|
| GET / POST | `/api/v1/platos` |
| GET / PUT / DELETE | `/api/v1/platos/{id}` |

## Bebidas

| Método | Ruta |
|----------|----------|
| GET / POST | `/api/v1/bebidas` |
| GET / PUT / DELETE | `/api/v1/bebidas/{id}` |

## Vinos

| Método | Ruta |
|----------|----------|
| GET / POST | `/api/v1/bebidas/vinos` |
| GET / PUT / DELETE | `/api/v1/bebidas/{id}/vino` |

Ejemplo:

```json
{
  "idSucursal": "{{idSucursal}}",
  "nombre": "Menú degustación",
  "descripcion": "Selección de temporada",
  "temporada": "Otoño",
  "cantidadPersonas": 2,
  "precio": 35.00,
  "fechaInicio": "2026-10-01",
  "fechaFin": "2026-12-31"
}
```

---

# 🔌 12. Eventos y reservas

La API expone CRUD de eventos y reservas.

## Eventos

```text
GET     /api/v1/eventos
GET     /api/v1/eventos/{id}
POST    /api/v1/eventos
PUT     /api/v1/eventos/{id}
DELETE  /api/v1/eventos/{id}
```

## Reservas

```text
GET     /api/v1/reservas
GET     /api/v1/reservas/{id}
POST    /api/v1/reservas
PUT     /api/v1/reservas/{id}
DELETE  /api/v1/reservas/{id}
```

---

# 🧪 13. Pruebas

## 13.1 Pruebas unitarias (JUnit 5 + Mockito)

Las pruebas unitarias validan la lógica de los servicios sin depender de PostgreSQL.

Clase actualmente identificada:

```text
src/test/java/sv/edu/ues/reservas/service/
└── CatalogoServiceTest.java
```

Cobertura planificada:

| Servicio | Alcance |
|-----------|-----------|
| CuentaService | CRUD, estados y contraseña |
| CorreoService | CRUD y correo principal |
| TelefonoService | CRUD y teléfono principal |
| PerfilClienteService | Operaciones y errores |
| PerfilGestorService | Operaciones y errores |
| PreferenciaClienteService | Referencias y errores |
| RestauranteOrgService | Organización y reglas |
| RestauranteSucursalService | Sucursal y referencias |
| EventoService | Funcionalidad estable |
| ReservaService | Validaciones estables |

> Esta lista representa alcance planificado, no garantía de implementación actual.

## 13.2 Ejecutar las pruebas

### Windows

```cmd
.\mvnw.cmd test
```

```cmd
.\mvnw.cmd clean test
```

### Linux/macOS

```bash
./mvnw test
```

```bash
./mvnw clean test
```

Resultado esperado:

```text
BUILD SUCCESS
```

Reportes:

```text
target/surefire-reports/
```

## 13.3 Criterios para las pruebas unitarias

- Probar flujos exitosos y errores relevantes.
- Simular dependencias con Mockito.
- Verificar persistencia únicamente cuando corresponda.
- Mantener independencia entre pruebas.
- No requerir PostgreSQL.
- No duplicar pruebas HTTP de Postman.

## 13.4 Pruebas funcionales con Postman

Permiten validar:

- Códigos HTTP.
- Respuestas JSON.
- Validaciones.
- Manejo de errores.
- Integración entre capas.

## 13.5 Relaciones en implementación

Las pruebas deben reflejar el estado real del código. Las relaciones aún en desarrollo no deben tratarse como funcionalidades terminadas y deberán documentarse como escenarios pendientes o bloqueados.

---

# 🚀 14. Evolución del proyecto

```text
MODELO UML
     │
     ▼
MODELO DE DOMINIO
     │
     ▼
PostgreSQL
     │
     ▼
Repository
     │
     ▼
Service
     │
     ▼
Controller
     │
     ▼
REST API
     │
     ▼
Postman
     │
     ▼
PRUEBAS
```

Posteriormente podrán incorporarse mecanismos adicionales de seguridad, documentación y despliegue.

---

# 👥 15. Equipo

| Integrante | Carné |
|------------|--------|
| Gerardo Andrés Rodríguez Herrador | RH19056 |
| Luis Ángel Huezo Martínez | HM25001 |
| Rafael Orlando Guardado Díaz | GD24013 |
| Steven Alexis Martínez Jiménez | MJ25012 |
| Uber de Jesús Barillas Quijada | BQ24002 |

> Sustituir estos nombres por los integrantes definitivos del equipo cuando corresponda.

---

# 🚧 Estado del proyecto

**Estado general:** En desarrollo.

El repositorio cuenta con una estructura Java/Maven organizada por capas, entidades de dominio, DTO, repositorios, servicios y controladores orientados a la gestión de reservas para restaurantes.

## Trabajo de pruebas (Milestone 3)

- Identificar servicios sin clase de prueba propia.
- Definir alcance de pruebas unitarias.
- Implementar pruebas pendientes.
- Revisar cobertura de `CatalogoServiceTest`.
- Corregir fallos detectados por Maven.

Los issues asociados representan planificación de trabajo y deben verificarse en la pestaña **Issues** del repositorio.

## Próximas etapas

```text
Implementación del dominio y relaciones pendientes
                     ↓
Persistencia y validaciones
                     ↓
Pruebas unitarias con JUnit 5 y Mockito
                     ↓
Ejecución de pruebas con Maven
                     ↓
Pruebas HTTP con Postman
                     ↓
Corrección de errores y documentación
```

---

**API PARA RESERVAS · UES**

Proyecto académico de Ingeniería de Software.
