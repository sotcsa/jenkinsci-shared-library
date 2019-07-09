def call(def map) {

    if(map.branchName==null){
        map.branchName="*/master"
    }

    if(map.credentialsId==null){
        map.credentialsId=env.GIT_CREDS
    }

    map.extensions=[]
    //by default on
    if(map.cleanBeforeCheckout==null){
        map.extensions<<[$class: 'CleanBeforeCheckout']
        map.extensions<<[$class: 'CloneOption', timeout: 60] // default: 1h timeout
    }

    //by default do trigger build
    if(map.triggerBuildOnChange==false){
        map.changelog=false
        map.poll=false
    }else{
        map.changelog=true
        map.poll=true
    }

    echo "checking out url $map ..."

    script {
        retry(5) {
            try {
                checkout(
                    changelog: map.changelog,
                    poll: map.poll,
                    scm: [
                        $class                           : 'GitSCM',
                        branches                         : [[name: map.branchName]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : map.extensions,
                        submoduleCfg                     : [],
                        userRemoteConfigs                : [[credentialsId: map.credentialsId, url: map.url]]
                    ]
                )
            } catch (error) {
                sleep(30*1000)
                throw error
            }
        }
    }
}

