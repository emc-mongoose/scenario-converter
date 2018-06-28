PipelineLoad
    .append({
      "item" : {
        "output" : {
          "path" : "/default"
        }
      }
    }) //substeps
    .append({
      "load" : {
        "type" : "read"
      }
    }) //substeps
    .append({
      "load" : {
        "type" : "update"
      },
      "item" : {
        "data" : {
          "ranges" : {
            "random" : 1
          }
        }
      }
    }) //substeps
    .append({
      "load" : {
        "type" : "read"
      },
      "item" : {
        "data" : {
          "verify" : true
        }
      }
    }) //substeps
    .append({
      "load" : {
        "type" : "delete"
      }
    }) //substeps
    .append({
      "load" : {
        "type" : "noop"
      }
    }) //substeps
    .run();

