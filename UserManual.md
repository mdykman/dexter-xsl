# User Manual #

> ## Introduction ##

For quick notes on installation and usage, see [Introduction](Introduction.md)

## XPath 1.0 ##
In keeping with the XSLT 1.0 specification, dexter uses XPath 1.0 (XML Path Language, version 1.0) in many of it's operators. An in-depth discussion beyond the scope of this document, and you should refer to the [XML Path Language (XPath) 1.0 standard](http://www.w3.org/TR/xpath/) for definitive information.

A useful resource for those new to XPath or who desire a refresher in the basics is the [Zvon.org XPath Tutorial](http://zvon.org/xxl/XPathTutorial/General/examples.html), which is available in several languages.


## Dexter Operators ##
> Most of Dexters operators are XML-agnostic; that is to say they are useful regardless of the scheme of XML being manipulated.  Those are the attributes in the 'dx' namespace.  There are a few HTML-specific operators which are in the 'dd' namespace.

Many dexter operators require XPath 1.0 in their arguments with additional, optional prefixes.

> ### dx: xml-agnostic ###

  * [Substitution](Substitution.md) _dx:text,dx:cpy_
  * [Attributes](Attributes.md) _dx:attr,dx:attrs,dx:cattr,dx:cattrs_,
  * [Iteration](Iteration.md) _dx:each_
  * [Conditionals](Conditionals.md) _dx:if,dx:case/dx:default_
  * [Miscellany](Miscellany.md) _dx:ghost,dx:ignore,dx:uniq,dx:cdata,dx:comment,dx:noop_
  * [Advanced](Advanced.md) _dx:sub,dx:template,dx:call_

> ### dd: html-specific ###

  * [Forms](Forms.md) _dd:form,dd:option,dd:options_