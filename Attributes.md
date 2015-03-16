There are 4 Dexter operators used to substitute data into target attributes:

  * dx:attr
  * dx:attrs
  * dx:cattr
  * dx:cattrs

The '!' prefix may be used to retain a default value from the target document.

It does not matter if the attribute is present in the source document or not.

> ## dx:attr ##
> single attribute substitution

> `dx:attr="[attribute-name]:{prefix}?XPath"`
or
> `dx:attr="[attribute-name]:{prefix}?(<literal text>{XPath}) ..."`

**attr** handles the simplest case, substituting a single value into a single target attribute.

```
data.xml
<data>
  <profile>
      <name>John</name>
      <icon>john.jpg</icon>
      <description>Something about me</description>
  </profile>
</data>
```

```
dexter source
<div>
   <span dx:text="data/profile/name">Name</span> 
   <img src="notfound.jpg" dx:attr="!src:data/profile/icon" />
</div>
```
```
renders as
<div>
   <span>John</span> 
   <img src="john.jpg"/>
</div>
```

Had the value `data/profile/icon` had been empty, the result would have been `src="notfound.jpg"` because of the '!' prefix.  Without it, the attribute would be empty.

> ### Attribute Value Termplate ###

All attributes in this family accept http://www.w3.org/TR/xslt#dt-attribute-value-template  attribute value template-type format strings], meaning we can use braces to mix one or more XPath expressions.


```
   <a href dx:attr="href:http://www.google.com?q={data/profile/name}"
      dx:text="Who is this {data/profile/name} person?">Name</a> 

```
> ## dx:attrs ##
> multiple attribute substitution
> `dx:attr="SEP[attribute-name]:{prefix}?XPath(SEP[attribute-name]:{prefix}?XPath)*"`

**attrs** operates similarly to **attr** except that it may set multiple attributes.  The first character in it's argument string it taken as a separator character and used as such throughout.
```
dexter source
   <img src="notfound.jpg" 
    dx:attrs="|src:data/profile/icon|title:data/profile/description" />
```
```
renders as
   <img src="john.jpg" title="Something about me" />
```

> ## dx:cattr ##
> > condition attribute substitution

> `dx:cattr="[attribute-name]:{prefix}?XPath(WS)XPath"

**cattr** syntax requires 2 XPath expressions: the first to determine the data value, the second to be used as a test.  Unless that test evaluates to _true_, the attribute will not be created at all in the output document.

Whitespace is the implied separator between the two expressions.  If you need whitespace as part of your XPath expression, consider using **cattrs** instead.

```
data.xml
<result>
   <default>two</default>
   <values>
      <value>one</value>
      <value>two</value>
      <value>three</value>
   </values>
```
```
dexter source

<options dx:each="result/values" dx:text="." dx:cattr="selected:'true' string(.)=string(../../default)">option</option>
```
```
renders as
   <option>one</option>
   <option selected='true'>two</option>
   <option>three</option>
```


> ## dx:cattrs ##
> > multiple condition attribute substitution

> `dx:attr="SEP[attribute-name]:{prefix}?XPath(SEP)XPath(SEP)[attribute-name]:{prefix}?XPath"`

Like **attrs**, **cattrs** operates on multiple attributes but, like **cattr**, it's syntax includes an additional test. Unlike **cattr**, **cattrs** is not sensitive to whitespace as it uses an explicit seperator.  To rewrite the **cattr** example using cattrs:
```
dexter source

<options dx:each="result/values" dx:text="." dx:cattrs="|selected:'true'|string(.)=string(../../default)">option</option>
```