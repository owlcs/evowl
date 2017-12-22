package owl.cs.evowl.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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

public class AllOntologiesView extends ViewCanvas implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1220761697755407873L;

	/**
	 * 
	 */

	Map<String, Grid<Ontology>> groupcache = new HashMap<>();
	Layout vl_main = new VerticalLayout();
	ComboBox<String> selectg = new ComboBox<>("Select Group");

	public AllOntologiesView(MetricsServer server, Navigator navigator) {
		super(server, navigator,"All Ontologies");
		vl_main.setSizeFull();
		selectg.setItems(server.getGroups());
		selectg.addValueChangeListener(v -> changeSelection(v));
		getMain().addComponent(selectg);
		addComponent(vl_main);
	}

	private void changeSelection(ValueChangeEvent<String> v) {
		vl_main.removeAllComponents();
		String s = selectg.getValue();

		if (!groupcache.containsKey(s)) {
			List<Ontology> ontologies = getServer().getOntologyEvowl(s);
			Grid<Ontology> grid = new Grid<>();

			grid.setItems(ontologies);
			grid.addColumn(Ontology::getDownloadURL).setCaption("URL");
			grid.addColumn(p -> mainBadgeHTML(p), new HtmlRenderer())
					.setCaption("EvOWL").setWidth(190).setResizable(false).setStyleGenerator(o -> "bggrey");
			grid.addColumn(p -> allBadgeHTML(p), new HtmlRenderer())
					.setCaption("Breakdown").setWidth(400).setResizable(false).setStyleGenerator(o -> "bggrey");
			grid.setWidth("100%");
			grid.setHeight(38*(ontologies.size()+1)+18+"px");
			grid.addStyleName("allont");
			groupcache.put(s, grid);
		}
		vl_main.addComponent(groupcache.get(s));
		vl_main.setSizeFull();
	}

	private String mainBadgeHTML(Ontology o) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style=\"background-color: #7e928e;\"><a href=\"" + EvOWLMetrics.BADGEHOME
				+ "\"><img class=\"gwt-Image\" src=\"VAADIN/themes/evowlTheme/img/"
				+ o.getEvowlMainBadge().getBadgeName() + "\" alt=\"" + o.getEvowlMainBadge().getBadgeName()
				+ "\" height=\"30\" style=\"\" /></a></div>");

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
			sb.append("<div style=\"background-color: #7e928e;\"><a href=\"" + EvOWLMetrics.BADGEDEPLOYLOCATIONRAW
					+ "\"><img class=\"gwt-Image\" src=\"VAADIN/themes/evowlTheme/img/" + b.getBadgeName() + "\" alt=\""
					+ b.getBadgeName() + "\" height=\"30\" width=\"30\" style=\"\" /></a></div>");
		}
		return sb.toString();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getParameters() == null || event.getParameters().isEmpty()) {
			vl_main.addComponent(new Label("Nothing to see here, " + "just pass along."));
			return;
		} else {
			String ps = event.getParameters();
			String[] pas = ps.split(";");
			for(String s:pas) {
				if(s.startsWith("url=")) {
					String url = s.substring(4);
					selectg.setValue(url);
				}
			}
			
		}
	}
}
