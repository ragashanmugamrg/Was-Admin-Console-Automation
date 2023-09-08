sudo su <<EOF
        stopEng -username mdmadmin -password changeme
        stopNode -username mdmadmin -password changeme
        startNode
        startEng
EOF

retrival=$?
echo "Pass:$retrival"
