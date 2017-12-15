package owl.cs.evowl.ui.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

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

	Layout vl_main = new VerticalLayout();
	ComboBox<String> selectg = new ComboBox<>("Select Group");
	final private MetricsServer server;

	public AllOntologiesView(MetricsServer server) {
		this.setWidth("100%");
		this.server = server;

		
		selectg.setItems(server.getGroups());
		selectg.addValueChangeListener(v -> changeSelection(v));
		addComponent(new Label("<h1>All Ontologies</h1>", ContentMode.HTML));
		addComponent(selectg);
		addComponent(vl_main);

	}

	private void changeSelection(ValueChangeEvent<String> v) {
		List<Ontology> ontologies = server.getOntologyEvowl(selectg.getValue());
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
		vl_main.addComponent(grid);
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
