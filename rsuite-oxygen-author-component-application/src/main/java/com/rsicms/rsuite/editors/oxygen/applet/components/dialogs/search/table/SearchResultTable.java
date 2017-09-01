package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class SearchResultTable extends JTable {

	/** UUID **/
	private static final long serialVersionUID = 6543620014337193862L;

	public SearchResultTable(TableModel model) {
		super(model);
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int column) {
		Component c = super.prepareRenderer(renderer, row, column);

		if (!isRowSelected(row)) {

			c.setBackground(row % 2 == 0 ? getBackground() : new Color(230,
					230, 250));
		}

		return c;
	}

}
