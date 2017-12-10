package owl.cs.evowl.ui.view;

import java.util.List;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

import owl.cs.evowl.ui.MetricsServer;
import owl.cs.evowl.util.OWLBadge;
import owl.cs.evowl.util.Ontology;

public class AllOntologiesView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1220761697755407873L;

	/**
	 * 
	 */

	public AllOntologiesView(MetricsServer server) {
		this.setWidth("100%");
		List<Ontology> ontologies = server.getOntologies();

		// Create a grid bound to the list
		Grid<Ontology> grid = new Grid<>();
		grid.setItems(ontologies);
		grid.addColumn(Ontology::getDownloadURL).setCaption("URL");
		grid.addColumn(p -> mainBadgeHTML(p), new HtmlRenderer()).setCaption("EvOWL").setWidth(200);
		grid.addColumn(p -> allBadgeHTML(p), new HtmlRenderer()).setCaption("Breakdown").setWidth(600);
		grid.setWidth("100%");

		addComponent(new Label("<h1>All Ontologies</h1>", ContentMode.HTML));
		addComponent(grid);
	}

	private String mainBadgeHTML(Ontology o) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"www.google.de\"><img src=" + o.getEvowlMainBadge().getBadgeLocation() + " alt=\""
				+ o.getEvowlMainBadge().getMetric() + "\" width=\"100px\" /></a>");
		return sb.toString();
	}

	private String allBadgeHTML(Ontology o) {
		StringBuilder sb = new StringBuilder();
		for (OWLBadge b : o.getBadges()) {
			sb.append("<a href=\"www.google.de\"><img src=" + b.getBadgeLocation() + " alt=\"" + b.getMetric()
					+ "\" height=\"30\" width=\"30\" /></a>");
		}
		return sb.toString();
	}
}
