There are 2 operators in Dexter for substituting data into the target document.
  * dx:text
  * dx:cpy


## dx:text ##

> text substitution
> `dx:text="{prefix}?XPath"`

**text** replaces all child nodes of the current source element with the text content of the data node specified by the path.

A optional prefix '!' may be used to retain the value in the source document as a default:
```
 data.xml

<xml>
  <person>
     <firstname>John</firstname>
  </person>
</xml>
```
```
 dexter source 
<div class="person" dx:text="person/firstname">unknown</div> 
<div class="person" dx:text="!person/middlename">unknown</div> 
<div class="person" dx:text="person/lastname">unknown</div> 
```
> this renders as
```
<div class="person">John</div> 
<div class="person">unknown</div> 
<div class="person"></div> 
```

> ### Attribute Value Template ###

As with the `dx:attr` familty, `dx:text` also supports the [attribute value template notation](http://www.w3.org/TR/xslt#dt-attribute-value-template) meaning that one or more XPath expressions can be mixed with literal text.

```
<div class="person" 
dx:text="His name is {person/firstname} {person/middlename} {person/lastname}">unknown</div> 
```

A failure to find a non-empty result for any of the supplied XPath  expressions (unless the default operater is supplied) may cause the entire entry to render as an empty string.  I am currently rethinking the logic of that.

## dx:cpy ##

> node substitution
> `dx:cpy="{prefix}?XPath"`

**cpy** replaces all child nodes of the current source element with the child nodes of the data node specified by the path.

A optional prefix '!' may be used to retain the value in the source document as a default.

A optional prefix '#' may be used to cause the children of the selected data nodes to be copied.

> ### example 1 ###
```
data.xml

<xml>
  <style>
    <s1>
      <a href="http://code.google.com/p/dexter-xsl">Dexter</a>
    </s1>
  <style>
</xml>
```


```
 dexter source 
<div dx:cpy="style/s1">unknown
</div> 
<div dx:cpy="!style/s2">
   No link was found.  Try  <a href="www.google.com">here</a>
</div> 
<div dx:cpy="style/s3">unknown
</div> 
```

> this renders as
```
<div>
      <a href="http://code.google.com/p/dexter-xsl">Dexter</a>
    
</div> 
<div>
   No link was found.  Try  <a href="www.google.com">here</a>
</div>
<div></div> 
```

> ### example 2 ###
```
data.xml
<xml>
   <profiles>
      <profile>
        <username>bob</username>
        <bio>
<div>
some mixed html here
</div>
        </bio>
      </profile>
   </profiles>
</xml>
```
```
 dexter source 
<div dx:each="xml/profiles/profile">
   username: <span dx:text="username">user</span><br/>
   bio: <p dx:cpy="#bio">bio data</bio>
</div> 
```

> this renders as
```
<div dx:each="xml/profiles/profile">
   username: <span>bob</span><br/>
   bio: <p><div>
some mixed html here
</div></p>
</div> 

```