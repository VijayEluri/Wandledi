package org.wandledi;

import org.xml.sax.Attributes;

/**This is how a spell look like.
 *
 * @author Markus Kahl
 */
public interface Spell {

    public void setParent(Spell spell);
    public void startElement(String name, Attributes attributes);
    public void endElement(String name);
    public void writeCharacters(char[] characters, int offset, int length);

    /**This shall only be a convenience method that makes a call to #writeCharacters.
     */
    public void writeString(String string);
    
    public void startTransformedElement(String name, Attributes attributes);
    public void endTransformedElement(String name);

    /**Each Spell must implement this method and above all mind it
     * within its implementation of #startTransformedElement and
     * #endTransformedElement, which mark the bounds of a transformation.
     * 
     * If this method is called with true as an argument,
     * a Spell must not execute its actual implementation of
     * the respective method, but instead the normal #startElement and
     * #endElement respectively.
     *
     * Hence, a typical implementation of #startTransformedElement would like
     * this:
     * <pre>
     * public void startTransformedElement(String name, Attributes attributes) {
     *
     *     if (ignoreBounds()) {
     *         startElement(name, attributes);
     *     } else {
     *         // actual logic ...
     *         super.startTransformedElement(possiblyTransformedName,
     *                 possiblyTransformedAttributes);
     *     }
     * }
     * </pre>
     *
     * This is highly uncomfortable, but I just don't know how to
     * enforce this implicitly at the moment.
     *
     * @param ignoreBounds Flag indicating whether or not the bounds shall be ignored.
     */
    public void ignoreBounds(boolean ignoreBounds);

    /**Indicates whether or not this Spell ignores transformation bounds events.
     *
     * @return True if this Spell ignores transformation bounds.
     */
    public boolean ignoreBounds();

    public Spell clone();

    /**Checks whether the given spell is contained within this spell's hierarchy.
     * For this each respective parent is compared.
     *
     * @param spell
     * @return
     */
    public boolean hierarchyContains(Spell spell);
}