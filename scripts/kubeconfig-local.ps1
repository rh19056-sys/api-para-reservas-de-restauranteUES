# Genera .kube-local/config con las credenciales del Kubernetes de Docker Desktop.
# El pipeline de act lo usa como KUBECONFIG para desplegar en el clúster local.
# El archivo contiene credenciales: está en .gitignore y NO debe subirse al repositorio.
#
# Uso (desde la raíz del proyecto):  .\scripts\kubeconfig-local.ps1

$ErrorActionPreference = "Stop"
$contexto = "docker-desktop"
$raiz = Split-Path -Parent $PSScriptRoot
$destino = Join-Path $raiz ".kube-local\config"

kubectl config get-contexts $contexto *> $null
if ($LASTEXITCODE -ne 0) {
    Write-Error "No existe el contexto '$contexto'. Habilita Kubernetes en Docker Desktop (Settings > Kubernetes)."
}

New-Item -ItemType Directory -Force -Path (Split-Path $destino) | Out-Null

# --minify: solo el contexto indicado; --flatten: incrusta los certificados en el archivo.
$config = kubectl config view --minify --flatten --context $contexto
if ($LASTEXITCODE -ne 0) { Write-Error "No se pudo leer la configuración de kubectl." }

# Dentro del contenedor de act, 127.0.0.1 es el propio contenedor. Se apunta a
# host.docker.internal (el equipo anfitrión), manteniendo el puerto del clúster.
# El certificado del API server no incluye ese nombre, por eso se valida TLS contra "localhost".
$config = $config -replace 'server: https://(127\.0\.0\.1|localhost):', 'server: https://host.docker.internal:'
$config = $config -replace '(\s+)server: (https://host\.docker\.internal:\d+)', ('$1server: $2' + "`n" + '$1tls-server-name: localhost')

[System.IO.File]::WriteAllLines($destino, $config)
Write-Host "Kubeconfig generado en $destino (para usarlo desde el runner de act)."
Select-String -Path $destino -Pattern 'server:|tls-server-name:' | ForEach-Object { $_.Line.Trim() }
