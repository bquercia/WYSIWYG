/**
 * 
 */
package content;

/**
 * This listener is used to detect that the content of the document has changed.
 * It does not fire when text is changed, only when the structure itself is altered.
 * @author Bruno Quercia
 *
 */
public interface DocumentListener {
	void contentUpdate();
}
