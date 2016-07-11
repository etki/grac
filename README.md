# Generic REST API Client

[![Build Status](https://travis-ci.org/etki/grac.svg?branch=dev)](https://travis-ci.org/etki/grac)

This project aims to provide simple (not fit-all-cases) asynchronous 
REST API client that may help in bootstrapping common situations. 

GRAC is being developed as a response to challenges presented by
microservice architecture:

- REST is a simple concept of request-response interaction, several 
fixed actions (create, read, replace, modify and delete), optional
metadata and optional payload, which can be either object representation
or typeless BLOB byte stream.
- Application API may consist of dynamically changed several servers, 
probably exposed via different transports (e.g. http and https).
- It is hard to think about traditional synchronous architecture in 
high throughput applications, so you have to choose between async 
execution and fiber-based architecture; this project chooses former 
option and is based around promise-like `CompletableFuture`.

It is implied that single GRAC instance represents single application
(that may be deployed as several instances); GRAC is not a traditional 
HTTP client that may query anything, once GRAC is created, it is tied
to pool of servers (that may be dynamic or consist of single instance).
If you need to query several applications, you need to have a copy of
GRAC for every of them (they, however, can share serializers and 
transports).

## Traditional "get started" quickie

```
ServerProvider serverProvider = new ConstantServerProvider(new ServerContainer(new InetSocketAddress("localhost", 80), "http"));
Client client = new ClientBuilder()
        .withServerProvider(serverProvider)
        .withTransport(new AsyncHttpClientTransport())
        .withSerializer(new JacksonSerializer())
        .build();
TypeSpec expectedType = new TypeSpec(MetricReport.class);
CompletableFuture<Response<MetricReport>> future = client.<MetricReport>read("metric", expectedType);
```

## Principles

This client tries to be transport-agnostic (even though i know you'll 
use HTTP/1.1), so all requests are basically reduced to simple structure
of resource path, action, payload (stream, stream length, mime type),
request parameters (that's query string in common case) and 
metadata (headers in HTTP realm). The client itself may be divided in
three main components: transport (responsible for delivering message and
getting response), serialization (responsible for converting streams 
into runtime objects and vice versa) and application layer that doesn't 
do much except for providing clean interface for you.

Instead of usual request-this-url pattern current client separates
url into server object and resource path. Server object is a simple
server description: protocol that may be used to access server, address
and port, while resource path is a simple string describing resource.
This decoupling allows to provide constantly updated list of backend
servers that may serve client's request.

Transport layer consists of `me.etki.grac.transport.Transport` 
implementations. When client makes a request, it fetches server list, 
throws out servers with protocols unsupported by current transport 
implementations, takes next one according to load balancing policy,
then fetches first transport that is capable of handling that server's
protocol. After that it's up to main transport method: it has to perform 
single request and either return byte stream response with overall 
response status (OK / client error / server error) or fail with 
exception (be aware that erroneous response should not be considered as
an excuse for exception).

Serialization layer then tries to deserialize received payload using
similar mechanism: iterates all registered serializers until it finds 
one that supports provided mime type, then it tries to deserialize 
object of target type using that deserializer. If deserialization
fails, client tries to deserialize one of fallback types, and then fails
completely if this is not possible. Serialization manager uses caching
input stream under cover, so while smaller payload may easily fit into
cache and reread using different serializer, bigger ones may be bigger
than cache and result in applying only part of serializaers.

On the last layer response object is assembled and returned to client 
code, or, if client is configured to throw exception on client/server
error responses, returns corresponding exception wrapped in completable 
future.

## Notable facts

- There is one painful place in HTTP protocol: the 404 case, which is 
not necessarily client error. The default behavior is to downgrade 
'client error' response status to 'OK' if REST action was READ or 
DELETE, otherwise it is kept intact and promoted to exception on 
application layer. It is possible to change that behavior using 
other-than-default interceptor.
- The interface is not stable and may change from one minor release to
another. It's almost certain that serialization manager will be
rewritten, as well as return statuses scheme.

## Requirements

- Java 8
- Strong passion for CompletableFuture