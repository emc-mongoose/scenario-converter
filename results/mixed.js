PreconditionLoad
    .config({
      "load" : {
        "step" : {
          "limit" : {
            "count" : 1000
          }
        }
      },
      "item" : {
        "output" : {
          "path" : "items2read",
          "file" : "items2read.csv"
        }
      }
    })
    .run();

WeightedLoad
    .config({
      "limit" : {
        "time" : 60,
        "concurrency" : 20
      }
    })
    .append({
      "item" : {
        "output" : {
          "path" : "default"
        }
      }
    })
    .append({
      "load" : {
        "op" : {
          "recycle" : true,
          "type" : "read"
        }
      },
      "item" : {
        "output" : {
          "path" : "items2read"
        },
        "input" : {
          "file" : "items2read.csv"
        }
      },
      "storage" : {
        "driver" : {
          "limit" : {
            "concurrency" : 50
          }
        }
      }
    })
    .run();

