# Introduction

The intent of this (java) application  is to convert ing (www.ing.nl) csv files to ofx files 
that can be read by a program like GnuCash (www.gucash.org).

The origin is a Python script which can be found on Github:  https://github.com/chmistry/ing2ofx/releases.
The original script needed some modifications due to a new Python version and some changes in the ING CSV format.

Due to Python installation and versioning problems, this Java applicaton was born.
The Python script(s) are rewritten in Java.

The ofx specification can be downloaded from http://www.ofx.net/

A tutorial on how to keep your bank records in GnuCash can be read on:
http://www.chmistry.nl/financien/beginnen-met-boekhouden-in-gnucash/

# Opening menu
When running the application (Windows excutable or Java jar-file) the following menu is shown:

![Main screen ing2ofx](https://github.com/RSHKwee/ing2ofx/blob/master/ing2ofxMain.PNG)

Button _CSV File_: an ING CSV file can be choosen.
- The application recognize a comma or semicolon separated input file.
- The content "normal" or saving transactions.
The checkbox "Only interest transaction" is used only for a CSV file with saving transactions.

Button _Output folder_: Point to the directory where the generated OFX-file(s) are stored. 
A proposal for the "Output filename" is made, this can be changed.

Button _Convert to OFX_: The conversion to OFX format is started, the progres is shown in the lower panel.
<br>
Button _Start GnuCash_: GnuCash is started.

# Setting menu

![Settings menu](https://github.com/RSHKwee/ing2ofx/blob/master/ing2ofxSettings.PNG)

In the "settings" menu the following options are available:
- A ING CSV file may contain transactions for more then one account, all converted OFX transcations can be stored in one OFX file or per account a separate OFX file (default).
- Where to find _GnuCash_ executable.
- For debugging a _loglevel_ can be defined, default is level _INFO_.
- _Look and Feel_ of the GUI can be adjusted.
- _Create logfiles_ in the choosen directory a HTML- and a textfile with logging is created.
