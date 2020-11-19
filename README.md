Real-time Transaction Ledger API Service
========================================
## Overview
Welcome to an OpenAPI rest service built on top of the Spring framework.

This project attempts to recreate some of the concepts in [event-sourcing](https://martinfowler.com/eaaDev/EventSourcing.html).

## Schema
The [included service.yml](service.yml) is the OpenAPI 3.0 schema to a service that is hosted.

## Details
The service accepts two types of transactions:
1) Loads: Add money to a user (credit)
2) Authorizations: Conditionally remove money from a user (debit)

Every load or authorization PUT should return the updated balance following the transaction. Authorization declines should be saved, even if they do not impact balance calculation.

You may use any technologies to support the service. We do not expect you to use a persistent store (you can you in-memory object), but you can if you want. We should be able to bootstrap your project locally to test.

## Goals 
We are looking for attention in the following areas:
1) Open API schema-defined requests and responses are implemented by this application.
2) Use JUnit to test the service endpoints.

# Bootstrap instructions
* If needed, the IntelliJ project can be imported directly by right-clicking the `.idea` directory and importing the project with IntelliJ.

Two ways to test the service: 
1) With unit tests: Run `ApplicationTest` as a JUnit test.
2) Launch the application server. Run the `Application` main application. No extra program arguments or environment variables needed.

## Design considerations
* Event-driven system architecture: I built the 'skeleton' of what a full-fledged event-driven might look like. Most of the files related to this are in the `EventDriven` package. This was by far one of the more fun aspects of the project.
I envisioned a production-level event-driven system (like AmazonSQS or MicrosoftMQ) to "hand off" events to be processed by computing nodes. This part of the project made me question my own understanding of concepts like event re-queuing, event sourcing (creating events from different sources) and how such an system might work with a microservices architecture.
* Distributed locking: this is an important and critical aspect of any distributed, transactional service. While I did not add support for ReadWriteLocks yet, I think adding a distributed locking service such as Zookeeper would be a good start.
Another ideal locking mechanism for this type of system would be the optimistic-locking architecture.
* Rule Engine: This part of the project was where I was able to use a powerful way of organizing business logic -- rule engines. The files that hold this logic are `RuleEngineService`, `RuleEngine` and `Rule`. Here is where
I think much of the "business logic" meat can live. It provides an easy way to customize, organize and re-use business logic for various business models.
* Internal and external schemas: Given more time, I would like to expand on the external model representations that the `TransactionProcessor` can return as a `RequestResult`. For example, if there is ever to be a web-solution for Current, then
there will likely be different response types. Another example might be implementing an endpoint for non-Open API protocols, such as gRPC. 
* Unique ID pools: One aspect of the project I wish I could have worked on more was the implementation of UUID pools. The reason I wanted this service was to differentiate between
the external messageId and the internal event's UID. Currently, our schema conflates the messageId and request's sourceId. This could be a dangerous and limiting design choice, as it decrease the 
"distance" between user-created requests and internal events responsible for mutating data. Ideally, there is a message ID pool and an event UID pool.

## Bonus: Deployment considerations in a Production environment
* Transactional Data Persistence: ideally, this would live on a SQL database -- perhaps Db2 SQL.
* Online Transactional Processing data: Implement an offline service responsible for offloading copies of the transactional data to 
tables that can be used for general reporting and analytics purposes. An Extract-Transform-Load (ETL) service that adds events to a low-priority event-processing queue would work well for this.
* Distributed locking: Zookeeper works well for this purpose and is very easy to use.
* Spring: this is my first time using the Spring framework -- I'm curious as to the Production-level capabilities it offers. 