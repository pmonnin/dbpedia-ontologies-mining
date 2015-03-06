Repository sbc-treillis
=======================

For the SBC project at TELECOM Nancy

Authors
-------
* Soline Blanc
* Damien Flament
* Thomas Herbeth
* Pierre Monnin
* Sylvain Vissière-Guerinet

Useful files
------------

* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/ : french data
* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/en/ : english data (warning: big files)
* http://wiki.dbpedia.org/Ontology : ontologies (warning: some files are also available on previous links)


API Colibri
-----------

Used to build the lattice. Available here : https://code.google.com/p/colibri-java/


Access to SPARQL endpoint
-------------------------

We have set up a Fuzeki available through eduroam or UL VPN here : http://sbc2015.telecomnancy.univ-lorraine.fr:8080/

You can query it by selecting the projects_fr via the control panel. The URL
is http://sbc2015.telecomnancy.univ-lorraine.fr:8080/sparql.tpl

You can query it from the Java code with two GET variables :
* query : where you put the query encoded with UTF-8
* output : usually the JSON format (output=json)
