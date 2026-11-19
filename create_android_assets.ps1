$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
  python ci/package.py --output android/otclientv8/assets/data.zip
  if ($LASTEXITCODE) { throw 'Asset packaging failed' }
} finally { Pop-Location }
