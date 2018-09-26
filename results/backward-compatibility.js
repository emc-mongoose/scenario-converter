Load
    .config({
      "load" : {
        "op" : {
          "limit" : {
            "count" : 100
          }
        },
        "step" : {
          "id" : "backward-compatibility-test"
        }
      },
      "storage" : {
        "driver" : {
          "limit" : {
            "concurrency" : 10
          }
        },
        "net" : {
          "node" : {
            "addrs" : new java.util.ArrayList([ "127.0.0.1" ])
          }
        },
        "auth" : {
          "uid" : "wuser1@sanity.local"
        }
      }
    })
    .run();

