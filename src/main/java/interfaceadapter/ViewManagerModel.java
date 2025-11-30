package interfaceadapter;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 */
public class ViewManagerModel extends ViewModel<String> {
    public static final String VIEW_NAME = "view manager";

    public ViewManagerModel() {
        super(VIEW_NAME);
        this.setState("");
    }

}