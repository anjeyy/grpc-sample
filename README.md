# Sample project for a gRPC (& ReST) web service

A simple project to showcase a gRPC webservice in combination with Spring Boot. 
For detailed information what gRPC is, see the block quote.

Project contains three submodules, _server_ and the corresponding _client_ 
to consume the webservice. 
Definied `.proto` files have their own module, since either _server_ and _client_
then have the same reference compiling `.proto` files.


> please have a look at [gRPC](https://grpc.io/) for further information

<br>

## Modules

Following listing of submodules are beeing used in this project.
<br>

### Proto

Mainly used to specifiy `.proto` files, which are beeing used by either
servers or clients. Instead of modifying every single `.proto` file
now you can simply modifiy this one in the submodule, and the change
is beeing made in all referenced _maven_ projects.

Generating `.proto` files is os (operating system) independant.
<br>

## Server

Server side implementation was done with [grpc-spring-boot-starter](https://github.com/yidongnan/grpc-spring-boot-starter).
Alternatively there exists another [grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter),
but the first one mentioned has more to offer out of the box.

_Hint:_ There is no official `grpc-spring-boot-starter` supported by the 
[spring-boot](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-starters)
team.
<br>

### Exception Handling
> TODO ...

## Client

As already mentioned in section [server](#server), client side implementation was done
similarily.
<br>

## gRPC misc

- On my way to exploring, understanding and implement a gRPC service 
I encountered a very nice tool, similar to _postman_.
    > [BloomRPC](https://github.com/uw-labs/bloomrpc)
  
- A very nice introduction to gRPC with spring in Java and hands-on tutorial, 
I'll recommend [grpc-java-example](https://codenotfound.com/grpc-java-example.html)
  

- For a first insight how to throw and handle errors with gRPC, 
  this [resource](https://www.vinsguru.com/grpc-error-handling/) is helpful
  
