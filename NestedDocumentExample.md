#demonstrate nested documents

**data.xml**
```
<?xml-stylesheet href="source.xml.xsl" type="text/xsl" ?>
<result>
        <cat>
                <stuff>stuff-data</stuff>
        </cat>
        <thing1>
                <stuff>stuff-data1</stuff>
        </thing1>
        <thing2>
                <stuff>stuff-data2</stuff>
        </thing2>
</result>
```
**source.xml**
```
<xxx>
        <www dx:value="*/cat/stuff" />
        <yyy id="yyy" dx:sub="*/thing1">
                <span dx:ghost="" dx:value="stuff"/>
                <zzz id="zzz" dx:sub="/*/thing2" dx:value="stuff" />
        </yyy>
</xxx>
```
Running the command
```
dexter.sh source.xml
```
produces 3 stylesheets: `source.xml.xsl`,  `source.xml-yyy.xsl`  and `source.xml-zzz.xsl`.  Applying `source.xml.xsl` to `data.xml` produces
```
<xxx>
        <www>stuff-data</www>
        <yyy id="yyy">
                stuff-data1
                <zzz id="zzz">stuff-data2</zzz>
        </yyy>
</xxx>
```

The inner templates may also be used independently to  format data within it's own domain.