package com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup;

import java.util.List;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;

public interface IReferenceTargetLookup {

	List<IReferenceTargetElement> getReferenceTargetElementList(AuthorAccess authorAccess, IReposiotryResource node, InsertReferenceElement element) throws Exception;
}
