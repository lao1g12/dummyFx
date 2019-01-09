Overview:
This basic application will store any bid or ask requests in the local memory, and will attempt to match bid and ask requests
to "complete" an order. This application can show the current orders using the requests below, and also if the order is not matched
then it can be deleted. In the case of a partially matched order, the part that is unmatched cannot be removed.

Assumptions
-There is no defined initial currency so assumed all transactions are FROM GBP to USD.
-Orders can only be canceled if stock has not been matched.
-Orders can be partially matched.
-Profit margin is not defined therefore not included so buy and ask prices are all static.
-As the price is static the price is not considered when matching orders.
-Orders are matched on FIFO (First in first out, price is irrelevant as price is static).
-A new order is a new user (As there is no concept of users elsewhere in the application).

Design
-The UserId is essentially the unique order ID, I decided to not let this be defined in the post request so there is control
over the uniqueness of the ID.
-I used a treemap to maintain the order of the orders recieved. This also allows me to use a FIFO. I decided to use
FIFO as a design pattern such that orders don't get "stale". If implemented with a changing GBP/USD rate then you would implement
a mix of FIFO and also highest rate.
-I used two different enitities in this application, one is the Order which is what is stored in memory, and then also
a simplified version called RequestOrder. I chose to use two seperate objects as it is then clearer to the user using the
API as to what information is needed (Particularly relevant if using swagger for documentation).

How to use:
Place Order:
To place an order POST a json of format below to http://localhost:8080/order The returned value is the full order.
{
	"orderType": "ASK",
	"currency": "USD",
	"amount": 20
}

Order type is either ASK or BID. The rate of the transfer is set in the application.yaml.

Delete Order:

To delete an order send a DELETE request to /order/{userId} where the userId is found in the order. This will then reply
with a response string.

Show completed Orders:
Send a GET request to /order/complete

Show unmatched Ask Orders:
Send a GET request to /order/ask

Show unmatched Bid Orders:
Send a GET request to /order/bid