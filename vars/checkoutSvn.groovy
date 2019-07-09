def call(def map) {
    script {
        assert map.url: "map.url must exists"

        if(map.credentialsId==null){
            map.credentialsId="svn_jenkins_user"
        }
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

        //by default do trigger build
        if(map.triggerBuildOnChange==false){
            map.changelog=false
            map.poll=false
        }else{
            map.changelog=true
            map.poll=true
        }


        echo "map is $map"

        def tmpMap

        retry(5) {
            echo "Retry...."
            try {
                tmpMap = checkout(
                    changelog: map.changelog,
                    poll: map.poll,
                    scm: [
                        $class                : 'SubversionSCM',
                        additionalCredentials : [],
                        excludedCommitMessages: '',
                        excludedRegions       : '',
                        excludedRevprop       : '',
                        excludedUsers         : '',
                        filterChangelog       : false,
                        ignoreDirPropChanges  : false,
                        includedRegions       : '',
                        locations             :
                            [[cancelProcessOnExternalsFail: true,
                              credentialsId               : map.credentialsId,
                              depthOption                 : map.depthOption,
                              ignoreExternalsOption       : true,
                              local                       : map.local,
                              remote                      : map.url]],
                        quietOperation        : true,
                        workspaceUpdater      : [$class: map.workspaceUpdater]
                    ]
                )
            } catch (error) {
                echo "ERROR.... sleep 30s...."
                sleep(time:30,unit:"SECONDS")
                throw error
            }
        }

        //Left for backwards compatiblity
        env.TMP_SVN_REVISION = tmpMap.SVN_REVISION
        echo "returned map is $tmpMap"
        return tmpMap
    }
}

