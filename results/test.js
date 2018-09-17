var cmd_1 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "rm $ITEM_INPUT_FILE")
    .inheritIO()
    .start();
cmd_1.waitFor();

var cmd_2 = new java.lang.ProcessBuilder()
    .command("/bin/sh", "-c", "export ITEM_INPUT_FILE=10K_items.csv")
    .inheritIO()
    .start();
cmd_2.waitFor();

PreconditionLoad
    .config({
      "load" : {
        "step" : {
          "limit" : {
            "count" : 10000
          }
        }
      },
      "item" : {
        "output" : {
          "file" : "" + ITEM_INPUT_FILE + ""
        }
      }
    })
    .run();

function func1() {
    ReadLoad
        .config({
          "load" : {
            "op" : {
              "recycle" : true
            }
          },
          "item" : {
            "input" : {
              "file" : "" + ITEM_INPUT_FILE + ""
            }
          }
        })
        .run();

};

function func2() {
    var itemSize_seq = ["10KB", "1MB", "100MB"];
    for each (itemSize in itemSize_seq){
        var cmd_3 = new java.lang.ProcessBuilder()
            .command("/bin/sh", "-c", "echo " + itemSize + "")
            .inheritIO()
            .start();
        cmd_3.waitFor();

        var concurrencyLimit_seq = [1, 10, 100];
        for each (concurrencyLimit in concurrencyLimit_seq){
            var cmd_4 = new java.lang.ProcessBuilder()
                .command("/bin/sh", "-c", "echo " + concurrencyLimit + "")
                .inheritIO()
                .start();
            cmd_4.waitFor();

            Load
                .config({
                  "load" : {
                    "step" : {
                      "limit" : {
                        "time" : "20s",
                        "concurrency" : concurrencyLimit
                      }
                    }
                  },
                  "item" : {
                    "data" : {
                      "size" : itemSize
                    }
                  }
                })
                .run();

        };
    };
};

var Thread = Java.type('java.lang.Thread');
thread1  = new Thread(func1);
thread1.start();
thread2  = new Thread(func2);
thread2.start();
thread1.join();
thread2.join();

