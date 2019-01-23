; VocabHunter Inno Setup File

#define MyAppName "VocabHunter"
#define MyAppVersion "@bundle.version@"
#define MyAppPublisher "Adam Carroll"
#define MyAppURL "https://vocabhunter.github.io/"
#define MyAppExeName "VocabHunter.exe"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{1ef57609-b5b1-434f-bdcf-21c09cf92b0f}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\{#MyAppName}
DisableProgramGroupPage=yes
DisableDirPage=no
OutputBaseFilename=VocabHunter-@bundle.version@
Compression=lzma
SolidCompression=yes
ChangesAssociations=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "@executable.location@"; DestDir: "{app}"; Flags: ignoreversion
Source: "@bundle.content@"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{commonprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[Registry]
Root: HKLM; Subkey: "Software\Classes\.santa"; ValueType: string; ValueName: ""; ValueData: "VocabHunterSession"; Flags: uninsdeletevalue
Root: HKLM; Subkey: "Software\Classes\VocabHunterSession"; ValueType: string; ValueName: ""; ValueData: "VocabHunter Session"; Flags: uninsdeletekey
Root: HKLM; Subkey: "Software\Classes\VocabHunterSession\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\VocabHunterSession.ico"
Root: HKLM; Subkey: "Software\Classes\VocabHunterSession\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\VocabHunter.exe"" ""%1"""
