
Didi.Ajax = new Object();

Didi.Ajax.XSLTcache = new Array();

Didi.Ajax.xslUri = function(doc) {
	var c = doc.getFirstChild();
	alert(c.getNodeType());
}

Didi.Ajax.create_handler = function(url,method,apre,apost,children)
{
	var handler_func = function(id,params,pre,post)
	{
		var ret = true;
		var element = document.getElementById(id);
		if(ret && (pre)) ret = pre(element,params);
		if(ret && (apre)) ret = apre(element,params);
		
		if(ret)
		{
			var callback = function(status,newELement)
			{
				alert("callback");
				var ret = true;
				if(ret &&(apost)) ret = apost(status,newELement,element);
				if(ret &&(post)) ret = post(status,newELement,eLement);
				if(ret && newElement)
				{
					if((!status) || (status >= 200 && status < 300))
					{
						ret = Didi.Ajax.replace_node(id, newElement,children);
					}
				}
				return ret;
			};
			ret = Didi.Ajax.invoke_request(url,params,method,callback);
		}
		return ret;
	}
	return handler_func;
}
Didi.Ajax.transformer = function(uri) {
	if(this.XSLTcache[uri])
	{
		return this.XSLTcache[uri];
	}
	else
	{
		var xslt = this.createTransformer(uri);
		this.XSLTcache[uri] = xslt;
		return xslt;
	}
}
Didi.Ajax.createTransformer = function(uri) {

	xslt = this.loadXML(uri);

	if(!xslt)
	{
		return function(xml) {
			alert("failed to load stylesheet.");
		}
	}
	else if(typeof XSLTProcessor != "undefined")
	{
		return function(xml) {
			var processor = new XSLTProcessor();
			processor.importStylesheet(xslt);
			return processor.transformToFragment(xml,document);
		}
	}
	else if("transformNode" in xslt)
	{
		return function(xml) {
			return xml.transformNode(xslt);
		}
	}
	else
	{
		return function(xml) {
			alert("XSLT does not appear to be supported in your browser.");
		}
	}
}

Didi.Ajax.createDocument = function()
{
	if(document.implementation && 
		document.implementation.createDocument)
	{
		return document.implementation.createDocument("","",null);
	}
	else if(typeof ActiveXObject != "undefined")
	{
		return new ActiveXObject("MSXML2.DOMDocument");
	}
	else
	{
		return null;
	}
}
Didi.Ajax.loadXML = function(url)
{
	var doc = this.createDocument();
	if(doc)
	{
		doc.async = false;
		doc.load(url);
	}
	return doc;
}

// utility functions
Didi.Ajax.show_request = function(msg,uri,params,method) {
	var s = method + ": " + uri ;
	s = s + "\n";
	for(key in params)
	{
		s = s + key + "=" + params[key] + ", ";
	}
	alert(msg + " " + s); 
}

// default implementation
// these function should be overriden to bind to you favorite jaxa implementation
// an example of a prototype implementation is provided below for reference
Didi.Ajax.invoke_request = function(uri,params,method,callback) {
 	Didi.Ajax.show_request('not implemented', uri,params,method);
}

Didi.Ajax.replace_node = function(id,el,ch) {
	alert('not implemented');
}
