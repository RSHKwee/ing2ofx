; -- ing2ofxnojre.iss --
;
#define MyAppName "ing2ofx"
#define MyAppExeName "ing2ofx.exe"
#define MyIconFile "src\main\resources\ingSNSLogo.ico"

[Setup]
AppName={#MyAppName}
AppVersion=0.2.5
AppPublisher=RSH Kwee
AppPublisherURL=https://github.com/RSHKwee/ing2ofx/releases
AppContact=rsh.kwee@gmail.com
WizardStyle=modern
DisableWelcomePage=no
DefaultDirName={code:MyConst}\{#MyAppName}
DefaultGroupName={#MyAppName}
UninstallDisplayIcon={app}\{#MyAppExeName}
InfoBeforeFile=readme.md
OutputDir=target
OutputBaseFilename={#MyAppName}_setup
UninstallFilesDir={app}\uninst
 ; Tell Windows Explorer to reload the environment
ChangesEnvironment=yes
SetupIconFile={#MyIconFile}

[Registry]
Root: HKCU; Subkey: "Environment"; ValueType:string; ValueName: "JAVA_HOME"; \
    ValueData: "{app}\jre"; Flags: preservestringtype

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; \
    GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: ".\target\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "readme.md"; DestDir: "{app}"; Flags: isreadme
; DestDir: {app}\jre; Source: jre\*;   Flags: recursesubdirs ; Check: JreNotPresent

[Icons]
Name: "{commonstartup}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{userdesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Code]
var
  jreNotChecked : Boolean;
  FinishedInstall: Boolean;
  jrePresent : Boolean;

function InitializeSetup(): Boolean;
begin
  Log('InitializeSetup called');
  Result := true;
  jreNotChecked := true;
  jrePresent := false;
end;

procedure InitializeWizard;
begin
  Log('InitializeWizard called');
end;

function JreNotPresent: Boolean;
var
  ResultCode: integer;
begin
  if jreNotChecked then
  begin
    if Exec('java', '-version', '', SW_SHOW, ewWaitUntilTerminated, ResultCode) then
    begin
      jrePresent := false;    
      Log('Java jre is not present.');
    end else begin          
      jrePresent := true;
    end;
    jreNotChecked := false;
  end;
  Result := jrePresent;
end;

<event('InitializeWizard')>
procedure InitializeWizard2;
begin
  Log('InitializeWizard2 called');
end;

procedure DeinitializeSetup();
begin
  Log('DeinitializeSetup called');
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  Log('CurStepChanged(' + IntToStr(Ord(CurStep)) + ') called');
  if CurStep = ssPostInstall then
    FinishedInstall := True;
end;

procedure CurInstallProgressChanged(CurProgress, MaxProgress: Integer);
begin
  Log('CurInstallProgressChanged(' + IntToStr(CurProgress) + ', ' + IntToStr(MaxProgress) + ') called');
end;

function NextButtonClick(CurPageID: Integer): Boolean;
begin
  Log('NextButtonClick(' + IntToStr(CurPageID) + ') called');
  Result := True;
end;

function BackButtonClick(CurPageID: Integer): Boolean;
begin
  Log('BackButtonClick(' + IntToStr(CurPageID) + ') called');
  Result := True;
end;

function ShouldSkipPage(PageID: Integer): Boolean;
begin
  Log('ShouldSkipPage(' + IntToStr(PageID) + ') called');
  { Skip wpInfoBefore page; show all others }
  case PageID of
    wpInfoBefore:
      Result := True;
  else
    Result := False;
  end;  
end;

procedure CurPageChanged(CurPageID: Integer);
begin
  Log('CurPageChanged(' + IntToStr(CurPageID) + ') called');
end;

function PrepareToInstall(var NeedsRestart: Boolean): String;
begin
  Log('PrepareToInstall() called');
  Result := '';
end;

function MyConst(Param: String): String;
begin
  Log('MyConst(''' + Param + ''') called');
  Result := ExpandConstant('{autopf}');
end;

