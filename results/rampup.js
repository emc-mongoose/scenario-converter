
var size_seq = [0, 1KB, 1MB];
for( size in size_seq ){

var superConfig_1 = {
  "output" : {
    "metrics" : {
      "average" : {
        "period" : 0
      }
    }
  },
  "item" : {
    "data" : {
      size : size
    }
  },
  "load" : {
    "step" : {
      "limit" : {
        "count" : 1000000,
        "time" : "10s"
      }
    }
  }
};

        var threads_seq = [1, 10, 100];
        for( threads in threads_seq ){

        var superConfig_2 = {
          "load" : {
            "step" : {
              "limit" : {
                "concurrency" : threads
              }
            }
          },
          "item" : {
            "output" : {
              "path" : "size_threadsthreads"
            }
          }
        };

                var step_1 CreateLoad();
                step_1.config(superConfig_1);
                step_1.config(superConfig_2);
                step_1.config({
                      "load" : {
                        "type" : "create",
                        "step" : {
                          "id" : "Cthreads_size"
                        }
                      },
                      "item" : {
                        "output" : {
                          "file" : "size_threadsthreads.csv"
                        }
                      }
                    });
                step_1.start();

                var step_2 ReadLoad();
                step_2.config(superConfig_1);
                step_2.config(superConfig_2);
                step_2.config({
                      "load" : {
                        "type" : "read",
                        "step" : {
                          "id" : "Rthreads_size"
                        }
                      },
                      "item" : {
                        "input" : {
                          "file" : "size_threadsthreads.csv"
                        }
                      }
                    });
                step_2.start();

                var step_3 DeleteLoad();
                step_3.config(superConfig_1);
                step_3.config(superConfig_2);
                step_3.config({
                      "load" : {
                        "type" : "delete",
                        "step" : {
                          "id" : "Dthreads_size"
                        }
                      },
                      "item" : {
                        "input" : {
                          "file" : "size_threadsthreads.csv"
                        }
                      }
                    });
                step_3.start();
        };
};
