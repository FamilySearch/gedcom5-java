Here is a parser that converts GEDCOM files to a _de facto_ object model.

De Facto object model
---------------------

_De Facto_ means _"In fact or in practice; in actual use or existence,
regardless of official or legal status."_

The parser converts GEDCOM files to an object model that includes all of the
information found in the majority of GEDCOMs in the wild.
The model includes additional tags over those in the official GEDCOM standard,
because they are commonly used in GEDCOM files, and excludes a few tags from
the official GEDCOM standard that are never or only rarely used.

Further, most of the information from GEDCOMs that cannot be represnted directly
in the model is represented as _extensions_ so it is not lost.

The object model is able to represent _all_ of the information found in nearly
50% of the 7000 GEDCOMs submitted to [WeRelate.org](http://www.werelate.org),
over the past five years.  This may not sound like a large percentage, but
due to the standard not being updated in over 10 years, nearly everyone has
added their own custom tags. So having a simple object model represent all
tags found in nearly 50% of GEDCOMs in the wild is an accomplishment.

Additional information found in the GEDCOMs is represented in the model by
extending the objects in the model with the ability to store additional tags.
Using this approach, the object model is able to represent all of the
information found in 94% of the GEDCOMs submitted to WeRelate.  (It's not 100%
because GEDCOM data represented as strings in the model can't be extended with
additional tags.)

The object model has the normal classes you'd expect for a GEDCOM-based object model:
people, families, source citations, sources, notes, repositories, etc.
The purpose of this project is not to propose a new object model, but to _expose_
an object model that is currently used by genealogists and make it easy to work with.

A new proposed object model could use this project to convert existing GEDCOM files
to the new model by first converting them to the _de facto_ object model, then
transforming the objects into the proposed object model.

For more information about the object model, see the doc directory (coming soon).

Extendible
----------

Developers can add custom extensions to the model.  An extension might annotate
people with warnings about suspicious dates for example.

Tools
-----

The project includes three parsers:

* From GEDCOM to a the de facto object model: the object model and any
extensions can be saved as a json file.

* From GEDCOM to a general tree-based object model: captures everything within
the GEDCOM file.  The tree-based object model can also be saved as a json file.

* From json to the de facto object model or the tree-based object model.

as well as a GEDCOM export tool:

* From the de facto object model to GEDCOM.

Round-trippable
---------------

It is possible to do a round-trip: parse a GEDCOM file into the object model,
save it to json, read it back from json, and export it back to GEDCOM, without
any loss of information for the vast majority (94%) of GEDCOM files.

The round-trip capability allows anyone to create programs that read gedcom files,
do interesting things like generate warnings for suspicious dates in the GEDCOM,
allow the user to correct the warnings, and save the information back as a GEDCOM
file without loss of information from the original GEDCOM.

Roadmap
-------

This project was posted only recently.  It may have bugs.  Use at your own risk.
If you find bugs, please report them.
