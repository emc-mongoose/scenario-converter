PipelineLoad
    .append({
      "item" : {
        "output" : {
          "path" : "/default"
        }
      }
    })
    .append({
      "load" : {
        "op" : {
          "type" : "read"
        }
      }
    })
    .append({
      "load" : {
        "op" : {
          "type" : "update"
        }
      },
      "item" : {
        "data" : {
          "ranges" : {
            "random" : 1
          }
        }
      }
    })
    .append({
      "load" : {
        "op" : {
          "type" : "read"
        }
      },
      "item" : {
        "data" : {
          "verify" : true
        }
      }
    })
    .append({
      "load" : {
        "op" : {
          "type" : "delete"
        }
      }
    })
    .append({
      "load" : {
        "op" : {
          "type" : "noop"
        }
      }
    })
    .run();

