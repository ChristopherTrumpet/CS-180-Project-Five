# Project 5: Purdue MarketPlace
*Vraj Patel, Matthew Lee, Shrinand Perumal, Mohit Sachin Ambe, Christopher Trumpet*

---

## How to Compile and Run Program
1. Clone the Repository with `git clone https://github.com/ChristopherTrumpet/CS-180-Project-Five.git`
2. Compile the repository by running `javac *.java`
3. Run the program by typing `java Program` into the terminal
4. Enjoy!

## Submissions by Person
- Report - Vraj Patel
- Vocareum Workplace Submission - Matthew Lee

## Program Classes

### AutoCompletion

### Client
Central class that handles all client interactions with server, interface, and backend

### Page (Classes with page)
Functions as the primary means for program flow. 
The program class loops through page objects for navigation. 
Each page relies on a Menu object to display information to the user.

### ClientThread
Allows multiple clients to interact with the marketplace application simultaneously, controlling the order and flow between server and multiple different client interactions.

### Server
Central class that handles all server interactions that communicates with the client. Works with the  backend to store, retrieve, or perform operations involving data.

### AccountService
Provide functions for creating, deleting, or modifying buyer and seller account info. In addition, the class checks whether an account exists, stores the accounts in a user.json file, and also determines what the account type is.

### CSVtoJSON
For export data for a buyer or seller, this class converts the newly created JSON file object and converts it into a presentable CSV file.

### StatService
Handles all data statistics involving sorting products by popularity, sorting stores by amount of products sold, etc. for both the buyer and sellers.

### StoreService
Handles all functionality for store in relation to sellers. Sellers can hold multipple stores. This class can add, remove, or modify stores in addition to those same functions for products that are added to the stores. Sellers and buyers can retrieve about the products price, quantity, and description.

### TransactionService
Handles all functionality with shopping carts and placing orders. Both shopping cart methods and placing order methods run through a list of checks to determine whether the quantity for each product is valid and whether or not a customer has sufficient funds to purchase all items in the shoppinng cart. 

### SearchService
Handles all logic for for customers to searh for products based on name, description, or store.

### Test Cases (for the main classes)
Each Test class checks that the respective classes' function as intended and per the requirements of the Project 5 Handout.
All cases for their respective classes and methods pass.
