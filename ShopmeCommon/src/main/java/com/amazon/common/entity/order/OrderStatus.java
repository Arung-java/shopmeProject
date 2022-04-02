package com.amazon.common.entity.order;

public enum OrderStatus {
	NEW {

		@Override
		public String defaultDescription() {
			return "Order was placed by the customer";
		}

	},
	CANCELLED {

		@Override
		public String defaultDescription() {
			return "Order was rejected";
		}

	},
	PROCESSING {

		@Override
		public String defaultDescription() {
			return "Order is being processed";
		}

	},
	PACKAGED {

		@Override
		public String defaultDescription() {
			return "Product were packed";
		}

	},
	PICKED {

		@Override
		public String defaultDescription() {
			return "Shipper picked the package";
		}

	},
	SHIPPING {

		@Override
		public String defaultDescription() {
			return "Shipper is delivering the package";
		}

	},
	DELIVERED {

		@Override
		public String defaultDescription() {
			return "Customer recevied the products";
		}

	},
	RETURNED {

		@Override
		public String defaultDescription() {
			return "Prodcuts were returned";
		}

	},
	PAID {

		@Override
		public String defaultDescription() {
			return "Customer has paid the order";
		}

	},
	REFUNDED {

		@Override
		public String defaultDescription() {
			return "Customer has been refunded";
		}

	}, RETURN_REQUESTED{

		@Override
		public String defaultDescription() {
			return "Customer sent request to return purchase";
		}
		
	};

	public abstract String defaultDescription();
}
