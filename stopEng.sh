sudo su <<EOF
        #stopEng
        $WAS_HOME/profiles/MDMAppSrv01/bin/stopServer.sh MDMDevApp01 -username mdmadmin -password changeme
        #stopNode
        $WAS_HOME/profiles/MDMAppSrv01/bin/stopNode.sh
        #startNode
        $WAS_HOME/profiles/MDMAppSrv01/bin/startNode.sh
        #startEng
        $WAS_HOME/profiles/MDMAppSrv01/bin/startServer.sh MDMDevApp01 -username mdmadmin -password changeme   
EOF


# stopEng
# stopNode
# startNode
# startEng