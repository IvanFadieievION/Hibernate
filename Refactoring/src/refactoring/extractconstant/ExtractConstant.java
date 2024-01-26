package refactoring.extractconstant;

import java.util.List;

public class ExtractConstant {

	public String cartUid;

	public long calculateCartTotal(List<Item> items) {
		long cartSum = sumCartCosts(items);
		long discount = caclulateDiscount(cartSum);
		return cartSum - discount;
	}

	private long sumCartCosts(List<Item> items) {
		long totalCartAmount = 0;
		for (Item item : items) {
			totalCartAmount += item.getPrice();
		}
		return totalCartAmount;
	}

	private long caclulateDiscount(long cartSum) {
		if (cartSum > 1000) { // TODO: Extract Constant: DISCOUNT_THRESHOLD
			return cartSum / 100 * 5; // TODO: Extract Constant: PERCENT_DISCOUNT
		}
		return 0;
	}

}
