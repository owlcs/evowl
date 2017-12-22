package owl.cs.evowl.ui.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

import owl.cs.evowl.ui.MetricsServer;
import owl.cs.evowl.util.EvOWLMetrics;
import owl.cs.evowl.util.Group;
import owl.cs.evowl.util.Ontology;

public class AllGroupsView extends ViewCanvas implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1220761697755407873L;

	/**
	 * 
	 */

	Map<String, Grid<Ontology>> groupcache = new HashMap<>();

	Grid<Group> grid = new Grid<>();
	VerticalLayout vl_main = new VerticalLayout();

	public AllGroupsView(MetricsServer server, Navigator navigator) {
		super(server, navigator,"Groups");

		List<Group> ontologies = getServer().getOntologyGroups();
		grid.setItems(ontologies);
		grid.addColumn(Group::getName).setCaption("Group");
		grid.addColumn(p -> mainBadgeHTML(p), new HtmlRenderer()).setCaption("Shiny").setWidth(190).setResizable(false);
		grid.addColumn(p -> allBadgeHTML(p), new HtmlRenderer()).setCaption("Nr. Ontologies").setWidth(400)
				.setResizable(false);
		grid.setWidth("100%");
		grid.setHeight(38*(ontologies.size()+1)+18+"px");

		getMain().addComponent(vl_main);
		vl_main.setSizeFull();
		vl_main.addComponent(grid);
	}

	private String mainBadgeHTML(Group o) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style=\"background-color: #7e928e;\"><a href=\"" + EvOWLMetrics.BADGEHOME
				+ "\"><img class=\"gwt-Image\" src=\"VAADIN/themes/evowlTheme/img/shiny.png"
				+ "\" alt=\"Shiny\" height=\"30\" style=\"\" /></a></div>");

		return sb.toString();
	}

	private String allBadgeHTML(Group o) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style=\"background-color: #7e928e;\">" + o.getOntologyCount() + "</div>");
		return sb.toString();
	}

}
