package info.joseluismartin.model;

/**
 * Class for Address data
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 *
 */

public class Address {
	/** address */
	private String address;
	/** city */
	private City city;
	/** conuntry */
	private Country country;
	private String postalCode;

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}
