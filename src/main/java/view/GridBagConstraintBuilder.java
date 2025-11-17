package view;

import java.awt.*;

public class GridBagConstraintBuilder extends GridBagConstraints {

    public GridBagConstraintBuilder setGridX(int gridx) {
        this.gridx = gridx;
        return this;
    }

    public GridBagConstraintBuilder setGridY(int gridy) {
        this.gridy = gridy;
        return this;
    }

    public GridBagConstraintBuilder setAnchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public GridBagConstraints build() {
        return this;
    }
}
