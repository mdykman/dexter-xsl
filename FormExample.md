#demonstrating dd:form.

**data.xml**
```
<?xml-stylesheet href="source.xml.xsl" type="text/xsl" ?>
<result>
        <formdata>
                <first>Michael</first>
                <last>Dykman</last>
                <size>tall</size>
                <adult>yes</adult>
                <senior>no</senior>
                <story>this is my story</story>
                <clicker>press this</clicker>
        </formdata>
</result>
```
**source.xml**
```
<form dd:form="*/formdata" name="userform">
        <input name="first" /> <br/>
        <input name="last" /> <br/>
        <input type="radio" name="size" value="tall" />tall
        <input type="radio" name="size" value="short" />short <br/>
        <input type="checkbox" name="adult" value="yes" />adult <br/>
        <input type="checkbox" name="senior" value="yes" />senior <br/>
        <textarea name="story">
        some lame content
        </textarea><br/>
        <input name="clicker" type="submit" value="go!" />
</form>
```
Running the command
```
dexter.sh source.xml
```
produces the stylesheet `source.xml.xsl`.  Applying that template to data.xml produces
```
<form name="userform">
        <input value="Michael" name="first"> <br>
        <input value="Dykman" name="last"> <br>
        <input checked="true" name="size" type="radio" value="tall">tall
        <input name="size" type="radio" value="short">short <br>
        <input checked="true" name="adult" type="checkbox" value="yes">adult <br>
        <input name="senior" type="checkbox" value="yes">senior <br>
        <textarea name="story">this is my story</textarea><br>
        <input value="press this" name="clicker" type="submit">
</form>
```