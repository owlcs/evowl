Process

0. Prepare environment

	1. Ubuntu
	2. Tomcat
	3. Shiny server
	
1. Build tools (maven install metrics)
	
	1. metrics.harvest
		
		* url
		* oa3
		* oa4
		* oa5
		* hermit
		
	2. metrics.utilities
		
		* utilities
		* oa3
		* oa4
		* oa5
		
	3. metrics.server
	
	4. metrics.ui
	
	5. owlbadge
	
	6. shinyowl
	
2. Prepare pipeline

	1. Deploy metrics.server
		* Post triples in case of migration
	2. Deploy metrics.ui
		* Configure metrics.server access (urls)
	3. Deploy owlbadge
	4. Deploy shinyowl
	5. Prepare data harvesting pipeline
		1. copy harvest.url
		2. copy all others
		3. deploy script: for all jar in others, java -jar thatjar
		4. store harvest script: for all *.rdf -> add metric ws
	
	
	