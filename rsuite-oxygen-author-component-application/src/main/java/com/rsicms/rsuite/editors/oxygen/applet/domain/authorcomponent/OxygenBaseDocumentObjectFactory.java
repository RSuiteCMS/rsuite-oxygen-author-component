package com.rsicms.rsuite.editors.oxygen.applet.domain.authorcomponent;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.component.AuthorComponentException;
import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;
import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.exml.editor.EditorPageConstants;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorComponentEditorPage;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;

class OxygenBaseDocumentObjectFactory {

	private AuthorComponentFactory authorComponentFactory;

	private static ExecutorService executor = Executors.newFixedThreadPool(2);

	private ConcurrentLinkedQueue<Future<OxygenBaseDocumentObjects>> preparedProviders = new ConcurrentLinkedQueue<>();

	public OxygenBaseDocumentObjectFactory(AuthorComponentFactory authorComponentFactory) {
		this.authorComponentFactory = authorComponentFactory;
	}

	public void prepareEditorComponentProvider() {
		Future<OxygenBaseDocumentObjects> futureTask = executor
				.submit(new Callable<OxygenBaseDocumentObjects>() {

					@Override
					public OxygenBaseDocumentObjects call() throws Exception {
						return createEditorComponentProvider();
					}
				});

		preparedProviders.add(futureTask);

	}

	public synchronized OxygenBaseDocumentObjects obtianBaseDocumentObjects()
			throws OxygenIntegrationException {
		try {

			if (preparedProviders.isEmpty()) {
				return createEditorComponentProvider();
			}

			Future<OxygenBaseDocumentObjects> futureProvider = preparedProviders
					.poll();
			
			return futureProvider.get();
		} catch (InterruptedException | ExecutionException
				| AuthorComponentException e) {
			throw new OxygenIntegrationException(e.getLocalizedMessage(), e);
		}
	}

	private OxygenBaseDocumentObjects createEditorComponentProvider()
			throws AuthorComponentException {
		EditorComponentProvider editorComponentProvider = authorComponentFactory.createEditorComponentProvider(
				new String[] {
						// Comment this if you do not want a text page in
						// the component.
						EditorPageConstants.PAGE_TEXT,
						EditorPageConstants.PAGE_AUTHOR },
				// The initial page
				EditorPageConstants.PAGE_AUTHOR);
		
		WSAuthorComponentEditorPage editorPage = ((WSAuthorComponentEditorPage)editorComponentProvider.getWSEditorAccess().getCurrentPage());
		
		AuthorAccess authorAccess = editorPage.getAuthorAccess();
		
		return new OxygenBaseDocumentObjects(editorComponentProvider, authorAccess, editorPage);
	}

}
