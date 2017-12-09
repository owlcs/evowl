package owl.cs.evowl.ui.view;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import owl.cs.evowl.ui.MetricsServer;

public class OntologyEvowluationView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8121604620261546970L;

	public OntologyEvowluationView(MetricsServer server) {
		// Have some data
		ComboBox<String> select = new ComboBox<>("Select Ontology");
		select.setItems(server.getOIDs());

		addComponent(new Label("<h1>Ontology EvOWLuation</h1>",ContentMode.HTML));
		addComponent(select);
	}
}
