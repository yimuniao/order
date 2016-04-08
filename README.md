# order
order processing system

# There are some modules 
## ordercommon 
"ordercommon" is for common definition.  

## orderweb
"orderweb" is a restful web service, it is based on spring boot. 
             it accept the order information from user summbit, then first insert it into database, 
             database will generate a unique orderid, then return the order newly generated to browser. 
             At the same time, service will put the order a blocking queue, then a kalka producer 
             thread will poll from the blocking, and send the order to kafka server. 
             The webserver provide the query interface.
## orderpipeline             
"orderpipeline" is an order processing server, it is a java application. 
it contains a kafka consumer thread, it will poll the order message from kafka, 
then put it to an ArrayBlockingQueue, then pipeline threads will poll the order message from this blocking queue,               and process the order. there are some steps in the pipeline. 
Monitorservice can collect the status and statistic periodically, then send it to control center. And it can poll
the configuration from control center (maybe zookeeper, we can use curator framework, it is opensource licnese),
then according to the running mode and debug mode, we can make the app stop to process the order and switch the debug
level dynamically.
                
### Two ways pipeline works.
             
1, one pipeline have some processors, one order will go through the processors in sequence. 
it means all of the processors work in one thread. Maybe you will say, 
it has a performance issue, but actually, you can start many threads in the same time to go the pipeline.
                
2, pipeline has some runner services. Each service contains multi threads, Each service has a blocking queue. 
One step has a runner service. all of the threads in one service will poll the order context from queue, 
and handle them in the same time. Then processed order context will be delivered to the queue of the next
runner service. it is really  muti threads pipeline. The order entity will be commit to database in each step.
Then user can query the status of each step. 
          
There are two unit tests to test the two pipeline.  1，PipelineSingleThreadExecutorTest  2，PipelineMultiThreadsExecutorTest.

## orderscanner
"orderscanner" is an application, which will scan the database to find the timeout order, then send it to kafka again, 

## About the unique order id issue.
  currently, it depends the database function, the value of id column is auto increased.
  [@GeneratedValue(strategy = GenerationType.AUTO)]
    
  if this method is not ok, it is easy to generate an unique id by creating a new service in orderweb module. For example:
  Assign one webservice a unique id, then appending a incremental value to the unique id. Then webservice unique id plus incremental value is a new global unique id.

## How to guarantee the safty of the order
  There will be many scanner processes that register to Zookeeper, one of them is master, the others are slaves. when the master is down, one of the slaves will become master. it will garrantee the scanner's safty.
  The main responsibility of the scanner is to scan database to get the orders which are timeout. After got the timeout orders, then send them to kafka, then pipeline node will cosume these orders again.
 
## Some other issues 
If the database is the bottlenetck, we can partition the table or use submeter. 
If you worry about the kafka server crash, we can do write-ahead log before sending to kafka server,  after kafka server  recovery, replay the log.

If you do not worry about the little delay of order processing when enlarging the capacity, we can use kafka + storm to processing the order, this solution has  a disadvantage, when we join a new node we should enlarge the number of worker, executor and task. Looks like during the rebalance, there are will be about ten seconds dealy.


  