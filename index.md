# ING2OFX Introduction

The intent of this (java) application  is to convert ing (www.ing.nl) csv files to ofx files 
that can be read by a program like GnuCash (www.gucash.org).

The origin is a Python script which can be found on Github:  https://github.com/chmistry/ing2ofx/releases.
The original script needed some modifications due to a new Python version and some changes in the ING CSV format.
Due to Python installation problems and integration in Java, this Java applicaton was born.
The Python script(s) are rewritten in Java.

The ofx specification can be downloaded from http://www.ofx.net/

A tutorial on how to keep your bank records in GnuCash can be read on:
http://www.chmistry.nl/financien/beginnen-met-boekhouden-in-gnucash/

# Opening menu
When running the application (Windows excutable or Java jar-file) the following menu is shown:

![Main screen ing2ofx](https://github.com/RSHKwee/ing2ofx/blob/master/ing2ofxMain.PNG)

With the button "CSV File" a ING CSV file can be choosen.
The application recognize a comma or semicolon separated input file, it also recognize a content with "normal" or saving transactions.


In the "settings" menu the following options are available:
- A ING CSV file may contain transactions for more then one account, all converted OFX transcations can be stored in one OFX file or per account a separate OFX file (default).
- The more or less original Python scripts can be used for conversion.

See:
![Settings menu](https://github.com/RSHKwee/ing2ofx/blob/master/ing2ofxSettings.PNG)



You can use the [editor on GitHub](https://github.com/RSHKwee/ing2ofx/edit/gh-pages/index.md) to maintain and preview the content for your website in Markdown files.

Whenever you commit to this repository, GitHub Pages will run [Jekyll](https://jekyllrb.com/) to rebuild the pages in your site, from the content in your Markdown files.

### Markdown

Markdown is a lightweight and easy-to-use syntax for styling your writing. It includes conventions for

```markdown
Syntax highlighted code block

# Header 1
## Header 2
### Header 3

- Bulleted
- List

1. Numbered
2. List

**Bold** and _Italic_ and `Code` text

[Link](url) and ![Image](src)
```

For more details see [Basic writing and formatting syntax](https://docs.github.com/en/github/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax).

### Jekyll Themes

Your Pages site will use the layout and styles from the Jekyll theme you have selected in your [repository settings](https://github.com/RSHKwee/ing2ofx/settings/pages). The name of this theme is saved in the Jekyll `_config.yml` configuration file.

### Support or Contact

Having trouble with Pages? Check out our [documentation](https://docs.github.com/categories/github-pages-basics/) or [contact support](https://support.github.com/contact) and we’ll help you sort it out.
