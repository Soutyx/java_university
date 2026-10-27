$ErrorActionPreference = 'Stop'
& "$PSScriptRoot/run.ps1" -Entry SelfCheck
& "$PSScriptRoot/run.ps1" -Entry BoundaryTests
