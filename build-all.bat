@echo off
setlocal
cd /d "%~dp0"
for %%m in (hello-mod hud-clock block-counter) do (
  echo === %%m ===
  call "%%m\build.bat"
  if errorlevel 1 exit /b 1
)
echo All example mods compiled into dist\.
