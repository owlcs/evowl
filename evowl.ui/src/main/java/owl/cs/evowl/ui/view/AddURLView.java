package owl.cs.evowl.ui.view;

import java.util.List;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import owl.cs.evowl.ui.MetricsServer;
import owl.cs.evowl.util.Ontology;

public class AddURLView extends VerticalLayout {

	private static final long serialVersionUID = -8224929117517039797L;


	public AddURLView(MetricsServer server) {
	// Have some data
	List<Ontology> ontologies = server.getOntologies();

	// Create a grid bound to the list
	Grid<Ontology> grid = new Grid<>();
	grid.setItems(ontologies);
	grid.addColumn(Ontology::getDownloadURL).setCaption("URL");
	grid.addColumn(Ontology::getEvowlMainBadge).setCaption("EvOWL?");

	addComponent(new Label("<h1>Add URL</h1>",ContentMode.HTML));
	addComponent(grid);
}
}
