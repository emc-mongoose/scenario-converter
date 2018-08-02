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

PreconditionLoad
    .config(parentConfig_1)
    .config({
      "item" : {
        "output" : {
          "path" : "/var/tmp/read-files-from-variable-path/%p{16;2}",
          "file" : "read-files-from-variable-path.csv"
        }
      }
    })
    .run();

ReadLoad
    .config(parentConfig_1)
    .config({
      "item" : {
        "input" : {
          "file" : "read-files-from-variable-path.csv"
        }
      }
    })
    .run();

