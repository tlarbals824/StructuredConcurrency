package com.sim.structuredconcurrency;

import org.springframework.stereotype.Service;

import java.util.OptionalDouble;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.IntStream;

@Service
public class TestService {


    public Long calculateSync(){
        long startTime = System.currentTimeMillis();
        cal1();
        cal1();
        long endTime = System.currentTimeMillis();
        System.out.println("sync: " + (endTime - startTime));
        return endTime - startTime;
    }

    public Long calculateAsync(){
        long startTime = System.currentTimeMillis();
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(this::cal1);
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(this::cal1);
        CompletableFuture.allOf(task1, task2).join();
        long endTime = System.currentTimeMillis();
        System.out.println("sync: " + (endTime - startTime));
        return endTime - startTime;
    }

    public Long calculateAsyncWithStructuredConcurrency(){
        try(var scope = new StructuredTaskScope.ShutdownOnFailure()){
            long startTime = System.currentTimeMillis();
            StructuredTaskScope.Subtask<Long> fork1 = scope.fork(this::cal1);
            StructuredTaskScope.Subtask<Long> fork2 = scope.fork(this::cal1);

            scope.join();

            StructuredTaskScope.Subtask<Long> fork3 = scope.fork(this::cal1);

            System.out.println("fork: " + fork1.get()+fork2.get());

            scope.join();

            System.out.println("fork: " + fork3.get());
            long endTime = System.currentTimeMillis();
            System.out.println("sync: " + (endTime - startTime));
            return endTime - startTime;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Long cal1()  {
        System.out.println("cal1");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long name = Thread.currentThread().threadId();
        System.out.println("thread id: " + name);
        return 1L;
    }
}
