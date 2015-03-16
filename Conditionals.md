There are three Dexter operators for addressing conditional layout flow.

  * dx:if
  * dx:case/dx:default

The argument strings for dx:if and dx:case are identical expecting pure XPath for both dx:if and dx:case. No Dexter-specific prefixes are allowed here.

## dx:if ##
> `dx:if="XPath"`
**if** is simple.  If the XPath expression evaluates to true, the element and it's children are rendered.  Otherwise, neither the element nor it's children will be rendered.  There is no dx:else, for chaining if/elseif logic, refer to **case/default**

```
data.xml

<xml>
  <customer>
     <person preferred="true">Bob</person>
     <person>Carol</person>
     <person>Ted</person>
     <person preferred="true">Alice</person>
  </customer>
<xml>
```

```
dexter source

   <div id="person" dx:each="xml/customer/person">
       <span dx:text="$person">name here</span>
       <span dx:if="$person/[@preferred='true']">is special</span>
   </div> 
```

```
renders as:
   <div id="person">
       <span>John</span>
       <span>is special</span>
   </div> 
   <div id="person">
       <span>Carol</span>
   </div> 
   <div id="person">
       <span>Ted</span>
   </div> 
   <div id="person">
       <span>Alice</span>
       <span>is special</span>
   </div> 
```

> ### dx:case/dx:default ###
> `dx:case="XPath"`
> `dx:default=""`

**case**, when specified on contiguous elements, gathers those elements into a single switch-like statement.  That group of contiguous **case**'ed elements may optionally be terminated with a **default** operation which takes no argument.
```
<div dx:each="xml/customer">
  <div dx:case="person[@preferred='true']">
  .. special layout for the preferred customer
  </div>
  <div dx:case="person/[icon]">
 .. standard layout for person with an icon
  </div>
  <div dx:default="">
 .. fallthrough layout for everyone else.
  </div>
</div>
```