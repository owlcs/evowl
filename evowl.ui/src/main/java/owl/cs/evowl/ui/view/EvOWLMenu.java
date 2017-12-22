package owl.cs.evowl.ui.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import owl.cs.evowl.ui.State;

public class EvOWLMenu extends VerticalLayout {

	Navigator navigator;
	private static final String MAINVIEW = State.GROUPS.getName();

	EvOWLMenu(Navigator navigator) {
		this.navigator = navigator;
		Layout l = new GridLayout(4, 1);
		l.setSizeUndefined();
		l.addComponent(new Button("Add URL", new ButtonListener(State.ADDURL)));
		l.addComponent(new Button("Groups", new ButtonListener(State.GROUPS)));
		l.addComponent(new Button("Ontologies", new ButtonListener(State.ALLONTOLOGIES)));
		l.addComponent(new Button("evaluation", new ButtonListener(State.EVOWLUATION)));
		addComponent(l);
		setSizeUndefined();
	}

	class ButtonListener implements Button.ClickListener {
		State menuitem;

		public ButtonListener(State menuitem) {
			this.menuitem = menuitem;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			navigator.navigateTo(menuitem.getName());
		}
	}

}
