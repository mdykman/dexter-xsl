dexter-xsl-0.2.2-beta - http://code.google.com/p/dexter-xsl

copyright (c) 2007,2008 Michael Dykman <michael@dykman.org>

The user is granted rights to use Dexter-XSL under the
Artistic License. (http://www.opensource.org/licenses/artistic-license.php)

Dexter-XSL is pure jaxp-compliant under Java JRE 1.6. 

Dexter is currently packaged to be used as a command-line tool or as
an embeddable library.

Only a minimal launch script for linux is provided with this version.  The
user is encouraged to create a launcher suitable to thier own environment.
Please refer to docs/dexter-user-guide.xml

To run Dexter-XSL, the user must have the Java 1.6 installed on their system.

 java -cp lib/dexter.jar:lib/gnu-getopt.jar org.dykman.dexter.Main  [OPTIONS] file ...

Valid options are listed below.  Long and short options are behave identically.
 
 -d, --define=key=value     * explicitly override a single property; may be specified multiple times
 -r, --resolve-entities     * resolve entity references in source. Default: preserve entities
 -t, --mime-type=STRING     * specify the media-type in the XSLT output element. Default: text/html
 -m, --method=xml|hmtl|text * specify the method in the XSLT output element.   Default: html
 -i, --indent=yes|no        * specify the indent attribute in the output element. Default: no
 -p, --properties=filename  * override the built-in properties
 -e, --encoding=STRING      * specify the encoding used throughout the production  Default: UTF-8
 -c, --suppress-comments    * filter all user comments out of the stylesheet  
 -o, --directory=PATH       * specify an output directory. Default: use source directory
 -v, --version              * print version and copyright information.
 -h, --help                 * print this screen and exit

which will produce one or more XSL stylesheets.

Please refer to docs/index.xml for further information.

 - michael dykman
 - michael@dykman.org
 - July 29, 2008
