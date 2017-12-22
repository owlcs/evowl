package owl.cs.evowl.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import owl.cs.analysis.utilities.WSConfig;
import owl.cs.evowl.ui.view.AddURLView;
import owl.cs.evowl.ui.view.AllGroupsView;
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
public class EvOWLUI extends UI {

	Navigator navigator;
	private static final long serialVersionUID = -6742698853306007200L;
	
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		getPage().setTitle("OWL Ontology Evaluation Center (EvOWL)");
		navigator = new Navigator(this, this);
		final MetricsServer server = new MetricsServer(new WSConfig("http://130.88.193.79", "8080"));
		navigator.addView(State.ADDURL.getName(), new AddURLView(server, navigator));
		navigator.addView("",new StartView());
		navigator.addView(State.GROUPS.getName(), new AllGroupsView(server, navigator));
		navigator.addView(State.ALLONTOLOGIES.getName(), new AllOntologiesView(server, navigator));
		navigator.addView(State.EVOWLUATION.getName(), new OntologyEvowluationView(server, navigator));
		navigator.navigateTo(State.GROUPS.getName());
	}

	@WebServlet(urlPatterns = "/*", name = "evowlServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = EvOWLUI.class, productionMode = false)
	public static class evowlServlet extends VaadinServlet {

		private static final long serialVersionUID = 6596146514499261232L;
	}
	
	public class StartView extends VerticalLayout implements View {
	    public StartView() {
	        setSizeFull();

	        Button button = new Button("Go to Main View",
	                new Button.ClickListener() {
	            @Override
	            public void buttonClick(ClickEvent event) {
	                navigator.navigateTo(State.GROUPS.getName());
	            }
	        });
	        addComponent(button);
	        setComponentAlignment(button, Alignment.MIDDLE_CENTER);
	    }

	    @Override
	    public void enter(ViewChangeEvent event) {
	    }
	}
}
