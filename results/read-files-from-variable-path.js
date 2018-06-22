
var parentConfig_1 = {
  "item" : {
    "data" : {
      "size" : "10KB"
    }
  },
  "storage" : {
    "driver" : {
      "type" : "fs"
    }
  },
  "load" : {
    "step" : {
      "limit" : {
        "count" : 10000,
        "concurrency" : 10
      }
    }
  }
};

        var step_1 = PreconditionLoad();
        step_1.config(parentConfig_1);
        step_1.config({
              "item" : {
                "output" : {
                  "path" : "/var/tmp/read-files-from-variable-path/%p{16;2}",
                  "file" : "read-files-from-variable-path.csv"
                }
              }
            });
        step_1.run();

        var step_2 = ReadLoad();
        step_2.config(parentConfig_1);
        step_2.config({
              "load" : {
                "type" : "read"
              },
              "item" : {
                "input" : {
                  "file" : "read-files-from-variable-path.csv"
                }
              }
            });
        step_2.run();
