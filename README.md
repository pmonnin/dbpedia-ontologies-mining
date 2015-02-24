Repository sbc-treillis
=======================

For the SBC project at TELECOM Nancy

Authors
-------
Soline Blanc
Damien Flament
Thomas Herbeth
Pierre Monnin
Sylvain Vissière-Guerinet

Useful files
------------

* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/article_categories_fr.ttl.bz2 : ressource URL to category URL
* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/category_labels_fr.ttl.bz2 : category URL to title
* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/page_links_fr.ttl.bz2
* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/labels_fr.ttl.bz2 : ressource URL to title
* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/skos_categories_fr.ttl.bz2 : category URL to category URL 

Maybe useful files
------------------

* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/topical_concepts_fr.ttl.bz2 : category URL to concept URL
* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/topical_concepts_unredirected_fr.ttl.bz2 : idem
* http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/fr/redirects_fr.ttl.bz2

API Colibri
-----------
https://code.google.com/p/colibri-java/

Access to SPARQL endpoint
-------------------------
que ce soit à partir du vpn ou eduroam pour faire des query faut passer par:

http://sbc2015.telecomnancy.univ-lorraine.fr/project/query en GET


les argument à mettre dedans: 

-query avec pour valeur la requête
     par exemple la query correspondant à 

    SELECT ?p
    WHERE {
     ?p ?c ?d

    }

    c'est: SELECT+%3Fp%0D%0AWHERE+%7B%0D%0A+%3Fp+%3Fc+%3Fd%0D%0A%7D

    tout les %XY c'est les codes héxa des caractères genre "?"->%3F

-output avec la valeur json pour du json


si vous utilisé autre chose que du json demandez moi je ressortirais les arguements nécessaire.


ensuite la tête du json c'est:  
{ "head" : { "vars" : [liste des variables retournées ] },
  "results" : { "bindings" : [ { "variable1" : {"type" : type de la donné, "value" : la valeur} }, { "variable1" : {"type" : type de la valeur, "value" : la valeur} }

                 ]

        }

}

donc en gros on a un dictionnaire qui à pour clefs head et results.

head donne accès à un dictionnaire qui a pour clef vars.
vars donne accès à la liste des variables retournées

results donne accès à un dictionnaire qui à pour clef bindings.
bindings donne accès à une liste de résultat chaque élément de la liste est un dictionnaire qui a pour clefs les variables demandées.
chacune de ces variable donne accès à un dictionnaire qui a deux clefs type et value.
type donne accès à une valeur qui est le type de la donné renvoyé.
value donne accès à la valeur de la variable.