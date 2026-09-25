<h1 align="center">
  API PARA RESERVAS DE RESTAURANTE UES
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
  <img src="https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" height="42">
  <img src="https://img.shields.io/badge/PostgreSQL-17-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" height="42">
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" height="42">
  <img src="https://img.shields.io/badge/JSON-Data-000000?style=for-the-badge&logo=json&logoColor=white" height="42">
  <img src="https://img.shields.io/badge/HTML5-Markup-E34F26?style=for-the-badge&logo=html5&logoColor=white" height="42">
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-IDE-000000?style=for-the-badge&logo=intellijidea&logoColor=white" height="42">
  <img src="https://img.shields.io/badge/Git-Version_Control-F05032?style=for-the-badge&logo=git&logoColor=white" height="42">
  <img src="https://img.shields.io/badge/GitHub-Repository-181717?style=for-the-badge&logo=github&logoColor=white" height="42">
</p>

<br>

<hr>

<h2>Sobre el proyecto</h2>

<p>
  API REST orientada a la gestión de reservas y administración de restaurantes.
  El sistema permitirá gestionar sucursales de restaurantes, hacer reservas, gestionar recursos tangibles y abstractos como
  mesas, horarios, menús, platos, bebidas, eventos, reservas y pagos,
  manteniendo una arquitectura organizada y trazable.
</p>

<p align="center">
  <strong>Universidad de El Salvador · Proyecto académico</strong>
</p>

<hr>

# 1. Descripción

**API para Reservas de Restaurante UES** es un sistema backend orientado
a la gestión de reservas en restaurantes.

El sistema permite modelar y administrar diferentes elementos del proceso
de reserva, incluyendo:

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

El proyecto se desarrolla inicialmente para **ejecución local**, utilizando
PostgreSQL como sistema de persistencia y Postman como herramienta para
realizar y probar las solicitudes HTTP hacia la API.

---

# 2. Objetivo

El objetivo del proyecto es desarrollar una API REST capaz de proporcionar
una base organizada y escalable para la gestión de reservas de restaurantes.

El sistema contempla principalmente dos perfiles:

### 👤 Cliente

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

### 🏢 Gestor

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

El proyecto se ejecutará inicialmente de forma local.

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
                 │    localhost:8080   │
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
3.2 Flujo de una solicitud

Una solicitud realizada desde Postman seguirá conceptualmente este
recorrido:

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

4. Estructura actual del proyecto

```text
api-para-reservas-de-restauranteUES/
│
├── .idea/
│
├── src/
│   └── main/
│       └── java/
│           └── sv/
│               └── edu/
│                   └── ues/
│                       └── reservas/
│
│                           ├── controller/
│                           ├── dto/
│                           ├── model/
│                           ├── service/
│                           ├── repository/
│                           ├── config/
│                           └── exception/
│
├── pom.xml
│
└── README.md

```
controller/

Contendrá los controladores encargados de recibir las solicitudes HTTP
y exponer los endpoints de la API.

Postman
   ↓
Controller
dto/

Contendrá los objetos utilizados para transportar información entre la
API y sus consumidores.

HTTP Request / Response
          ↓
         DTO
model/

Contendrá las clases principales del dominio del sistema.

Entre ellas:

Cuenta
PerfilCliente
PerfilGestor
Correo
Telefono

RestauranteOrg
RestauranteSucursal
GestorRestaurante

Mesa
HorarioRestaurante
PoliticaReserva

Menu
Plato
MenuPlato
Bebida
Vino
MenuBebida

Reserva
ReservaMesa
ReservaPlato
ReservaMenu
ReservaBebida

Pago
ComprobantePago

Evento
EventoMenu
EventoPlato
EventoBebida

PreferenciaCliente
service/

Contendrá la lógica de negocio de la aplicación.

Controller
    ↓
 Service
    ↓
Repository
repository/

Contendrá los componentes responsables de la interacción con la
persistencia de datos.

Service
   ↓
Repository
   ↓
PostgreSQL
config/

Contendrá la configuración necesaria para el funcionamiento de la
aplicación.

exception/

Contendrá las excepciones y mecanismos relacionados con el manejo de
errores de la API.

5. Modelo general del sistema

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
  Cliente    Gestor   Organización Sucursal Mesa  Menú  Pago
                                │
                      ┌─────────┼─────────┐
                      ▼         ▼         ▼
                   Horarios  Políticas  Eventos
Cuenta

La cuenta funciona como superclase:

Cuenta
├── PerfilCliente
└── PerfilGestor

Además:

Cuenta
├── Correo
└── Telefono
Restaurante
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
Reserva
Reserva
├── ReservaMesa
├── ReservaPlato
├── ReservaMenu
├── ReservaBebida
└── Pago
       └── ComprobantePago

