Restmode Database Schema
========================


.. image:: src/images/restmode.png

The NIAM diagram of restmode database.


table : customer
----------------

customer contains information of customer..

=================  =================  ======  ===========================================================
customer           type               null    explanation
=================  =================  ======  ===========================================================
id                 bigint             no      Primary key.
customeremail      varchar(50)        no      customer email (i.e. user@domain.com). It's **UNIQUE**.
customername       varchar(50)        no      customer name. Used for internal display of data.
=================  =================  ======  ===========================================================


table : agreement
-----------------

agreement contains information of list of agreement..

=================  =================  ======  ===========================================================
agreement          type               null    explanation
=================  =================  ======  ===========================================================
id                 bigint             no      Primary key.
agreementdetail    varchar(100)       no      agreement detail. (i.e. property loan).
=================  =================  ======  ===========================================================


table : agreementdetail
-----------------------

agreementdetail contains information between customer and agreement..

=================  =================  ======  ===========================================================
agreementdetail    type               null    explanation
=================  =================  ======  ===========================================================
id                 bigint             no      Primary key.
agreementstatus    varchar(100)       no      agreement status. (i.e. processing).
customer_id        bigint             no      foreign key to customer.
agreement_id       bigint             no      foreign key to agreement.
=================  =================  ======  ===========================================================

