-- Ejecutar conectado a la base postgres.
-- Crea la base de datos si no existe.
SELECT 'CREATE DATABASE reservas_restaurante'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'reservas_restaurante'
)\gexec




