# RewardPointCalc
Transaction data is initialized dynamically. I also have included the case from example in the test case.

A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction
(e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points). This example has been used in the test case in Transaction service test. 



To view the points calculated for each customer by month and total please make a GET request to /api/transactions/report
