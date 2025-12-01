package view;

import java.awt.GridBagConstraints;

public class GridBagConstraintBuilder extends GridBagConstraints {

    /**
     * Set gridx value of this GridBagConstraintBuilder.
     * @param gridx The value of gridx to set
     * @return GridBagConstraintBuilder
     */
    public GridBagConstraintBuilder setGridX(int gridx) {
        this.gridx = gridx;
        return this;
    }

    /**
     * Set gridy value of this GridBagConstraintBuilder.
     * @param gridy The value of gridy to set
     * @return GridBagConstraintBuilder
     */
    public GridBagConstraintBuilder setGridY(int gridy) {
        this.gridy = gridy;
        return this;
    }

    /**
     * Set anchor value of this GridBagConstraintBuilder.
     * @param anchor The value of anchor to set
     * @return GridBagConstraintBuilder
     */
    public GridBagConstraintBuilder setAnchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    /**
     * Builder for making an instance of GridBagConstraintBuilder.
     * @return GridBagConstraintsBuilder
     */
    public GridBagConstraintBuilder build() {
        return this;
    }
}


