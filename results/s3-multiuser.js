var step_1 = Load
    .config({
      "item" : {
        "data" : {
          "size" : "10KB"
        },
        "output" : {
          "path" : "bucket-%d(314159265){00}[0-99]",
          "file" : "objects.csv"
        }
      },
      "storage" : {
        "driver" : {
          "type" : "s3"
        },
        "auth" : {
          "uid" : "user-%d(314159265){00}[0-99]",
          "file" : "credentials.csv"
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
    })
    .run();
