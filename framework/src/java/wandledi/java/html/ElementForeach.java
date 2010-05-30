package wandledi.java.html;

import java.util.Collection;

/**
 *
 * @author Markus Kahl
 */
public abstract class ElementForeach<T> {

    private Collection<T> items;

    protected void setItems(Collection<T> items) {

        this.items = items;
    }

    /**Returns the index of the current item.
     *
     * @return
     */
    public int index() {

        return -1;
    }

    /**Checks whether the current item is the first one.
     *
     * @return
     */
    public boolean first() {

        return index() == 0;
    }

    /**Checks whether the current item is the last item.
     *
     * @return
     */
    public boolean last() {

        return index() == items.size();
    }

    /**Checks whether the current item's index is odd.
     *
     * @return
     */
    public boolean odd() {

        return index() % 2 == 1;
    }

    /**Checks whether the current item's index is even.
     *
     * @return
     */
    public boolean even() {

        return index() % 2 == 0;
    }

    /**Applies the given plan onto each item within the collection.
     *
     * @param plan
     */
    public abstract void apply(Plan<T> plan);
}
