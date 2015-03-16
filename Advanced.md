## dx:sub ##
> `dx:sub="XPath"`
> `dx:sub="ref:XPath"`

**sub** is used to generate auxiliary stylesheets which can be shared between other templates.

Note that an element using the **sub** operator must have an id.  That id is used in file naming, so please avoid the use of non-alphanumeric characters except dash and underscore ('-' and '`_`').

```
data.xml
<result>
  ...
   <copyright>
      <year>2011</year>
      <owner>Somebig Inc.</owner>
   </copyright>
</result>

```

```
dexter ssource
<html>
<body>
 ...
<div id="footer" dx:sub="result/copyright">
  common company info which is need on every page
    &copy <span dx:text="year" /> by <span dx:text="owner" />
<div>
</body>
</html>
```