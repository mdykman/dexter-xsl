All HTML-specific operators are in the 'dd' namespace.

## dd:form ##
> `dd:form="XPath"`
form completion

**form** works for all types of for elements except the select/option construct which has it's own operator.  The XPath expression should select a node whose members match the names of the form elements.
```
data.xml
<xml>
  <formdata>
     <name>Bob</name>
     <email>bob@blackhole.com</email>
  </formdata>
</xml>
```

```
<form dd:form="xml/formdata">
 <input name="name" />
 <input name="email" />
</form>
```

The form will be prepopulated with the data from the XML.

## dd:option ##

## dd:options ##