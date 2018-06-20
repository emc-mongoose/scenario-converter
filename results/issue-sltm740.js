
        var step_1 CreateLoad();
        step_1.config({
              "item" : {
                "output" : {
                  "file" : "MONGOOSE_DIR/log/INIT/items.csv"
                }
              }
            });
        step_1.start();

        var step_2 CreateLoad();
        step_2.config({
              "item" : {
                "output" : {
                  "file" : "MONGOOSE_DIR/log/INIT/items.csv"
                }
              }
            });
        step_2.start();
