### Benchmark

#### Overview

The project has two load tests (`ParallelLoadTest`, `SingleThreadLoadTest`) used to generate and process files with a
lot of events and show the elapsed time.

To choose between using a sequential stream or a parallel stream, we can use the property `enable-parallel-stream`. When
using the parallel mode, there's one more difference: we start using a `ConcurrentHashMap`, instead of `HashMap` to
accumulate events.

#### Used hardware

* Dell G3 Notebook;
* Intel(R) Core(TM) i7-10750H CPU @ 2.60GHz;
* RAM 16GB;

#### Running the tests

```sh
    load_test=true ./mvnw test -Dtest="ParallelLoadTest"
    load_test=true ./mvnw test -Dtest="SingleThreadLoadTest"
```

#### Results

##### Total time (generating test files + processing the files)

| Parallel=false                                                             | Parallel=true                                               |
|----------------------------------------------------------------------------|-------------------------------------------------------------|
| ![test-results-single-thread](./doc/assets/test-results-single-thread.png) | [test-results-parallel](./doc/assets/test-results-parallel) |

##### Elapsed time

![elapsed-time](/./doc/assets/elapsed-time.png)

| Number of events | Number of employees | Elapsed Time (parallel=false) | Elapsed Time (parallel=true) |
|------------------|---------------------|-------------------------------|------------------------------|
| 10_000           | 100                 | 168 ms                        | 165 ms                       |                
| 10_000           | 500                 | 64 ms                         | 65 ms                        |                  
| 10_000           | 1_000               | 36 ms                         | 45 ms                        |                  
| 10_000_000       | 100                 | 37066 ms                      | 29642 ms                     |                          
| 10_000_000       | 1_000               | 24013 ms                      | 22853 ms                     |                          
| 10_000_000       | 1_000_000           | 29414 ms                      | 174563 ms                    |

##### JVM Used

| Parallel=false                                             | Parallel=true                                           |
|------------------------------------------------------------|---------------------------------------------------------|
| ![jvm-parallel-false](./doc/assets/jvm-parallel-false.png) | [jvm-parallel-true](./doc/assets/jvm-parallel-true.png) |

##### Conclusion

A few tests were executed. To have a more reliable comparison we ideal would be to execute the tests a lot of times, and
then, extract the metrics with the average and standard deviation, but, with these few tests, we could see that the
applying multi-threading here had equal or even bad results.

Since there's no blocking operation while accumulating the results, the over-head caused by forking threads and
synchronizing methods don't pay off. 