```
📋 6. Requisitos previos

Para ejecutar el proyecto localmente se requiere:

Java 25.
Maven.
PostgreSQL 17.
Postman.
Git.
IntelliJ IDEA u otro IDE compatible con Maven.

Verificar las instalaciones:

```text
java -version
mvn -version
psql --version
git --version
```

Postman puede utilizarse independientemente del IDE para realizar las
solicitudes HTTP hacia la API.

📥 8. Instalación del proyecto

Clonar el repositorio:
```text

git clone <URL_DEL_REPOSITORIO>
```

Entrar al directorio:

```text
cd api-para-reservas-de-restauranteUES
```
```text
Validar el proyecto Maven:

mvn validate
```

Compilar:
```text
mvn clean install
```

🗄️ 8. Configuración de PostgreSQL

La base de datos se ejecutará inicialmente de forma local.

Configuración conceptual:

```text

Host:      localhost
Port:      5433
Database:  <nombre_de_la_base_de_datos>
Username:  <usuario>
Password:  <contraseña>

La API utilizará esta conexión para acceder a PostgreSQL mediante la
capa repository.

API Java
   │
   │ JDBC
   ▼
localhost:5433
   │
   ▼
PostgreSQL

Los valores concretos de conexión deberán configurarse según el entorno
local utilizado por el equipo.

```

▶️ 9. Ejecución local

La ejecución del proyecto seguirá este orden:

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
6. Verificar que la API esté disponible
          ↓
7. Abrir Postman
          ↓
8. Realizar HTTP Requests
          ↓
9. Verificar respuestas JSON
10.1 Iniciar la API desde IntelliJ IDEA
Abrir el proyecto en IntelliJ IDEA.
Esperar a que Maven cargue las dependencias.
Verificar la configuración de Java.
Verificar la conexión con PostgreSQL.
Ejecutar la clase principal de la aplicación.
Esperar el inicio del servidor HTTP.

Inicialmente se utilizará:
```text

http://localhost:8080
```

El puerto puede modificarse posteriormente mediante la configuración
de la aplicación.

📮 10. Pruebas con Postman

Postman será la herramienta principal para realizar las pruebas HTTP
durante el desarrollo local.

Una solicitud tendrá la siguiente estructura:

POST
http://localhost:8080/reservas

Por ejemplo:
```

POST http://localhost:8080/reservas
Content-Type: application/json

Con un cuerpo JSON:

{
    "numeroComensales": 4,
    "horaInicio": "2026-09-25T19:00:00",
    "horaFin": "2026-09-25T21:00:00"
}
```
La respuesta será recibida nuevamente en Postman:
```
Postman
   │
   │ POST /reservas
   ▼
API
   │
   ▼
PostgreSQL
   │
   ▼
API
   │
   │ JSON Response
   ▼
Postman
```
🔌 11. Endpoints

Los endpoints serán definidos progresivamente durante la implementación
de los casos de uso.

De forma conceptual, las operaciones podrán organizarse mediante
recursos como:
```
/reservas
/restaurantes
/sucursales
/mesas
/menus
/platos
/bebidas
/eventos
/pagos
/clientes
/gestores
```
Los endpoints definitivos deberán corresponder al diseño de casos de uso,
modelo de dominio y reglas de negocio del proyecto.

🧪 12. Pruebas

Las pruebas se incorporarán progresivamente durante el desarrollo.

Se contemplan pruebas para:
```
Reglas de negocio.
Creación de reservas.
Consulta de reservas.
Cancelación de reservas.
Gestión de mesas.
Gestión de menús.
Gestión de platos y bebidas.
Gestión de eventos.
Procesamiento de pagos.
Validaciones.
Manejo de errores.
Persistencia.
```
Postman será utilizado principalmente para verificar el comportamiento
de los endpoints mediante solicitudes HTTP.

🚀 13. Evolución del proyecto

La implementación seguirá una evolución progresiva:

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

Posteriormente podrán incorporarse mecanismos adicionales de seguridad,
documentación, despliegue y otras capacidades según las necesidades
del proyecto.

👥 14. Equipo

| Integrantes | DUE |
|---|---|
| Gerardo Andrés Rodríguez Herrador | rh19056 |
| Luis Ángel Huezo Martínez | hm25001 |
| Rafael Orlando Guardado Diaz | gd24013 |
| Steven Alexis Martinez Jimenez | mj25012 |
| Uber de Jesús Barillas Quijada | bq24002 |


Sustituir los nombres anteriores por los integrantes reales del equipo.

15. Estado del proyecto

Estado: 🚧 En desarrollo

Actualmente se cuenta con:

Modelo conceptual.
Modelo de clases UML.
Casos de uso.
Definición inicial de arquitectura.
Estructura inicial Java/Maven.
Definición de entidades y relaciones.
Diseño inicial de la API.

Próximas etapas:
```text

Implementación del dominio
        ↓
Persistencia PostgreSQL
        ↓
Servicios
        ↓
Controllers
        ↓
Endpoints
        ↓
Pruebas con Postman
        ↓
Validación
        ↓
Documentación
```
<p align="center"> <strong> API PARA RESERVAS · UES</strong> </p> <p align="center"> Proyecto académico de Ingeniería de Software </p> 
