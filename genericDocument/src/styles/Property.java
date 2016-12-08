package styles;

import java.util.Set;

/**
 * Base class for styles management.
 * A property is a basically a tuple (label, value). Virtually any property is enabled.
 * @author Bruno Quercia
 * 
 */
public class Property {
	String label, value;
	Set<String> possibleValues;
	/**
	 * @param label
	 * Name of the property (e.g. font-weight)
	 * @param value
	 * Value of the property (e.g. bold)
	 * @param possibleValues
	 * List of the possible values for attribute value (e.g. ['bold', 'normal', 'light']).
	 * value is automatically added to possibleValues.
	 */
	public Property(String label, String value, Set<String> possibleValues) {
		this.label = label;
		this.value = value;
		this.possibleValues = possibleValues;
		this.possibleValues.add(value);
	}
	
	/**
	 * 
	 * @return the current value of the property.
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * Sets the value of the property
	 * @param v the new value of the property
	 * @throws Exception if the value is not in the possible values of the property
	 */
	public void setValue(String v) throws Exception {
		if(possibleValues.contains(v)){
			this.value = v;
		}
		else throw new Exception("Impossible value for this property");
	}
	
	/**
	 * Adds a new value the property can be set to
	 * @param v new possible value
	 */
	public void adPossibleValue(String v){
		this.possibleValues.add(v);
	}
	
	/**
	 * Determines if a value is possible for the property.
	 * @param v the value.
	 * @return if the value is possible.
	 */
	public boolean isPossibleValue(String v){
		return possibleValues.contains(v);
	}
	
	/**
	 * 
	 * @return the set of possible values.
	 */
	public Set<String> getPossibleValues(){
		return this.possibleValues;
	}
	
	/**
	 * 
	 * @return the property label.
	 */
	public String getLabel(){
		return this.label;
	}

}
