package com.rsicms.rsuite.editors.oxygen.applet.components.symbols;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.ClassPathResourcesAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.symbols.jaxb.Symbol;
import com.rsicms.rsuite.editors.oxygen.applet.components.symbols.jaxb.SymbolList;

public class SymbolPicker extends JPanel {

	/** UID **/
	private static final long serialVersionUID = -7607865577393532346L;

	private Logger logger = Logger.getLogger(this.getClass());
	
	private JLabel theLabel;

	public SymbolPicker(final JDialog parentDialog,
			final AuthorAccess authorAccess) throws Exception {

		setLayout(new BorderLayout());

		String[] columnNames = {};

		Object[][] data = getTableData(authorAccess);
		if (data != null && data[0] != null) {
			columnNames = new String[data[0].length];
			for (int i = 0; i < columnNames.length; i++) {
				columnNames[i] = "";
			}
		}

		final JTable table = new SymbolTable(data, columnNames);

		table.getTableHeader().setVisible(false);
		table.getTableHeader().setPreferredSize(new Dimension(-1, 0));

		table.setPreferredScrollableViewportSize(new Dimension(300, 100));
		table.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(table);

		theLabel = new JLabel("") {
			
			private static final long serialVersionUID = 4150501805880350561L;

			public Dimension getPreferredSize() {
				return new Dimension(150, 100);
			}

			public Dimension getMinimumSize() {
				return new Dimension(150, 100);
			}

			public Dimension getMaximumSize() {
				return new Dimension(150, 100);
			}
		};
		theLabel.setVerticalAlignment(SwingConstants.CENTER);
		theLabel.setHorizontalAlignment(SwingConstants.CENTER);
		Font currentFont = theLabel.getFont();

		Font newFont = new Font(currentFont.getFontName(),
				currentFont.getStyle(), 40);
		theLabel.setFont(newFont);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel
				.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory
								.createTitledBorder("Select a symbol, then click the mouse button"),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		leftPanel.add(scrollPane);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parentDialog.dispose();
			}
		});

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		rightPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Symbol preview"),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		rightPanel.add(theLabel);

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(leftPanel, BorderLayout.CENTER);
		add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.LINE_END);
		add(rightPanel, BorderLayout.LINE_END);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(button);
		add(bottomPanel, BorderLayout.PAGE_END);
		
		HoverSymbolAdapter aMouseAda = new HoverSymbolAdapter(theLabel);
		table.addMouseMotionListener(aMouseAda);
		table.addMouseListener(new SymbolClickAdtapter(parentDialog, authorAccess));
	}

	public class HoverSymbolAdapter extends MouseMotionAdapter

	{
		private JLabel jlabel;

		public HoverSymbolAdapter(JLabel jlabel) {
			super();
			this.jlabel = jlabel;
		}

		public void mouseMoved(MouseEvent e) {
			JTable aTable = (JTable) e.getSource();

			int itsRow = aTable.rowAtPoint(e.getPoint());
			int itsColumn = aTable.columnAtPoint(e.getPoint());
			if (itsColumn > -1 && itsRow > -1) {
				if (aTable.getValueAt(itsRow, itsColumn) != null) {
					Symbol symbol = (Symbol) aTable.getValueAt(itsRow,
							itsColumn);
					jlabel.setText(symbol.getDisplayName());
				} else {
					jlabel.setText("");
				}
			} else {
				jlabel.setText("");
			}
		}

	}

	public class SymbolClickAdtapter extends MouseAdapter

	{
		private AuthorAccess authorAccess;

		private JDialog parentDialog;

		public SymbolClickAdtapter(JDialog parentDialog, AuthorAccess authorAccess) {
			this.authorAccess = authorAccess;
			this.parentDialog = parentDialog;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			JTable aTable = (JTable) e.getSource();

			int itsRow = aTable.rowAtPoint(e.getPoint());
			int itsColumn = aTable.columnAtPoint(e.getPoint());

			if (itsColumn > -1 && itsRow > -1) {
				if (aTable.getValueAt(itsRow, itsColumn) != null) {
					Symbol symbol = (Symbol) aTable.getValueAt(itsRow,
							itsColumn);

					AuthorDocumentController documentController = authorAccess
							.getDocumentController();
					if (symbol.isXML()) {
						try {
							documentController.insertXMLFragment(symbol
									.getValue(), authorAccess.getEditorAccess()
									.getCaretOffset());
						} catch (AuthorOperationException e1) {							
							OxygenUtils.handleException(logger, e1);
						}
					} else {
						documentController.insertText(authorAccess
								.getEditorAccess().getCaretOffset(), symbol
								.getValue());
					}

					parentDialog.dispose();
				}
			}
		}
	}

	private static Object[][] getTableData(AuthorAccess authorAccess)
			throws Exception {

		InputStream is = null;

		int columnNumber = 6;

		ClassPathResourcesAccess classPath = authorAccess
				.getClassPathResourcesAccess();
		for (URL url : classPath.getClassPathResources()) {
			if (url.toString().contains("resources")) {
				URL newURL;

				newURL = new URL(url, "symbols.xml");
				is = newURL.openStream();
			}

		}

		if (is == null) {
			return new Object[0][0];
		}

		JAXBContext context = JAXBContext.newInstance(SymbolList.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		SymbolList listElement = (SymbolList) unmarshaller.unmarshal(is);

		int size = listElement.getTargetElementList().size();
		int rows = size / columnNumber;
		int rest = size % columnNumber;

		if (rest > 0) {
			rows++;
		}

		List<Symbol> symbols = listElement.getTargetElementList();
		Object[][] data = new Object[rows][columnNumber];

		Object[] row = null;
		for (int i = 0; i < symbols.size(); i++) {
			int index = i % columnNumber;

			if (index == 0) {
				row = new Object[columnNumber];
				data[i / columnNumber] = row;
			}

			row[index] = symbols.get(i);
		}

		is.close();

		return data;
	}
}
