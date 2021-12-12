# ING2OFX

The intent of this (java) application  is to convert ing (www.ing.nl) csv files to ofx files 
that can be read by a program like GnuCash (www.gucash.org).

This script is adapted from pb2ofx.pl Copyright 2008, 2009, 2010 Peter Vermaas,
originally found at http://blog.maashoek.nl/2009/07/gnucash-en-internetbankieren/ 
which is now offline.

The ofx specification can be downloaded from http://www.ofx.net/

A tutorial on how to keep your bank records in GnuCash can be read on:
http://www.chmistry.nl/financien/beginnen-met-boekhouden-in-gnucash/

Due to Python installation problems and integration in Java, this Java applicaton was born.
Its is based on the python scripts of Chmistry (see https://github.com/chmistry/ing2ofx/releases).
The Python script(s) are rewritten in Java.

The application recognize a comma or semicolon separated input file, it also recognize a content with "normal" or saving transactions.

![Main screen ing2ofx](https://github.com/RSHKwee/ing2ofx/blob/master/ing2ofxMain.PNG)

A ING CSV file may contain transactions for more then one account.
All converted OFX transcations can be stored in one OFX file or per account a separate OFX file (default). 
See:

![Settings menu](https://github.com/RSHKwee/ing2ofx/blob/master/ing2ofxSettings.PNG)
