Chord DHT : Using Apache Thrift RPC Framework
- Harshit Vadodaria (harshitv95@gmail.com)

A very generic Chord DHT project structure (interfaces and abstract classes) that supports RPC.

Includes a simple implementation, using Apache Thrift RPC Framework.

This project can be extended to work with any other RPC Framework (gRPC, Avro, Java RMI, REST and SOAP APIs etc.)

Steps to build and run:
The executable 'server' can be used to build and execute a Chord Server.
If you do not have permissions to execute 'server' on your system, simply execute (one-time setup):
chmod +100 server

Once you have the permissions, execute:
./server port

where port=any available port number you want to let this server listen on
example:
./server 9090

This should compile the code, build an executable jar, and execute it with the port parameter