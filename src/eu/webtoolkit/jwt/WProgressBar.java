/*
 * Copyright (C) 2009 Emweb bvba, Leuven, Belgium.
 *
 * See the LICENSE file for terms of use.
 */
package eu.webtoolkit.jwt;

import java.util.EnumSet;

/**
 * A progress bar
 * <p>
 * 
 * The progress bar can be used to indicate the progress of a certain operation.
 * The text displayed in the progress bar can be customized by specializing
 * {@link WProgressBar#getText() getText()}.
 * <p>
 * To use the progress bar, you need to give it a range (minimum and maximum
 * value), and update the progress using
 * {@link WProgressBar#setValue(double value) setValue()}.
 * <p>
 * WProgressBar is an {@link WWidget#setInline(boolean inlined) inline} widget.
 * <p>
 * <h3>CSS</h3>
 * <p>
 * Using HTML4, the widget is implemented using a set of nested DIVs. The
 * element can be styled using the <code>Wt-progressbar</code> style. It may be
 * styled through the current theme, or you can override the style using
 * internal or external CSS as appropriate.
 * <p>
 * <p>
 * <i><b>Note: </b>With the advent of HTML5, this widget will be implemented
 * using the native HTML5 control when available. </i>
 * </p>
 */
public class WProgressBar extends WInteractWidget {
	/**
	 * Creates a progress bar.
	 */
	public WProgressBar(WContainerWidget parent) {
		super(parent);
		this.min_ = 0;
		this.max_ = 100;
		this.value_ = 0;
		this.changed_ = false;
		this.setStyleClass("Wt-progressbar");
		this.setInline(true);
	}

	/**
	 * Creates a progress bar.
	 * <p>
	 * Calls {@link #WProgressBar(WContainerWidget parent)
	 * this((WContainerWidget)null)}
	 */
	public WProgressBar() {
		this((WContainerWidget) null);
	}

	/**
	 * Sets the minimum value.
	 * <p>
	 * The minimum value is the value that corresponds to 0%.
	 * <p>
	 * The default value is 0.
	 */
	public void setMinimum(double minimum) {
		this.min_ = minimum;
		this.changed_ = true;
		this.repaint(EnumSet.of(RepaintFlag.RepaintInnerHtml));
	}

	/**
	 * Returns the minimum value.
	 * <p>
	 * 
	 * @see WProgressBar#setMinimum(double minimum)
	 */
	public double getMinimum() {
		return this.min_;
	}

	/**
	 * Sets the maximum value.
	 * <p>
	 * The maximum value is the value that corresponds to 0%.
	 * <p>
	 * The default value is 100.
	 */
	public void setMaximum(double maximum) {
		this.max_ = maximum;
		this.changed_ = true;
		this.repaint(EnumSet.of(RepaintFlag.RepaintInnerHtml));
	}

	/**
	 * Returns the maximum value.
	 * <p>
	 * 
	 * @see WProgressBar#setMaximum(double maximum)
	 */
	public double getMaximum() {
		return this.max_;
	}

	/**
	 * Sets the range.
	 * <p>
	 * 
	 * @see WProgressBar#setMinimum(double minimum)
	 * @see WProgressBar#setMaximum(double maximum)
	 */
	public void setRange(double minimum, double maximum) {
		this.min_ = minimum;
		this.max_ = maximum;
		this.changed_ = true;
		this.repaint(EnumSet.of(RepaintFlag.RepaintInnerHtml));
	}

	/**
	 * Sets the current progress.
	 * <p>
	 * <code>value</code> must be a value between
	 * {@link WProgressBar#getMinimum() getMinimum()} and
	 * {@link WProgressBar#getMaximum() getMaximum()}.
	 */
	public void setValue(double value) {
		this.value_ = value;
		this.changed_ = true;
		this.repaint(EnumSet.of(RepaintFlag.RepaintInnerHtml));
	}

	/**
	 * Returns the current progress.
	 */
	public double getValue() {
		return this.value_;
	}

	/**
	 * Returns the text displayed inside the progress bar.
	 * <p>
	 * This text must be an XHTML formatted text fragment. The default text
	 * prints the current progress as a percentage. You may want to reimplement
	 * this method to display a different text corresponding to the current
	 * {@link WProgressBar#getValue() getValue()}.
	 */
	public WString getText() {
		return new WString(String.valueOf((int) this.getPercentage()));
	}

	void updateDom(DomElement element, boolean all) {
		DomElement bar = null;
		DomElement label = null;
		if (all) {
			bar = DomElement.createNew(DomElementType.DomElement_DIV);
			bar.setId("bar" + this.getId());
			bar.setProperty(Property.PropertyClass, "Wt-pgb-bar");
			label = DomElement.createNew(DomElementType.DomElement_DIV);
			label.setId("lbl" + this.getId());
			label.setProperty(Property.PropertyClass, "Wt-pgb-label");
		}
		if (this.changed_ || all) {
			if (!(bar != null)) {
				bar = DomElement.getForUpdate("bar" + this.getId(),
						DomElementType.DomElement_DIV);
			}
			if (!(label != null)) {
				label = DomElement.getForUpdate("lbl" + this.getId(),
						DomElementType.DomElement_DIV);
			}
			bar.setProperty(Property.PropertyStyleWidth, String.valueOf(this
					.getPercentage())
					+ "%");
			WString s = this.getText();
			removeScript(s);
			label.setProperty(Property.PropertyInnerHTML, s.toString());
			this.changed_ = false;
		}
		if (bar != null) {
			element.addChild(bar);
		}
		if (label != null) {
			element.addChild(label);
		}
		super.updateDom(element, all);
	}

	DomElementType getDomElementType() {
		return DomElementType.DomElement_DIV;
	}

	void propagateRenderOk(boolean deep) {
		this.changed_ = false;
		super.propagateRenderOk(deep);
	}

	private double min_;
	private double max_;
	private double value_;
	private boolean changed_;

	private double getPercentage() {
		return (this.getValue() - this.getMinimum()) * 100
				/ (this.getMaximum() - this.getMinimum());
	}
}