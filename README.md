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

Access to SPARQL endpoint
-------------------------
Pour ceux qui ont le vpn et qui veulent taper sur le serveur pour els requêtes suffit de faire une requête GET à l'url suivante: 

 GET http://sbc2015.telecomnancy.univ-lorraine.fr:10000/Project/query


Avec pour argument query et en valuer la requête associé en mettant els code ascii hexa des caractère "spéciaux". dans l'exemple suivant la requête était: 

SELECT ?p
WHERE {
 ?p ?c ?d
}
et ainsi la valeur transmise au serveur :
query=SELECT+%3Fp%0D%0AWHERE+%7B%0D%0A+%3Fp+%3Fc+%3Fd%0D%0A%7D&output=text&stylesheet=%2Fxml-to-html.xsl

la variable stylesheet sert à rien la et il faut précisé un format d'output text ou json ou csv ou xml(je pense que bizarement ça va pas être utilisé le xml xD)

ça fonctionne via eduroam aussi normalement, serveur accessible après j'ai pas fait un essai spécifique, l'url c'est donc sbc2015.telecomnancy.univ-lorraine.fr:80 sur du tcp bien sur