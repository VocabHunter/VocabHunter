set INSTALLER_TYPE=%1
set MODULE_PATH=%2
set INPUT=%3
set OUTPUT=%4
set JAR=%5
set VERSION=%6
set APP_ICON=%7

call "jpackage" ^
    --type "%INSTALLER_TYPE%" ^
    --module-path "%MODULE_PATH%" ^
    --verbose ^
    --add-modules "java.base,java.datatransfer,java.desktop,java.scripting,java.xml,jdk.jsobject,jdk.unsupported,jdk.unsupported.desktop,jdk.xml.dom,javafx.controls,javafx.fxml,java.naming,java.sql,jdk.charsets" ^
    --input "%INPUT%" ^
    --dest "%OUTPUT%" ^
    --name "VocabHunter" ^
    --main-jar "%JAR%" ^
    --app-version "%VERSION%" ^
    --icon "%APP_ICON%" ^
    --win-dir-chooser ^
    --win-menu ^
    --win-menu-group VocabHunter ^
    --main-class "io.github.vocabhunter.gui.main.VocabHunterGuiExecutable"
