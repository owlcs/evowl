package owl.cs.evowl.ui.view;


import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import owl.cs.evowl.ui.MetricsServer;

public class ViewCanvas extends VerticalLayout {
	
	final private Layout vl_main = new VerticalLayout();
	final private MetricsServer server;
	
	public ViewCanvas(MetricsServer server, Navigator navigator, String title) {
		this.server = server;
		addComponent(new EvOWLMenu(navigator));
		Label l =new Label("<h1>"+title+"</h1>", ContentMode.HTML);
		l.setSizeUndefined();
		//setMargin(false);
		addComponent(l);
		addComponent(vl_main);
		vl_main.setSizeFull();
	}
	
	protected Layout getMain() {
		return vl_main;
	}
	
	protected MetricsServer getServer() {
		return server;
	}
}
