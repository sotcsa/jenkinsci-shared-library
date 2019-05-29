def call(def map) {
    script {
        assert map.url: "map.url must exists"

        if(map.depthOption==null){
            map.depthOption="infinity"
        }
        if(map.local==null){
            map.local="."
        }
        if(map.workspaceUpdater==null){
            map.workspaceUpdater="UpdateWithCleanUpdater"
        }

        if (map.revision != null) {
            map.url = "${map.url}@${map.revision}"
        }

        map.test=true
        map.msg="this is the message"
        echo "map is $map"

        return map
    }
}