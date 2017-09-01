package com.rsicms.rsuite.editors.oxygen.applet.components.symbols;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class SymbolTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4576423069605170908L;

	public SymbolTable(Object[][] data, Object[] columns) {
		super(data, columns);
	}
	
	@Override
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
	
		return new CentralizedTableCellRenderer();
	}
}
