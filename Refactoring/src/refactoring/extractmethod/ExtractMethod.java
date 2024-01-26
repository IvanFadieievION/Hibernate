package refactoring.extractmethod;

import java.util.List;

public class ExtractMethod {

	private static final long PERCENT_DISCOUNT = 5;
	public String cartUid;

	public long calculateCartTotal(List<Item> items) {
		
		// TODO: Extract Method with name sumCartCosts
		long totalCartAmount = 0;
		for (Item item : items) {
			totalCartAmount += item.getPrice();
		}
		long cartSum = totalCartAmount;
		// end selection for Extract Method
		
		long discount = caclulateDiscount(cartSum);
		return cartSum - discount;
	}

	private long caclulateDiscount(long cartSum) {
		if (cartSum > 1000) {
			return cartSum / 100 * PERCENT_DISCOUNT;
		}
		return 0;
	}

}
