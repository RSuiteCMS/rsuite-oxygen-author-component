package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.component.customization;

import java.util.List;

import javax.swing.Action;

import ro.sync.exml.workspace.api.standalone.InputURLChooser;
import ro.sync.exml.workspace.api.standalone.InputURLChooserCustomizer;

public class RSuiteInputURLChooserCustomizer implements InputURLChooserCustomizer{

	public void customizeBrowseActions(final List<Action> existingBrowseActions, final InputURLChooser chooser) {	          
        existingBrowseActions.clear();	           
      }
}
