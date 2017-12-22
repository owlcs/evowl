package owl.cs.evowl.ui.view;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import owl.cs.evowl.ui.MetricsServer;
import owl.cs.evowl.util.Metric;
import owl.cs.evowl.util.Ontology;

public class OntologyEvowluationView extends ViewCanvas implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8121604620261546970L;
	ComboBox<String> select = new ComboBox<>("Select Ontology");
	Layout vl_main = new VerticalLayout();

	public OntologyEvowluationView(MetricsServer server, Navigator navigator) {
		super(server, navigator, "Evaluation");
		vl_main.setWidth("100%");
		vl_main.setHeight("100%");
		select.setItems(server.getOIDs());		
		select.addValueChangeListener(v -> changeSelection(v));
		getMain().addComponent(select);
		getMain().addComponent(vl_main);
	}

	private void changeSelection(ValueChangeEvent<String> v) {
		vl_main.removeAllComponents();
		String s = select.getValue();
		
		Ontology o = getServer().getOntologyMetrics(s);
		StringBuilder sb = new StringBuilder();
		sb.append("<div>");
		for(Metric m : o.getMetrics()) {
			sb.append("<div>"+m.getID()+": "+m.getValue()+"</div><br />");
		}
		sb.append("</div>");
		Label metrics = new Label(sb.toString(),ContentMode.HTML);
		vl_main.addComponent(metrics);
	}
}
