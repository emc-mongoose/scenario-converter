
        var cmd_1 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "rm $ITEM_INPUT_FILE")
            .run();

        var cmd_2 = new java.lang.ProcessBuilder()
            .command("sh", "-c", "export ITEM_INPUT_FILE=10K_items.csv")
            .run();

        var step_1 = PreconditionLoad();
        step_1.config({
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
            });
        step_1.run();

        function func1() {

            var step_2 = ReadLoad();
            step_2.config({
                  "load" : {
                    "type" : "read",
                    "generator" : {
                      "recycle" : {
                        "enabled" : true
                      }
                    }
                  },
                  "item" : {
                    "input" : {
                      "file" : "" + ITEM_INPUT_FILE + ""
                    }
                  }
                });
            step_2.run();
        };

        function func2() {

            var itemSize_seq = ["10KB", "1MB", "100MB"];
            for( itemSize in itemSize_seq ){

                    var cmd_3 = new java.lang.ProcessBuilder()
                        .command("sh", "-c", "echo " + itemSize + "")
                        .run();

                    var concurrencyLimit_seq = [1, 10, itemSize];
                    for( concurrencyLimit in concurrencyLimit_seq ){

                            var cmd_4 = new java.lang.ProcessBuilder()
                                .command("sh", "-c", "echo " + concurrencyLimit + "; echo test")
                                .run();

                            var step_3 = CreateLoad();
                            step_3.config({
                                  "load" : {
                                    "step" : {
                                      "limit" : {
                                        "time" : "1m"
                                      }
                                    }
                                  },
                                  "item" : {
                                    "data" : {
                                      "size" : itemSize
                                    }
                                  }
                                });
                            step_3.run();
                    };

                    for( var i1 = 0; i1 < 10; i1 += 1 ){
                    };

                    while( true ){
                    };

                    for( var rangeLoopValue = 1.55; rangeLoopValue < 3.48; rangeLoopValue += 1 ){
                    };

                    for( var rangeLoopValue = 1.55; rangeLoopValue < itemSize; rangeLoopValue += 1 ){
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

