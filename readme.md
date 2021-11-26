# Java GUI for ING2OFX Convertor

A GUI in Java which calls Python scripts for converting ING csv files to OFX format.
The Python GUI has broken? for Python 3.9, anyway I did not get it working......

For me it was the "easiest" way to write a Java GUI......
And the result is in this repository. 

There is some work in progress. 
The execution of the Python scripts works fine when run from Eclipse but not when it is a jar....

I wanted to add some features like:
- The new ING CSV format is ";" separated.
- When the CSV contains more then one account, then it must be possible to split it in a OFX file per account.

For these features I wanted to tweak the GUI....

Content of the original readme.rst, the Graphical user interface does not work with me for Python 3.9.

=======
ing2ofx
=======
Intro
-----
The intent of this script is to convert ing (www.ing.nl) csv files to ofx files 
that can be read by a program like GnuCash (www.gucash.org).

This script is adapted from pb2ofx.pl Copyright 2008, 2009, 2010 Peter Vermaas,
originally found at http://blog.maashoek.nl/2009/07/gnucash-en-internetbankieren/ 
which is now offline.

The ofx specification can be downloaded from http://www.ofx.net/

A tutorial on how to keep your bank records in GnuCash can be read on:
http://www.chmistry.nl/financien/beginnen-met-boekhouden-in-gnucash/

Windows users
-------------
Now available Graphical User Interface with all-in-one msi installer:
See https://github.com/chmistry/ing2ofx/releases

Usage - Graphical user interface:
---------------------------------
::

    On the command line: python gui.py

Usage - command line:
---------------------
::

    usage: ing2ofx [-h] [-o, --outfile OUTFILE] [-d, --directory DIR]
                   [-c, --convert] [-b, --convert-date]
                   csvfile

    This program converts ING (www.ing.nl) CSV files to OFX format. The default
    output filename is the input filename.

    positional arguments:
      csvfile               A csvfile to process

    optional arguments:
      -h, --help            show this help message and exit
      -o, --outfile OUTFILE
                            Output filename
      -d, --directory DIR   Directory to store output, default is ./ofx
      -c, --convert         Convert decimal separator to dots (.), default is
                            false
      -b, --convert-date    Convert dates with dd-mm-yyyy notation to yyyymmdd

Output
------
#. An ofx file converted from the csv file (default in the folder ./ofx)
#. Some statistics:

::

   TRANSACTIONS: 349
   IN:           test2.csv
   OUT:          test2.ofx

