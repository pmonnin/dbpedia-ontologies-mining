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

All useful files
----------------

* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/ : french data
* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/en/ : english data (warning: big files)
* http://wiki.dbpedia.org/Ontology : ontologies (warning: some files are also available on previous links)

Subset of files to use
----------------------

* DBPedia Ontology T-BOX (available on http://wiki.dbpedia.org/Ontology)
* On http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/en/ :
  * article_categories_en.ttl
  * category_labels_en.ttl
  * genders_en.ttl
  * geo_coordinates_en.ttl
  * geonames_links_en_en.ttl
  * infobox_properties_en.ttl
  * infobox_property_definitions_en.ttl
  * instance_types_en.ttl
  * labels_en.ttl
  * skos_categories_en.ttl
  * topical_concepts_en.ttl
  * pnd_en.ttl
* On http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/ :
  * yago_types.nt
  * yago_taxonomy.nt
  

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
