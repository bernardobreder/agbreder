package com.agbreder.plugin.swt;

import java.util.List;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.agbreder.plugin.swt.BContainer.BContainerItem;
import com.agbreder.plugin.swt.GBC.Fill;

public class BGridBagLayout extends BLayout {

	@Override
	public void build() {
		int numberOfColumns = this.getNumberOfColumns();
		int numberOfLines = this.getNumberOfLines();
		Composite composite = this.getContainer().getComposite();
		{
			GridLayout layout = new GridLayout(numberOfColumns, false);
			composite.setLayout(layout);
		}
		List<BContainerItem> children = this.getContainer().getChildren();
		for (int lin = 1; lin <= numberOfLines; lin++) {
			for (int col = 1; col <= numberOfColumns; col++) {
				for (BContainerItem child : children) {
					GBC gbc = (GBC) child.getData();
					if (gbc.getLine() == lin && gbc.getColumn() == col) {
						Control control = child.getComponent().build(composite);
						int halign = GridData.BEGINNING, valign = GridData.BEGINNING;
						if (gbc.is(Fill.HORIZONTAL)) {
							halign = GridData.FILL;
						} else if (gbc.is(Fill.VERTICAL)) {
							valign = GridData.FILL;
						} else if (gbc.is(Fill.BOTH)) {
							halign = GridData.FILL;
							valign = GridData.FILL;
						}
						GridData gridData = new GridData(halign, valign, gbc.is(Fill.HORIZONTAL) || gbc.is(Fill.BOTH), gbc.is(Fill.VERTICAL) || gbc.is(Fill.BOTH), gbc.getLineSpan(), gbc.getColumnSpan());
						control.setLayoutData(gridData);
						break;
					}
				}
			}
		}
	}

	public int getNumberOfColumns() {
		List<BContainerItem> children = this.getContainer().getChildren();
		int max = 0;
		for (BContainerItem child : children) {
			GBC gbc = (GBC) child.getData();
			if (gbc.getColumn() > max) {
				max = gbc.getColumn();
			}
		}
		return max;
	}

	public int getNumberOfLines() {
		List<BContainerItem> children = this.getContainer().getChildren();
		int max = 0;
		for (BContainerItem child : children) {
			GBC gbc = (GBC) child.getData();
			if (gbc.getLine() > max) {
				max = gbc.getLine();
			}
		}
		return max;
	}

}
