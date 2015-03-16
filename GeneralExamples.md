#demonstrating Dexter's basic features

**data.xml**
```
<data>
        <found value="y" >I found it</found>
        <record>
                <one>number 1</one>
                <two>number 2</two>
                <three>number 3</three>
                <four>number 4</four>
        </record>
</data>
```
**source.xml**
```
<tests>
        <div id="group-1" dx:env="data/found" >
                <div id="test-1" name="found" dx:text="." >
                </div>
                <div id="test-1" name="found" dx:text="the value is `{.}'" />
                <div id="test-2" name="found" dx:attrs="|fakeattribute:the value is `{.}'" />
                <div id="test-3" name="valueoffound" dx:text="@value" />
                <div id="test-4a" name="amfound" dx:if="@value!='y'" />
                <div id="test-4b" name="notfound" dx:if="@value='n'" />
                <div id="test-5a" dx:case="@value='y'"  cond="true" >only one should show with 42</div>
                <div id="test-5b" dx:default=""  cond="false" >only one should show with 42</div>
                <div id="test-6a" dx:case="@value='n'" cond="false" >only one should show with 43</div>
                <div id="test-6b" dx:default="" cond="true"  >only one should show with 43</div>
                <option id="test-7" name="thing" value="C" dx:cattr="checked:'true' @value='n' " />
                <option id="test-8" name="thing" value="D" dx:cattr="checked:'true' @value='y'" />
        </div>


        <div id="group-2" dx:env="data/record">
                        a record
                <div dx:each="*" dx:text="new record {name(.)} = {.}">
                </div>


                <div >
                        <span dx:text="one"></span>
                        <span dx:text="two"></span>
                        <span dx:text="three"></span>
                        <span dx:text="four"></span>
                </div>
        </div>
</tests>
```

running the command
```
dexter.sh source.xml
```

produces the stylesheet `source.xml.xsl`.  Applying that template to data.xml produces

```
<tests>
        <div id="group-1">
                <div id="test-1" name="found">I found it</div>
                <div id="test-1" name="found">the value is `I found it'</div>
                <div fakeattribute="the value is `I found it'" id="test-2" name="found"></div>
                <div id="test-3" name="valueoffound">y</div>
                <div id="test-4a" name="amfound"></div>

                <div cond="true" id="test-5a">only one should show with 42</div>
                <div cond="true" id="test-6b">only one should show with 43</div>
                <option id="test-7" name="thing" value="C"></option>
                <option checked="true" id="test-8" name="thing" value="D"></option>
        </div>


        <div id="group-2">
                        a record
                <div>new record one = number 1</div><div>new record two = number 2</div><div>new record three = number 3</div><div>new record four = number 4</div>


                <div>
                        <span>number 1</span>
                        <span>number 2</span>
                        <span>number 3</span>
                        <span>number 4</span>
                </div>
        </div>
</tests>

```