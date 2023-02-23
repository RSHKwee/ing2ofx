; -- ing2ofx_jreDownload.iss --
;
#define MyAppName "ing2ofx"
#define MyAppVersion "0.2.5"
#define MyAppExeName "ing2ofx.exe"
#define MyIconFile "src\main\resources\ingSNSLogo.ico"

#define JreUrl =  "https://www.eclipse.org/downloads/download.php?file=/justj/jres/17/downloads/20230204_0657/org.eclipse.justj.openjdk.hotspot.jre.minimal.stripped-17.0.6-win32-x86_64.tar.gz"

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
OutputBaseFilename={#MyAppName}_v{#MyAppVersion}_jreDownload_setup
UninstallFilesDir={app}\uninst
 ; Tell Windows Explorer to reload the environment
ChangesEnvironment=yes
SetupIconFile={#MyIconFile}

[Registry]
Root: HKCU; Subkey: "Environment"; ValueType:string; ValueName: "JAVA_HOME"; \
    ValueData: "{app}\jre"; Flags: preservestringtype; Check: JreNotPresent

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; \
    GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: ".\target\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "readme.md"; DestDir: "{app}"; Flags: isreadme
Source: ".\help\ing2ofx.chm"; DestDir: "{app}"; Flags: ignoreversion


[Icons]
Name: "{commonstartup}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{userdesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Code]
var
  jreNotChecked : Boolean;
  FinishedInstall: Boolean;
  L_jreNotPresent: boolean;
 
  DownloadPage: TDownloadWizardPage;

function InitializeSetup(): Boolean;
begin
  Log('InitializeSetup called');
  Result := true;
  jreNotChecked := true;
  L_jreNotPresent := true;
end;

function OnDownloadProgress(const Url, FileName: String; const Progress, ProgressMax: Int64): Boolean;
begin
  if Progress = ProgressMax then
    Log(Format('Successfully downloaded file to {tmp}: %s', [FileName]));
  Result := True;
end;

procedure ExtractTarGz(TarGzFile, DestDir: string);
var
  ResultCode: Integer;
begin
  Exec('C:\Windows\System32\tar.exe', '-xvzf "' + TarGzFile + '" -C "' + DestDir +'"', '', SW_HIDE,ewWaitUntilTerminated, ResultCode);
  Log('tar 1 ResultCode: ' + IntToStr(ResultCode));
  Log('');
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
  DownloadPage := CreateDownloadPage(SetupMessage(msgWizardPreparing), SetupMessage(msgPreparingDesc), @OnDownloadProgress);
  Log('InitializeWizard2 called');
end;

function NextButtonClick(CurPageID: Integer): Boolean;
begin
  if ((CurPageID = wpReady) AND JreNotPresent()) then
  begin
    DownloadPage.Clear;
    DownloadPage.Add(ExpandConstant('{#JreUrl}' + '&r=1'), 'jre.tar.gz', '');
    DownloadPage.Show;
    try
      try
        DownloadPage.Download; // This downloads the files to {tmp}
        Result := True;
      except
        if DownloadPage.AbortedByUser then
          Log('Aborted by user.')
        else
          SuppressibleMsgBox(AddPeriod(GetExceptionMessage), mbCriticalError, MB_OK, IDOK);
        Result := False;
      end;
    finally
      DownloadPage.Hide;
    end;
  end else begin
    Result := True;
  end;  
end;

procedure DeinitializeSetup();
begin
  Log('DeinitializeSetup called');
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  Log('CurStepChanged(' + IntToStr(Ord(CurStep)) + ') called');
  if ((CurStep = ssPostInstall) and (NOT JreNotPresent())) then
  begin
    ExtractTarGz(ExpandConstant('{tmp}\jre.tar.gz'), ExpandConstant('{app}\jre'));
    FinishedInstall := True;
  end;
end;

procedure CurInstallProgressChanged(CurProgress, MaxProgress: Integer);
begin
  Log('CurInstallProgressChanged(' + IntToStr(CurProgress) + ', ' + IntToStr(MaxProgress) + ') called');
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

