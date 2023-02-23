; -- ing2ofx.iss --
;
#define MyAppName "ing2ofx"
#define MyAppVersion GetVersionNumbersString('target\ing2ofx.exe')
#define MyAppExeName "ing2ofx.exe"
#define MyIconFile "src\main\resources\ingSNSLogo.ico"

[Setup]
AppName={#MyAppName}
AppVersion={#MyAppVersion}
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
OutputBaseFilename={#MyAppName}_v{#MyAppVersion}_jre_setup
UninstallFilesDir={app}\uninst
 ; Tell Windows Explorer to reload the environment
ChangesEnvironment=yes
SetupIconFile={#MyIconFile}

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; \
    GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: ".\target\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "readme.md"; DestDir: "{app}"; Flags: isreadme
Source: ".\help\ing2ofx.chm"; DestDir: "{app}"; Flags: ignoreversion
DestDir: {app}\jre; Source: jre\*;   Flags: recursesubdirs ; Check: JreNotPresent

[Icons]
Name: "{commonstartup}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{userdesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Code]
var
  jreNotChecked : Boolean;
  FinishedInstall: Boolean;
  L_jreNotPresent: boolean;

function InitializeSetup(): Boolean;
begin
  Log('InitializeSetup called');
  Result := true;
  jreNotChecked := true;
  L_jreNotPresent := true;
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
    if Exec('"%java_home%\bin\java"', '-version', '', SW_SHOW, ewWaitUntilTerminated, ResultCode) then
    begin
      L_jreNotPresent := true;
      RegWriteStringValue('HKCU', 'Environment', 'JAVA_HOME', ExpandConstant('{app}\jre'));  
      Log('Java jre is not present, define JAVA_HOME.');
    end else begin          
      L_jreNotPresent := false;
      Log('Java jre is present.');
    end;
    jreNotChecked := false;
  end;
  Result := L_jreNotPresent;
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

