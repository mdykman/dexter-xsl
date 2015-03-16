## New Release dexter-0.9.9 final ##

**Dexter** is a tool for producing XSLT 1.0-compliant stylesheets to style arbitrary XML data into human-readable XHTML  using the design document as the source. Thus changes to the visual layout may be propagated directly to the XSLT through a predictable, automated process.  While it can be used with many other XML formats such as WML or RSS, formats with complex attribute-value structures such as SVG will be supported with extension modules in the future.

Dexter allows users to define extended attributes which can be associated with descriptors and editors. These attributes can be embedded into any well-formed XML document. The resulting embellished document, the 'source', is then input to the dexter engine.  This will generate one or more XSLT stylesheets describing the input document interpolating editor modifications and any instructions specified by the descriptors.

Operations are specified exclusively with extended attributes embedded in per-module custom namespaces so web browsers will continue to see the decorated source exactly as the web designer saw it before.  Thus, the changes we effect while preparing the design document as source for dexter are effectively transparent to all but dexter.

## Why Dexter? ##
> [The Case for XSL](CaseForXSL.md), my argument for a not-so-new approach to document production.

> More discussion can be found at [Introducing Dexter](IntroducingDexter.md)


---

> ### **Input Documents** ###

Dexter does not validate input documents, but does expect them to be strictly well-formed XML or XHTML as it uses the JVM's default configured JAXP parser to produce the Document object against which dexter performs it's operations.  This constraint helps guarantee that the documents produced from the resulting XSLT will accurately reflect the input document.

Dexter is designed to handle arbitrarily complex input documents so long as they are well-formed.


---

> ## Documentation ##
I recommend you check out the [first example](FirstExample.md) to get a feel for exactly what dexter does.

Please refer to the [User Manual](UserManual.md) for details on setup and operation.