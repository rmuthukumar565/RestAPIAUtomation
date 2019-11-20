Verification of add,get,update and delete functionality

@Author - Muthu
Narrative: As a QA I want to verification of add items to cart functionality

Meta:
@categories aeoservices

Scenario: (1-POSITIVE) Add Items and verify items in get cart
Meta:
@features reg
Given I am connected with add cart service at:/v1/cart/item
When I have skuId as <SKUID>, productId as <PRODID>, quantity as <QTY>
Then I send POST request to service with <FORMAT> format
Then I expect the service to return <RESP> status code
Then I store cartid
Then I connect to get cart service at:/v1/carts/
Then I send GET request to service with <FORMAT> format
Then I verify skuId as <SKUID>, productId as <PRODID>, quantity as <QTY> in the response

Examples:
|FORMAT	|RESP|SKUID	  |PRODID|QTY|
|JSON	|200 |93183128|458933|2	 |
|JSON	|200 |9331234 |458937|4	 |
