package owl.cs.analysis.metrics.utilities.oa5;


import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.owlapi.modularity.ModuleType;

public class ReasonerUtilities {

	public static String getReasonerFullname(OWLReasoner reasoner) {
		return reasoner.getReasonerName() + " " + getReasonerVersion(reasoner);
	}

	public static String getReasonerVersion(OWLReasoner reasoner) {
		return reasoner.getReasonerVersion().getMajor() + "."
				+ reasoner.getReasonerVersion().getMinor() + "."
				+ reasoner.getReasonerVersion().getPatch() + "."
				+ reasoner.getReasonerVersion().getBuild();
	}

	

	public static String getReasonerFullname(OWLReasoner r, String alt) {
		if(r == null) {
			return alt;
		}
		if (r.getReasonerName() == null) {
			return alt;
		}
		return getReasonerFullname(r);
	}

	public static OWLReasonerConfiguration getReasonerConfig(String param,
			long reasoner_timeout) {
		return new SimpleConfiguration(reasoner_timeout);
	}

	public static OWLReasonerConfiguration getReasonerConfig(String param) {
		return new SimpleConfiguration();
	}

	public static ModuleType getModuleType(String strategy) {
		if (strategy.contains("-bot-")) {
			return ModuleType.BOT;
		} else if (strategy.contains("-top-")) {
			return ModuleType.TOP;
		} else if (strategy.contains("-star-")) {
			return ModuleType.STAR;
		} else {
			return ModuleType.BOT;
		}
	}
}
