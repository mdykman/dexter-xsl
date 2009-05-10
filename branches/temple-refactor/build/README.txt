dexter-xsl-0.3.0-beta - http://code.google.com/p/dexter-xsl

copyright (c) 2007,2009 Michael Dykman <mdykman@gmail.com>

The user is granted rights to use Dexter-XSL under the
Artistic License. (http://www.opensource.org/licenses/artistic-license.php)

Dexter-XSL is pure jaxp-compliant under Java JRE 1.6. 

Dexter is currently packaged to be used as a command-line tool or as
an embeddable library.

Only a minimal launch script for linux is provided with this version.  The
user is encouraged to create a launcher suitable to thier own environment.
Please refer to docs/dexter-user-guide.xml

To run Dexter-XSL, the user must have the Java 1.6 installed on their system.

dexter-0.3.0-beta copyright (c) 2007-2009 Michael Dykman
  usage: java -cp lib/dexter.jar:lib/gnu-getopt.jar org.dykman.dexter.Main  [OPTIONS] file ...

valid options are listed below.  Long and short options behave identically.
 
 -m, --method=xml|hmtl|text * specify the method in the XSLT output element.   Default: html
 -t, --media-type=<string>   * specify the mime-type for XSLT output. Default: text/html
 -T, --no-media-type        * do not output a media type
 -e, --encoding=<string>    * specify the encoding used throughout the production  Default: UTF-8
 -r, --resolve-entities     * resolve entity references in source. Default: preserve references
 -i, --indent=yes|no        * specify the indent attribute in the output element. Default: no
 -o, --directory=<path>     * specify an output directory. Default: use source directory
 -x, --transform=<xsl-file> <xml-data> * use file xsl-file to transform file xml-data
 -l, --library=<xsl-file>   * load additional function-templates; may be specified multiple times
 -p, --properties=<filename> * load additional properties; may be specified multiple times
 -d, --define=<key>=<value> * define a single property; may be specified multiple times
 -M, --macros               * list all currently defined macros
 -C, --suppress-comments    * filter all user comments out of the stylesheet  
 -v, --version              * print version and copyright information.
 -h, --help                 * print this screen and exit


which will produce one or more XSL stylesheets.

Please refer to docs/index.xml for further information.

 - michael dykman
 - michael@dykman.org
 - July 29, 2008
