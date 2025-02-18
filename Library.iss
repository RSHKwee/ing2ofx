// Library.iss
[Code]

function IsSystemEnvVarSet(VarName: string): Boolean;
var
  Value: string;
begin
  Value := ExpandConstant('{%' + VarName + '}');
  Result := Value <> '';
end;

function GetJavaHome: string;
var
  JavaHome: string;
begin
  if RegQueryStringValue(HKEY_CURRENT_USER, 'Environment', 'JAVA_HOME', JavaHome) then
    Result := JavaHome
  else if RegQueryStringValue(HKEY_LOCAL_MACHINE, 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment', 'JAVA_HOME', JavaHome) then
    Result := JavaHome
  else
    Result := '';
end;

function ExtractMajorVersion(const VersionString: string): Integer;
var
  StartPos, EndPos: Integer;
  MajorVersionStr: string;
begin
  Result := 0; // Standaard als parsing mislukt
  StartPos := Pos('"', VersionString); // Zoek eerste dubbele quote

  if StartPos > 0 then
  begin
    Inc(StartPos); // Verplaats naar eerste cijfer
    EndPos := Pos('"', Copy(VersionString, StartPos, Length(VersionString))); // Zoek tweede quote binnen substring

    if EndPos > 0 then
    begin
      EndPos := EndPos + StartPos - 1; // Pas de positie aan t.o.v. originele string
      MajorVersionStr := Copy(VersionString, StartPos, EndPos - StartPos);
      Result := StrToIntDef(MajorVersionStr, 0); // Converteer naar integer
    end;
  end;
end;

procedure SetUserEnvironmentVariable(VarName, VarValue: string);
begin
  RegWriteStringValue(HKCU, 'Environment', VarName, VarValue);
end;

function GetJavaVersion: Integer;
var
  JavaPath, Command: string;
  OutputFile, MajorVersion: string;
  VersionString : AnsiString;
  
  ResultCode: Integer;
  VersionPos, DotPos: Integer;
  P: Integer;
begin
  Result := 0;  // Standaardwaarde als er geen versie wordt gevonden
  
  JavaPath := GetJavaHome;
  if JavaPath = '' then
  begin
   Log('JAVA_HOME not present.');
    exit;
  end;
  
  OutputFile := ExpandConstant('{tmp}\javaversion.txt');
  Command := Format('/C ""%s\bin\java" -version > "%s" 2>&1"', [JavaPath, OutputFile]);
  Log (Format(' Command: ' + '%s', [Command]));
    
  // Voer java -version uit en sla de uitvoer op in een bestand
  if Exec(ExpandConstant('{cmd}'), Command, '', SW_HIDE, ewWaitUntilTerminated, ResultCode) then
  begin
     // Fix: Wacht kort om bestand te laten schrijven
    Sleep(500);

    // Lees de eerste regel uit het uitvoerbestand
    LoadStringFromFile(OutputFile, VersionString);
    Log(Format('java -version output: ' + #13#10 + '%s', [VersionString]));
    DeleteFile(OutputFile);
       
    Result := ExtractMajorVersion(VersionString);
    Log(Format('Major version: %d', [Result]));    
    
  end else begin
    MsgBox('Exec() failed, code: ' + IntToStr(ResultCode), mbInformation, MB_OK);
    DeleteFile(OutputFile);
  end;
end;

function CheckJavaVersion (RequiredVersion : Integer) : boolean;
var
  InstalledVersion: Integer;
begin
  InstalledVersion := GetJavaVersion;
  Result := false;
  
  if InstalledVersion = 0 then
  begin
    MsgBox('Java not found. Install Java.', mbError, MB_OK);
    Abort;
  end;

  if InstalledVersion < RequiredVersion then
  begin
    MsgBox('Java ' + IntToStr(RequiredVersion) + ' or higher is required. Installed version: ' + IntToStr(InstalledVersion), mbError, MB_OK);
    Abort;
  end
  else
  begin
    {MsgBox('Java versie is correct: ' + IntToStr(InstalledVersion), mbInformation, MB_OK); }
    Result := true;
  end;
end;
