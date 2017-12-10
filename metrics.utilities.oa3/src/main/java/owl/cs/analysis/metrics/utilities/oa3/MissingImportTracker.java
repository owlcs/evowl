package owl.cs.analysis.metrics.utilities.oa3;


import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.MissingImportEvent;
import org.semanticweb.owlapi.model.MissingImportListener;

public class MissingImportTracker implements MissingImportListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8584030601480299429L;
	private final Set<String> missingImports = new HashSet<String>();

    public void importMissing(MissingImportEvent event) {
        missingImports.add(event.getImportedOntologyURI().toString());

    }
    public Set<String> getMissingImports() {
        return missingImports;
    }
}

