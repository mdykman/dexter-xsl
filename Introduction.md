Dexter is a tool for creating XSLT-1.0 from  well-formed XML example documents.  It uses a collection of attributes to indicate required operations.

> ## Installation ##
It is recommended that you copy build/dexter.jar, lib/xerxesImpl.jar and lib/xml-apis.jar into a seperate directory along with a script to luanch it.  Under linux and OS/X, that script is dexter.sh.  No batch file is supplied for windows, but it should be a trivial job to create one.

See SCRIPTS below.


> ## Usage ##
The Dexter package includes a shell script written in bash which has only been tested under linux systems.  Dexter may be used on any operating system but you may be required to write a simple wrapper script.

under a bash shell (linux, OS/X)
```
dexter.sh [options] filename
```

To invoke Dexter at the command line on any system:
```
java  -jar dexter.jar [options] filename ...
```

`gnu-get-opts.jar` should be in the same directory as `dexter.jar`.

See SCRIPTS below for further notes on creating a simple wrapper for your system.

> ## Entities ##
Dexter uses JAXP to parse well-formed XML of any type: HTML, RSS, Soap responses,etc.  Unlike HMTL, it is an error to use undeclared entities in XML.  For most HTML fomats, this is as simple as declaring a DTD at the top of your document.

```
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
```

For users of HTML5, the doctype is just `<!DOCTYPE html>` which, unfortunately, does not declare any entities to the parser.  A reasonable workaround is to declare you entities in the document itself.
```
<!DOCTYPE html[
  <!ENTITY nbsp "&#160;">
]>
```

> ## JAXP ##

The default JAXP parser built into Java is flawed and does some very odd things with entities sometimes.  I recommend that you make the xerces parser (bundled) available on your class path for best results.

> ## Scripts ##
To invoke Dexter at the command line:
```
java  -jar dexter.jar [options] filename ...
```

> ### Dependencies ###

When used at the command line, Dexter's Main class uses gnu-getopt.jar to parse command line arguments.  That jar file should be in the same directory as dexter.jar.  Distributed with Dexter is the most current version of xerces, implemented as xercesImpl.jar and xmp-apis.jar.  It is recommended that you include these on your classpath as the default JAXP-provided XML parser does some unexpected things, most noticibly around entity handling.

[User Manual](UserManual.md)