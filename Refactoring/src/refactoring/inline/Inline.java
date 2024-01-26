package refactoring.inline;

import java.util.List;

public class Inline {

	private static final long PERCENT_DISCOUNT = 5;
	public String cartUid;

	public long calculateCartTotal(List<Item> items) {
		long cartSum = sumCartCosts(items); // <-- TODO: Inline cartSum
		long discount = caclulateDiscount(cartSum); // <-- TODO: Inline discount
		long cartTotal = cartSum - discount; // <-- TODO: Inline cartTotal
		return cartTotal; // <-- TODO: Inline cartTotal
	}

	private long sumCartCosts(List<Item> items) { 
		long totalCartAmount = 0;
		for (Item item : items) {
			totalCartAmount += item.getPrice();
		}
		return totalCartAmount;
	}

	private long caclulateDiscount(long cartSum) {
		if (cartSum > 1000) {
			return cartSum / 100 * PERCENT_DISCOUNT;
		}
		return 0;
	}

}
