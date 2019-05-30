
def call() {
    sh "!!!!hostname || true"
    sh "whoami || true"
    sh "id || true"
    sh "pwd || true"
    sh "env || true"
    sh "curl ifconfig.co || true"

   if(isUnix()) {
       sh 'ifconfig || true'
   }else{
       sh "ipconfig || true"
   }
   sh "echo 'THE END'"
}
//
//
