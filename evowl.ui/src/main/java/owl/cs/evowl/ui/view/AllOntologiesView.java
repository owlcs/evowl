package owl.cs.evowl.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ImageRenderer;

import owl.cs.evowl.ui.MetricsServer;
import owl.cs.evowl.util.EvOWLMetrics;
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
		grid.addColumn(p -> mainBadgeHTML(p), new HtmlRenderer()).setCaption("EvOWL").setWidth(192);
		// grid.addColumn(i -> new
		// ThemeResource("img/"+i.getEvowlMainBadge().getBadgeName()), new
		// ImageRenderer<>())
		// .setCaption("EvOWL");
		grid.addColumn(p -> allBadgeHTML(p), new HtmlRenderer()).setCaption("Breakdown").setWidth(600);
		grid.setWidth("100%");

		addComponent(new Label("<h1>All Ontologies</h1>", ContentMode.HTML));
		addComponent(grid);
	}

	private String mainBadgeHTML(Ontology o) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"" + EvOWLMetrics.BADGEHOME
				+ "\"><img class=\"gwt-Image\" src=\"VAADIN/themes/evowlTheme/img/"
				+ o.getEvowlMainBadge().getBadgeName() + "\" alt=\"" + o.getEvowlMainBadge().getBadgeName()
				+ "\" height=\"30\" style=\"\" /></a>");

		return sb.toString();
	}

	private String allBadgeHTML(Ontology o) {
		List<OWLBadge> badges = new ArrayList<>();
		for (String metric : EvOWLMetrics.badgeorder) {
			for (OWLBadge b : o.getBadges()) {
				if (b.getMetric().equals(metric)) {
					badges.add(b);
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		for (OWLBadge b : badges) {
			sb.append("<a href=\"" + EvOWLMetrics.BADGEDEPLOYLOCATIONRAW
					+ "\"><img class=\"gwt-Image\" src=\"VAADIN/themes/evowlTheme/img/" + b.getBadgeName() + "\" alt=\""
					+ b.getBadgeName() + "\" height=\"30\" width=\"30\" style=\"\" /></a>");
		}
		return sb.toString();
	}
}
