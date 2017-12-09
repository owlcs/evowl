package owl.cs.evowl.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import owl.cs.analysis.utilities.WSConfig;
import owl.cs.evowl.ui.view.AddURLView;
import owl.cs.evowl.ui.view.AllOntologiesView;
import owl.cs.evowl.ui.view.OntologyEvowluationView;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("evowlTheme")
public class evowl extends UI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6742698853306007200L;
	VerticalLayout vl_main = new VerticalLayout();
	final MetricsServer server = new MetricsServer(new WSConfig());
	VerticalLayout vl_addurl = new AddURLView(server);
	VerticalLayout vl_allontologies = new AllOntologiesView(server);
	VerticalLayout vl_evowl = new OntologyEvowluationView(server);

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();
		
		MenuBar barmenu = new MenuBar();
		
		final Label selection = new Label("-");
		barmenu.addItem("Add URL", null, e -> update(State.ADDURL));
		barmenu.addItem("All Ontologies", null,  e -> update(State.ALLONTOLOGIES));
		barmenu.addItem("Ontology Evowluation", null,  e -> update(State.EVOWLUATION));
		
		layout.addComponent(barmenu);
		layout.addComponent(selection);
		layout.addComponent(vl_main);
		
		setContent(layout);
	}

	private void update(State state) {
		vl_main.removeAllComponents();
		switch(state) {
		case ADDURL:
			vl_main.addComponent(vl_addurl);
			break;
		case ALLONTOLOGIES:
			vl_main.addComponent(vl_allontologies);
			break;
		case EVOWLUATION:
			vl_main.addComponent(vl_evowl);
			break;
		default:
			break;
		
		}
	}

	@WebServlet(urlPatterns = "/*", name = "evowlServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = evowl.class, productionMode = false)
	public static class evowlServlet extends VaadinServlet {

		private static final long serialVersionUID = 6596146514499261232L;
	}
}
