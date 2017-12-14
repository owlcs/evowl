# EvOWL - A web-based ontology evaluation framework

The overarching goal of EvOWL is to provide a centralised current Knowledge Base about ontologies on the web and their metrics and a wide range of services to access them.

Its goals are:
* Offering a wide range of metrics for ontologies on the web. These can serve, for example, as the basis for ontology snapshot surveys (which are the most used OWL constructs) and ontology experiments (sample creation, performance prediction).
* Providing automated quality control for deployed ontologies, testing, among others, whether the deploy location of the ontology is reachable, which OWL API versions it can be parsed with (3, 4 and 5), and in which OWL profiles it is expressed. Profile violations are currently listed, but not explained.
* Provide a comprehensive ontology of ontology metrics and measurements (EV.owl). This ontology describes the nature of all metrics, making it possible to interpret and analyse instance data more reliably.

Motivation:
* Reports mentioning an ontology have an inconsistent way to present metrics of that ontology. For example, it is often not clear what it means to have 100 axioms, or what should be reported about an ontology in the first place. We aim to provide a service that just generates a summary of an ontology, that can then be pasted directly into a report.
* A lot of effort is spend by repository managers such as the OBO Foundry to ensure the quality of the ontologies they host. Leaving quality control up to the ontology developers is often unsatisfactory. The EvOWL framework comes with a standardised notion of quality and a full fledged quality badge system, and provides a more detailed picture of ontology quality than most services currently used by repositry owners.
* Running experiments often requires the creation of ontology samples, either from within a particular repository (like BioPortal) or across all available ontologies. As we expose our measurements as a SPARQL endpoint, it will be much easier to create appropriate samples, such as "Give me all EL ontologies in BioPortal with more than 1000 axioms".

A reference implementation can be found [here](http://google.com). 

## Summary
The focal point of the EvOWL framework is the **ontology deploy location**, or more precisely, the URL that points to an ontology on the web. This is probably a controversial decision, for two reasons:
1. It precludes the use of ontologies that are not on the web or for which no publicly accessible URL exists.
2. As it allows for the use of aliases and redirects, duplication is strictly speaking a possibility (both the alias and the deploy location could be added seperately). In the future, we will make it possible to declare aliases, for example the PURLs that are used by the OBO Foundry, and the exact deploy location, as the *same* using owl:sameAs.

The decision is motivated by (1) the necessity to have an identifier of the ontology that always works (not all ontologies have meaningful ontology ids for example, and filenames alone are two unsafe for redundancy elimination) and (2) the necessity to have an easy way to keep the metrics current, by regularly downloading the latest deployed resource.

Users can add a URL to the repository using either a Web UI or a web service. A special harvesting server regularly asks for new URLs, processes them using a set of specialised harvesters (programs that extract the metrics). The evowl web server than asks for these metrics, and loads them into its triple store. 

The EvOWL framework has the following main components:

* The EvOWL Metrics Server
* The EvOWL Harvesting Server
* The EvOWL Badge System
* The EvOWL UI


## The EvOWL Metrics Server
The EvOWL Metrics Server (EvMS) is deployed as a set of Tomcat modules, plus a number of scripts for setting the server up and updating the knowledge base.
The EvMS contains the following sub-components:
* A standard RDF4J workbench to manage the triple store
* A standard RDF4J server instance to query the knowledge base, either using SPARQL or various web services
* The metrics server application, which offers a set of specialised web services on top of the knowledge base, such as retrieval of metrics,  