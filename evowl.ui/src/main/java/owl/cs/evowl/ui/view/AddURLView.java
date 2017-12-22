package owl.cs.evowl.ui.view;

import java.net.MalformedURLException;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import owl.cs.evowl.ui.MetricsServer;

public class AddURLView extends ViewCanvas implements View {

	private static final long serialVersionUID = -8224929117517039797L;
	TextField tf = new TextField();
	Label l = new Label("Response");
	Button b = new Button("Add");
	
	public AddURLView(MetricsServer server, Navigator navigator) {
		super(server, navigator, "Add URL");
		b.addClickListener(e->addUrl(e));
		getMain().addComponent(tf);
		getMain().addComponent(b);
		getMain().addComponent(l);
	}

	private void addUrl(ClickEvent e) {
		try {
			String out = getServer().addUrl(tf.getValue().toString());
			l.setValue(out);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			l.setValue(e1.getMessage());
		}
	}
	
}
