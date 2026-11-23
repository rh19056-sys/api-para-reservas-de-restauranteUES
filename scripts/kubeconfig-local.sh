#!/usr/bin/env bash
# Genera .kube-local/config con las credenciales del Kubernetes de Docker Desktop.
# El pipeline de act lo usa como KUBECONFIG para desplegar en el clúster local.
# El archivo contiene credenciales: está en .gitignore y NO debe subirse al repositorio.
#
# Uso (desde la raíz del proyecto):  ./scripts/kubeconfig-local.sh
set -euo pipefail

CONTEXTO=docker-desktop
RAIZ="$(cd "$(dirname "$0")/.." && pwd)"
DESTINO="$RAIZ/.kube-local/config"

if ! kubectl config get-contexts "$CONTEXTO" > /dev/null 2>&1; then
  echo "No existe el contexto '$CONTEXTO'. Habilita Kubernetes en Docker Desktop (Settings > Kubernetes)." >&2
  exit 1
fi

mkdir -p "$(dirname "$DESTINO")"

# --minify: solo el contexto indicado; --flatten: incrusta los certificados en el archivo.
# Dentro del contenedor de act, 127.0.0.1 es el propio contenedor. Se apunta a
# host.docker.internal (el equipo anfitrión), manteniendo el puerto del clúster.
# El certificado del API server no incluye ese nombre, por eso se valida TLS contra "localhost".
kubectl config view --minify --flatten --context "$CONTEXTO"   | sed -E 's#^([[:space:]]+)server: https://(127\.0\.0\.1|localhost):([0-9]+)#server: https://host.docker.internal:
tls-server-name: localhost#'   > "$DESTINO"
chmod 600 "$DESTINO"

echo "Kubeconfig generado en $DESTINO (para usarlo desde el runner de act)."
grep -E 'server:|tls-server-name:' "$DESTINO"
