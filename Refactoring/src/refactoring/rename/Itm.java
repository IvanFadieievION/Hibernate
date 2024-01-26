package refactoring.rename;

public class Itm { // <-- TODO: Rename to Item
	long p; // <-- TODO: Rename to 'price'

	public long getPrice() {
		return p;
	}

	public void setPrice(long price) {
		this.p = price;
	}
}