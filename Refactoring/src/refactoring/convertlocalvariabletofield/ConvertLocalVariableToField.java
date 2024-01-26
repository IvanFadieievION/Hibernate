package refactoring.convertlocalvariabletofield;

import java.util.List;

public class ConvertLocalVariableToField {

	private static final long PERCENT_DISCOUNT = 5;
	public String cartUid;

	public long calculateCartTotal(List<Item> items) {
		long cartSum = this.sumCartCosts(items); // <-- TODO: Convert Local Variable To Field: cartSum
		long discount = this.caclulateDiscount(cartSum); // <-- TODO: Convert Local Variable To Field: discount
		long cartTotal = cartSum - discount;
		return cartTotal;
	}

	private long sumCartCosts(List<Item> items) {
		long totalCartAmount = 0; // <-- TODO: Convert Local Variable To Field: discount
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
