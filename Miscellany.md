**ghost** causes the element it is embedded in to not be rendered while still preforming other embedded operations and processing it's children.

```
dexter source

<div>
  My name is <span dx:ghost="" dx:text="data/name">name</span>.
</div>
```
```
 renders as
<div>
  My name is John.
</div>

```

> ## dx:ignore ##
> `dx:ignore=""`

**ignore** causes the element it is embedded in to be excluded, along with it's children from the resulting XSL stylesheet.  It is convenient to use when one wishes to keep the design document intact after logic has been applied.

```
dexter source
<ul>
   <li dx:each="data/values" dx:text=".">one</li>
   <li dx:ignore="">two</li>
   <li dx:ignore="">three</li>
</ul>
```

> ## dx:uniq ##

> ## dx:cdata ##
> `dx:cdata="XPath"`
> `dx:cdata=""`

**cdata** will in effect wrap the rendered values of all child nodes into a CData node, effectively escaping it.

In the first form, it is effectively escaping the XML rendering of the data selected by the XPath expression.

In the second, no-args form, the child elements of the source doument are escaped.

I often find myself asked to address XML formats which require escaped fragments of other XML formats embedded in them.  Ugly, but true.

> ## dx:comment ##
> `dx:comment="XPath"`

**comment** generates a comment containing the string value of the provided xpath expression.




> ## dx:noop ##
> `dx:noop=""`

**noop** , as it's name suggests, does absolutely nothing.