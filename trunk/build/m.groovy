import groovy.xml.MarkupBuilder

def writer = new StringWriter()
def mb = new MarkupBuilder(writer)

mb.top() { [
	foo(bar:'hello world', zazz:'thingy'){}, 
	bar(bar:'hello world', zazz:'thingy'){}] }
println writer.toString()


