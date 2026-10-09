Introduction

The purpose of this (Java) application is to convert ING (www.ing.nl) CSV files or SNS (www.snsbank.nl) XML files into OFX files that can be read by a program such as GnuCash (www.gnucash.org).

What's new:
- Java upgraded to version 22.
- ING has changed its CSV input format (around 2 March 2023), which is now supported.
- SNS has changed the content of its XML file, which is now supported.
- The SNS XML file can be downloaded as a zip file; this is now supported.
- Module tests have been added.
- The OFX4J library is used for OFX file creation.
- Some refactoring has been done.
- Installation issues have been fixed.
- Internationalisation: Dutch and English are supported.
- Dependencies have been updated to the most recent versions.
