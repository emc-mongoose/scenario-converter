PreconditionLoad
    .config({
      "load" : {
        "op" : {
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
      "load" : {
        "step" : {
          "limit" : {
            "time" : 60
          }
        }
      }
    })
    .append({
      "item" : {
        "output" : {
          "path" : "default"
        }
      },
      "storage" : {
        "driver" : {
          "limit" : {
            "concurrency" : 20
          }
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

