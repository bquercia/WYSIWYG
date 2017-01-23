package styles;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A PropertySet is a HashSet of Property elements.
 * The difference with a classical HashSet is that the unicity only considers the label of the elements.
 * Therefore there cannot be two properties with the same label.
 * @author Bruno Quercia
 *
 */
public class PropertySet extends HashSet<Property> {
	
	@Override
	public boolean add(Property e){
		Iterator<Property> i = this.iterator();
		boolean valueChanged = false;
		while(i.hasNext() && !valueChanged){
			Property p = i.next();
			if(p.getLabel() == e.getLabel()){
				this.remove(p);
			}
		}
		return super.add(e);
	}
	
	@Override
	public boolean addAll(Collection<? extends Property> c){
		Iterator<?> i = c.iterator();
		boolean result = true;
		while(i.hasNext()){
			result = result && this.add((Property)i.next());
		}
		return result;
	}
}
