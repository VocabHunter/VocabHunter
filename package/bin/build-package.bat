set INSTALLER_TYPE=%1
set MODULE_PATH=%2
set INPUT=%3
set OUTPUT=%4
set JAR=%5
set VERSION=%6
set FILE_ASSOCIATIONS=%7
set APP_ICON=%8
set MODULES=%~9

call "jpackage" ^
    --type "%INSTALLER_TYPE%" ^
    --module-path "%MODULE_PATH%" ^
    --verbose ^
    --add-modules "%MODULES%" ^
    --input "%INPUT%" ^
    --dest "%OUTPUT%" ^
    --name "VocabHunter" ^
    --main-jar "%JAR%" ^
    --app-version "%VERSION%" ^
    --file-associations "%FILE_ASSOCIATIONS%" ^
    --icon "%APP_ICON%" ^
    --win-dir-chooser ^
    --win-menu ^
    --win-menu-group VocabHunter ^
    --main-class "io.github.vocabhunter.gui.main.VocabHunterGuiExecutable"
