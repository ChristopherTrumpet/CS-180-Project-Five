# Project Testing

---

## AccountService Test

### Test 1: Create Account

Steps:
1. User creates an account by inputting account type, username, password, and email.
2. Server adds user account to database.
3. True is returned if the operation is successful, false is return if it is unsuccessful.

Expected result: True is returned when the user attempts to create an account.

Test Status: _Passed_.

### Test 2: Validate Login

Steps:
1. User logs in with their username/email and password.
2. Server checks if the username/email and password match the database.
3. If the username/email and password match, their account info is returned. If they do not match, null is returned.

Expected result: The return type is not null when the user tries to log into an existing account.

Test Status: _Passed_.


## SearchService Test

### Test 1: Search Non-Existent Product

Steps:
1. User searches for a product of a name that does not exist in the database.
2. Server returns null if the product does not exist, otherwise it returns an ArrayList of matching products.

Expected result: Null is returned because the searched product does not exist.

Test Status: _Passed_.

### Test 2: Search Existing Product

Steps:
1. User searches for a product of a name that exists in the database.
2. Server returns null if the product does not exist, otherwise it returns an ArrayList of matching products.

Expected result: The return type is not null because the searched product exists and an ArrayList is returned.

Test Status: _Passed_.


## StatService Test

### Test 1: Get Product History Before Purchase

Steps:
1. User creates an account and does not purchase any products.
2. User attempts to get their product history.
3. Server returns null if the user has not purchased any products, otherwise it returns an JSONArray of purchased products.

Expected result: Null is returned because there is no product history.

Test Status: _Passed_.

### Test 2: Get Product History After Purchase

Steps:
1. User creates an account and purchases a product.
2. User attempts to get their product history.
3. Server returns null if the user has not purchased any products, otherwise it returns an JSONArray of purchased products.

Expected result: The return type is not null because the user has purchased a product and an JSONArray is returned.

Test Status: _Passed_.


## StoreService Test

### Test 1: Add/Remove Store From Seller

Steps:
1. User creates an account of type seller.
2. User adds a store to their account.
3. User then removes the store from their account.
4. Server returns true if the store was successfully removed from the seller's account, otherwise it returns false.

Expected result: True is returned because the store was added and then successfully removed.

Test Status: _Passed_.

### Test 2: Create Store

Steps:
1. User creates a new store with a given name.
2. The store is then automatically added to the seller's account.
3. Server returns true if the store was successfully created and added, otherwise it returns false.

Expected result: The return type is true because the store was successfully created and added.

Test Status: _Passed_.

### Test 3: Change Store Name

Steps:
1. User attempts to change the name of their store.
2. Server processes the request and updates the name of the store in the database.

Expected result: The name of the store in the database matches the name that the user attempted to change it to.

Test Status: _Passed_.

### Test 2: Create Store

Steps:
1. User creates a new product with a given name and description.
2. User then attempts to retrieve a JSONObject of the product by inputting its ID.
3. Server returns the JSONObject of the product if it exists, otherwise it returns null.

Expected result: The return type is not null because the product exists and a JSONObject is returned.

Test Status: _Passed_.


## TransactionService Test

### Test 1: Add to Cart

Steps:
1. User creates an account of buyer type.
2. User adds a product to their cart.
3. Server returns true if the product was successfully added to the cart, otherwise it returns false.

Expected result: True is returned because the product was successfully added to the cart.

Test Status: _Passed_.

### Test 2: Clear Cart

Steps:
1. User attempts to clear their cart with a product already inside.
2. Server returns true if the cart was successfully cleared, otherwise it returns false.

Expected result: True is returned because the cart was successfully cleared.

Test Status: _Passed_.

### Test 3: Add Funds

Steps:
1. User attempts to add a given number of funds to their account.
2. Server returns true if the funds were successfully added, otherwise it returns false.

Expected result: True is returned because the funds were successfully added.

Test Status: _Passed_.