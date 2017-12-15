package owl.cs.evowl.ui.view;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import owl.cs.evowl.ui.MetricsServer;

public class AddURLView extends VerticalLayout {

	private static final long serialVersionUID = -8224929117517039797L;


	public AddURLView(MetricsServer server) {
	
	TextField tf = new TextField();
		
	addComponent(new Label("<h1>Add URL</h1>",ContentMode.HTML));
	addComponent(tf);
}
}
