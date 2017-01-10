# Repository DBpedia Ontologies Mining

## Context

Work started as a project at [TELECOM Nancy](http://telecomnancy.univ-lorraine.fr/) (Knowledge based system - SBC class)

It aims at checking the soundness and completeness of DBpedia ontologies (DBpedia categories, DBpedia Ontology and
YAGO) w.r.t. data. To do so, we classify DBpedia pages w.r.t. page properties in a concept lattice using FCA
(Formal Concept Analysis). Then, we define the annotation of a concept lattice to associate ontology classes to a
concept. Finally, implications are extracted from the lattice and compared with the axioms of the three considered
ontologies.

## Files analyzed

* 2016-04 data set of DBpedia

## Execution

TBC

## Dependencies

### Java dependencies

* [Jackson](http://wiki.fasterxml.com/JacksonHome) (JSON parsing) - Apache License 2.0
    * Available as JAR files inside /DBPediaAnalyzer/lib/ to be linked

### Python dependencies

* [Matplotlib](http://matplotlib.org/) - [license](http://matplotlib.org/users/license.html)
* [Pymongo](https://docs.mongodb.org/getting-started/python/query/)

### Program dependencies

* [SOFIA](https://github.com/AlekseyBuzmakov/FCAPS) - FCA program to generate the lattice. Available also through
a [docker](https://hub.docker.com/r/ecirtap/sofia/).

### Access to a SPARQL endpoint

To execute Java programs, an access to a SPARQL endpoint is needed. Its access can be configured 
in the ServerQuerier class by changing `SERVER_BASE_URL`

For our work, a Virtuoso server has been used. It is not available for outside queries.

## License

This program is under GNU GPL v3 (see LICENSE file for more info).

## Authors

* Soline Blanc
* Damien Flament
* Thomas Herbeth
* Pierre Monnin
* Sylvain Vissière-Guerinet
