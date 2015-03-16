Dexter generates XSL stylesheets using any well-formed XML document as input. Dexter-defined attributes can be added to specify transformation rules in a design document such as an XHTML example layout.

Dexter grew from a need: the commercial web development industry is under ever-mounting pressure to respond to rapidly evolving user interface requirements. All too often, even with the most careful planning, user interface upgrades require significant portions of an application to be re-coded. Over time, applications become unmaintainable as they are hacked to accomodate the latest vision.

Why one wonders, have mainstream web developers all but ignored XSLT? Certainly not the learning curve; competent web developers absorb new technologies constantly and XSLT is now over 10 years old. The short answer is: maintainence. I have a full-length [rant](CaseForXSL.md) on the subject which explains how Dexter solves this problem.

Simplicity and conciseness are the primary design goals of dexter.

Most Dexter operators are xml-agnostic for performing data substitution, conditional evaluation and loops.

Dexter was designed to be extendable so there is also a small family of html-specific operators.

A good place to begin understanding how dexter can be used can be found in the [first example](FirstExample.md).

Please refer to the [User Manual](UserManual.md) for usage details.  For a simple walk-though of the process, see [A Walkthrough](AWalkthrough.md)

If you want to see some more random examples, try [here](GeneralExamples.md).