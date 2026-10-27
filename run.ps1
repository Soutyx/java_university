param(
    [ValidateSet('Main','SelfCheck','BoundaryTests')][string]$Entry = 'Main',
    [string[]]$AppArgs = @()
)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Path out -Force | Out-Null
    $sources = @(Get-ChildItem -Path src,test -Recurse -Filter *.java | ForEach-Object FullName)
    & javac --release 17 -encoding UTF-8 -d out @sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
    & java '-Dfile.encoding=UTF-8' '-Dstdout.encoding=UTF-8' '-Dstderr.encoding=UTF-8' -cp out ru.edu.pr02.$Entry @AppArgs
    if ($LASTEXITCODE -ne 0) { throw 'Program failed' }
} finally { Pop-Location }
