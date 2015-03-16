There is one operator in dexter for iterating over data nodes

  * dx:each

### dx:each ###


> iteration/looping
> `dx:each="{prefix}?XPath"`

**each** causes the current element and all of it's children to be repeated for each data node matched by the XPath expression.

A optional prefix '#' may be used to cause the operator to repeat only the child nodes of the current node rather than the node itself.

If the element has an 'id' attribute, it will be used to create an auto-variable.

Note that **each** establishes the context for XPath expressions within it's scope.
### example 1 ###

```
data.xml

<xml>
  <things>
     <thing>watch</thing>
     <thing>teapot</thing>
     <thing>spoon</thing>
     <thing>table</thing>
  <things>
</xml>
```
```
dexter source

  <div dx:each="xml/things/thing" dx:text=".">
  <div>

```

```
 renders as:
  <div>watch</div> 
  <div>teapot</div> 
  <div>spoon</div> 
  <div>table</div> 
```

### example 2 ###
```
dexter source

   <table dx:each="#xml/things/thing">
       <tr><td dx:text=".">data</td></tr>
   </table> 
```

```
 renders as:
   <table>
       <tr><td>watch</td></tr>
       <tr><td>teapot</td></tr>
       <tr><td>spoon</td></tr>
       <tr><td>table</td></tr>
   </table> 
```
Without the '#' prefix in the each expression, dexter would have generated 4 tables.

### example 3 ###
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
       <span dx:if="$person/[@preffered='true']">is special</span>
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