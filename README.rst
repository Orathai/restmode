restmode
========

The NIAM diagram of restmode database.

.. image:: src/images/restmode.png

You find the database description here: \
https://github.com/Orathai/restmode/wiki/database-schame

References
----------

* `https://spring.io/guides/gs/spring-boot <https://spring.io/guides/gs/spring-boot>`_

Testing application with cURL
-----------------------------
1 : Running the application::

     mvn spring-boot:run
2 : Running test file: AgreementServiceIntegrationTest.java::

     curl -s http://localhost:9000/customers
      {
       "customerEmail": "p@p.com",
       "customerName": "Peter Pan",
       "id": 4
      }
      curl -s http://localhost:9000/agreements
      [{
        "agreementDetail": "Vacation Loan",
        "id": 3
      }]

      curl -s http://localhost:9000/agreementdetails
      [
        ...

        {
          "agreementStatus": "SENT_TO_CUSTOMER",
          "agreementModel": {
            "agreementDetail": "Vacation Loan",
            "id": 3
          },
          "customerModel": {
            "customerEmail": "p@p.com",
            "customerName": "Peter Pan",
            "id": 4
          },
          "id": 10
        }
        ...
      ]

