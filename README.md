# order
order processing system

There are three directories 

        "ordercommon" is for common definition.  
        
        "orderweb" is a restful web service, it is based on spring boot. it accept the order information from user summbit, then first insert it into database, database will generate a unique orderid, then return the order newly generated to browser. At the same time, service will put the order a blocking queue, then a kalka producer thread will poll from the blocking, and send the order to kafka server. The webserver provide the query interface.
        "orderpipeline" is a order processing server, it is a java application. it contains a kafka consumer thread, it will poll the order message from kafka, then put it to a ArrayBlockingQueue, then pipeline threads will poll the order message from this blocking queue, and process the order. there are some steps in the pipeline. there are two way which pipeline work.
            one pipeline have some processors, one order will go through the processors in sequence. it means all of the processors work in one thread. Maybe you will say, it has a performance issue, but actually, you can start many threads in the same time to go the pipeline.
            pipeline has some runner services. Each service contains multi threads, Each service has a blocking queue. One step has a runner service. all of the threads in one service will poll the order context from 	queue, and handle them in the same time. Then processed order context will be delivered to the queue of the next runner service. it is really  muti threads pipeline. The order entity will be commit to database in each step. then user can query the status of each step. 	
