In the life of every browser-based webapp, there is the static HTML file which defines the look and feel of the site.  Let's take a simple example, pushing all the styling to CSS. An example of a simple 'Friends' page.
```
<html>
<head>
 ... include stylesheets, javascript libraries, etc
</head>
<body>
 <div id="header">
 </div>
<div id="main">
  <div class="myself">
<a href="/profiles/myself" class="frname">me</a>
   </div>

  <div class="heading">
My Friend List
   </div>
 <div>
<a href="/profiles/john" class="frname">john</a>
 </div>
 <div>
<a href="/profiles/george" class="frname">george</a>
 </div>
 <div>
<a href="/profiles/paul" class="frname">paul</a>
 </div>
</div>
 <div id="footer">
 .. copyright, business links, disclaimer
 </div>
</body>
</html>
```

In this form, you can load it into a browser, modify the document or the CSS and immediately see your results.  Whatever technology your application will be implemented with, you don't need it at this stage.

> ## Notation ##
Assuming a simple xml data structure:
```
data.xml
<result>
  <friends>
     <friend>curly</friend>
     <friend>larry</friend>
     <friend>mo</friend>
  <friends>
</result>
```

We notate our requirements in the document with attributes in the 'dx' namespace. The only variable data in this document is the friends list, so the `div` which contains that is the focus of our notation.
```
<html xmlns:dx="http://code.google.com/p/dexter-xsl/source/browse/trunk/schema/dexterity-1.0.xsd">
<head>
 ... include stylesheets, javascript libraries, etc
</head>
<body>
 <div id="header">
 </div>
<div id="main">
  <div class="myself">
<a href="/profiles/myself" class="frname">me</a>
   </div>

  <div class="heading">
My Friend List
   </div>
 <div dx:each="result/friends/friend">
<a dx:attr="href:/profiles/{.}" dx:text="." 
    href="/profiles/john" class="frname">john</a>
 </div>
 <div dx:ignore="">
<a href="/profiles/george" class="frname">george</a>
 </div>
 <div dx:ignore="">
<a href="/profiles/paul" class="frname">paul</a>
 </div>
</div>
 <div id="footer">
 .. copyright, business links, disclaimer
 </div>
</body>
</html>
```

This is an example of a complete dexter source file.

`dx:each` accepts an XPath expression to define a data set and will produce a copy of the `div` for each element in the data set. It also sets to context path, which is why `dx:text` has a single dot, meaning the value of the current `friend` node.

The `div`s adjacent to the `dx:each`  receive a `dx:ignore`  notation which allows us to leave the document intact for static viewing, while making sure those elements are excluded from the code generation process.

The addition of the `xmlns` tag on the html element is not strictly required for dexter, it is there for the convenience of other XML-oriented tools which might otherwise complain about the presence of the `dx:` attributes. Under no circumstance will that namespace declaration appear in either your stylesheet or your output document.

Simliarly, dexter's attributes are erased from the generated files.

You may refer to the [User Manual](UserManual.md) for details on the `dx` operators.

> ## Generating XSL ##

Assuming Dexter has been installed correctly, turning your well-formed HTML file into XSL, is trivial:

  * On OS/X and linux at the command line: `$ dexter.sh design.html`
  * On windows: `c:\> dexter.bat design.html`

This will produce `design.html.xsl`.

If you are having trouble compiling, consult the [User Manual](UserManual.md) for installation instructions or post to the group for guidance.

> ## Viewing the Result ##

Again, the command line provides the most direct tool, xsltproc.

The following command renders our result out in full:
```
 $ xsltproc design.html.xsl data.xml 
```

Alternatively, we could have embedded the stylesheet into the XML data:
```
data.xml
<?xml-stylesheet type="text/xsl" href="design.html.xsl"?>
<result>
  <friends>
...
```

Now you can load that document into any modern desktop browser to render it.

Consult the [User Manual](UserManual.md) for details about Dexter's operators.