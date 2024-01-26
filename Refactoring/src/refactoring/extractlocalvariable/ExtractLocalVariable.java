package refactoring.extractlocalvariable;

import java.util.List;

public class ExtractLocalVariable {

	private static final long PERCENT_DISCOUNT = 5;
	public String cartUid;

	public long calculateCartTotal(List<Item> items) {
		long cartSum = sumCartCosts(items);
		long discount = caclulateDiscount(cartSum);
		return cartSum - discount; // TODO: Extract Local Variable: cartTotal
	}

	private long sumCartCosts(List<Item> items) {
		long totalCartAmount = 0;
		for (Item item : items) {
			long price = item.getPrice();
			totalCartAmount += price; // TODO: Extract Local Variable: item.getPrice() as itemPrice
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
