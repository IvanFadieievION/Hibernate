package refactoring.rename;

import java.util.List;

public class Rename {

	private static final long PERCENT_REDUCERE = 5; // <-- TODO: Rename to PERCENT_DISCOUNT

	public long calculateCartTotal(List<Itm> items) {
		long cartSum = sumCartCosts(items);
		long discount = caclulateReducere(cartSum);
		return cartSum - discount;
	}

	private long sumCartCosts(List<Itm> items) {
		long sum = 0; // <-- TODO: Rename to totalCartAmount
		for (Itm item : items) {
			sum += item.getPrice();
		}
		return sum;
	}

	private long caclulateReducere(long cartSum) { // <-- TODO: Rename to caclulateDiscount
		if (cartSum > 1000) {
			return cartSum / 100 * PERCENT_REDUCERE;
		}
		return 0;
	}

}
