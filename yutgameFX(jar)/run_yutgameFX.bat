@echo off
cd /d %~dp0
java --module-path "C:\Users\gangj\Downloads\openjfx-17.0.15_windows-x64_bin-sdk\javafx-sdk-17.0.15\lib" --add-modules javafx.controls,javafx.fxml -jar yutgameFX.jar
pause