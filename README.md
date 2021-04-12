# Simple Spring Cloud Stream Demo

This repo contains a basic demo of [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream) (SCSt).  
SCSt is a Spring Boot-based framework for building message-driven applications.  
SCSt allows the creation of very complex messaging systems with a very small amount of code and little or no code specific to the underlying 
message broker.

The SCSt programming model is very simple: it is based on Java functional interfaces and the [Spring Cloud Function](https://spring.io/projects/spring-cloud-function) 
technology.  In a messaging application the microservice sending a message is known as the `producer` (or `source` in SCSt terminology) and the 
receiving microservice is the `consumer` (or `sink` in SCSt).  Microservices that are both `producer` and `consumer` are known as `processors` 
(same terminology in SCSt).

SCSt also directly supports reactive programming via [Project Reactor](https://projectreactor.io/).  Reactive processing of messages is a very 
powerful tool to create complex even processing pipelines in a very concise manner.

A `producer` is a "supplier" of messages, so the Java functional interface used is the [`Supplier`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html).
However, since the is a "pull" mechanism (the method in the `Supplier` interface is `get()`), which is not very convenient in many cases, the 
reactive techniques are generally used.  There is also a special interface for directly writing a message to a `producer`.

A `consumer` is a "consumer" of messages, so the Java functional interface used is the [`Consumer`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html).
This interface can be used directly in an application, but the reactive style of programming is also available.

A `processor` is the combination of `consumer` and `producer`, where a inbound message directly results in an outbound message.  This is generally 
used for transformers that convert one message type to another.  The Java function interface is [`Function`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html).
This interface can also be used directly, as well as the reactive style of programming.

Note that none of this implies a specific message broker.  Code written against these APIs will work for all message brokers supported by SCSt.  
The magic that allows this is known as the `binder`, which "binds" your code to the underlying message broker.  Effectively this means that there 
is an adapter, under the covers, that attaches the instance of the functional method that you have defined to the client code provided by the 
message broker implementation.  For example, for Kafka, the Kafka Client is attached by SCSt to your code.  This results in messages in a 
`producer` being passed to the Kafka Client, which sends them to the Kafka broker.  Similarly, messages consumed from the Kafka broker are passed 
to you code via the Kafka Client and SCSt.

The following binders are provided:

- [RabbitMQ](https://www.rabbitmq.com/)
- [Kafka](https://kafka.apache.org/)
- [Amazon Kinesis](https://aws.amazon.com/kinesis/)
- [Google PubSub](https://cloud.google.com/pubsub/docs)
- [Azure Event Hubs](https://docs.microsoft.com/en-us/azure/event-hubs/)
- [Apache RocketMQ](https://rocketmq.apache.org/)
- [Solace PubSub](https://solace.com/products/event-broker/software/)

Custom binders are relatively easy to write via the Binder SPI.

## Running the Demo

JDK 11+ must be installed to run the demo.

This demo uses Kafka, so the first step is to get it running locally.  On Mac use Brew: `brew install kafka`.  This installs the Apache version.  
Kafka uses Zookeeper for cluster management, which is included with the Brew install.  Kafka requires JDK 1.8+.

To get the startup information use `brew info kafka`.  Note that the Brew service (`brew services start kafka`) is sometimes flaky, and doesn't 
show what Kafka and Zookeeper are doing, so generally it's best to use the non-background services.  However, the recommended non-background 
method shown in the info isn't always reliable either.  The reliable way to run Kafka is to run Zookeeper in one terminal:

`zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties`

Once that is up and running start the Kafka broker:

`kafka-server-start /usr/local/etc/kafka/server.properties`

Topics are automatically created by the SCSt applications when they are started.

Once Zookeeper and Kafka are running, each of the applications can be started.  Either start them directly from the IDE or use the Gradle 
`bootRun` task:

Terminal 1:
`./gradlew :consumer:bootRun`

Terminal 2:
`./gradlew :producer:bootRun`

Send some data to the producer:

`./post.sh`
