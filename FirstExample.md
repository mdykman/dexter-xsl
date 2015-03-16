To get an idea of what Dexter is for and what the purpose of the embedded attributes is, let's look at an example of using Dexter with no Dexter attributes in the input document. We are going to take an arbitrary XML document, and run it through through dexter to generate an XSL file.
```
[michael@fedora14 test-ident]$ cat source.xml
<tests>
	<div id="group-2" >
			a record
		<div  >
		something here
		</div>

	
		<div >
			<span >one </span>
			<span >two </span>
			<span >three </span>
			<span >four </span>
		</div>
	</div>
</tests>

[michael@fedora14 test-ident]$ dexter.sh source.xml 
```

This produces the output file `source.xml.xsl`.  Transforming any XML input with this style sheet produces a replica of the original input file.

```
[michael@fedora14 test-ident]$ xsltproc source.xml.xsl data.xml 
<tests>
	<div id="group-2">
			a record
		<div>
		something here
		</div>

	
		<div>
			<span>one </span>
			<span>two </span>
			<span>three </span>
			<span>four </span>
		</div>
	</div>
</tests>

```

Through the two-step process of compiling with dexter and using the resulting XSL with any XSLT-1.0 compliant processor, any XML file which is not annotated with dexter attributes becomes it's own source.

With the addition of dexter attributes, the generated XSL stylesheet can manipulate the output to reflect the data. Consider that as we proceeed to the [second example](AWalkthrough.md).

> Please consult the  [User Manual](UserManual.md) for details on dexter's attributes.