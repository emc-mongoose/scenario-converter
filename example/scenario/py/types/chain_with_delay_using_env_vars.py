# note the delay of the items output (1 minute)
# the items processed by the this sub-step operations won't be available
# for the next sub-step (defined by "config2") operations earlier than in 1 minute
config1 = {
    "item": {
        "output": {
            "delay": "1m",
            "path": "/default"
        }
    },
    "storage": {
        "net": {
            "node": {
                "addrs": ZONE1_ADDRS
            }
        }
    }
}

# read the items created previously from the other set of the nodes (after waiting for 1 minute for
# each item)
config2 = {
    "load": {
        "type": "read"
    },
    "storage": {
        "net": {
            "node": {
                "addrs": ZONE2_ADDRS
            }
        }
    }
}

ChainLoad \
    .config(config1) \
    .config(config2) \
    .run()
