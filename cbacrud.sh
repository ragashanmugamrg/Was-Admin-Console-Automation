#!/bin/bash
#file processs of cba

error_code=103
search_dir=$(cat installCba.properties | grep was.profile.dir | awk -F "was.profile.dir=" '{print $2}' | sed 's|\\||g')
wsadmin_dir=$(cat installCba.properties | grep was.repository.dir | awk -F "was.repository.dir=" '{print $2}' | sed 's|\\||g')
wspassword=$(cat installCba.properties | grep mdm.admin.password | awk -F "mdm.admin.password=" '{print $2}' | sed 's|\\||g')
wsuserid=$(cat installCba.properties | grep mdm.admin.userid | awk -F "mdm.admin.userid=" '{print $2}' | sed 's|\\||g')

for entry in "$search_dir"/*
do
  echo "$entry"
done

filename=$(basename "$entry")
echo "FileName: $filename"
echo "CBA Path: "$search_dir""$filename" "

cba_data=$search_dir$filename
echo "$cba_data"

#getting the old cba version id
data_path=`sudo -u "mdmadmin" -i <<EOF
        echo "$mdmadmin"
	      cd $wsadmin_dir
        sudo ./wsadmin.sh -lang jython -username $wsuserid -password $wspassword
	      print(AdminTask.listOSGiExtensions('-cuName com.ibm.mdm.hub.server.app-E001_0001.eba'));
EOF`

retrival=$?
echo "Retrival :$retrival"
echo "Pass:$data_path"
dataq=$(awk -F 'BBMComposites.cba;|P'// '{printf $2}'  <<< "$data_path")
#endof old version

new_cba_file=${filename:18:-4}
echo "NEW_CBA:$new_cba_file"
old_cba_file=$dataq
echo "OLD_CBA:$old_cba_file"
echo "$wsadmin_dir"

sudo su <<EOF
          echo "$mdmadmin"
          cd $wsadmin_dir
          ./wsadmin.sh -lang jython -username $wsuserid -password $wspassword
          print("Adding the CBA in bundle Repository");
          AdminTask.addLocalRepositoryBundle('-file $cba_data');
          AdminConfig.save();
EOF

sudo su <<EOF
          echo "$mdmadmin"
          cd $wsadmin_dir
          ./wsadmin.sh -lang jython -username $wsuserid -password $wspassword
          print("Removing The CBA in EBA");
          AdminTask.removeOSGiExtension('-cuName com.ibm.mdm.hub.server.app-E001_0001.eba -symbolicName BBMComposites.cba -version $old_cba_file');
          AdminConfig.save();
EOF

retrival=$?
echo "Retrival :$retrival"
if [ $retrival == $error_code ] 
then
    echo "Error Code"
    exit
fi

sudo su <<EOF
          echo "$mdmadmin"
          cd $wsadmin_dir
          ./wsadmin.sh -lang jython -username $wsuserid -password $wspassword
          print("Adding the New CBA in the EBA");
          AdminTask.addOSGiExtension('-cuName com.ibm.mdm.hub.server.app-E001_0001.eba -symbolicName BBMComposites.cba -version $new_cba_file');
          AdminConfig.save();
EOF

retrival=$?
echo "Retrival :$retrival"
if [ $retrival == $error_code ]
then
    	echo "Error Code"
	exit
fi

sudo su <<EOF
          echo "$mdmadmin"
          cd $wsadmin_dir
          ./wsadmin.sh -lang jython -username $wsuserid -password $wspassword
          print("Update to the Latest Deployment");
          AdminTask.editCompUnit('[-blaID WebSphere:blaname=MDM-operational-server-EBA-E001 -cuID WebSphere:cuname=com.ibm.mdm.hub.server.app-E001_0001.eba -CompUnitStatusStep [[com.ibm.mdm.hub.server.app-E001.eba true \"New OSGi application deployment available.\"]]]');
          AdminConfig.save();
EOF

retrival=$?
echo "Retrival :$retrival"
if [ $retrival == $error_code ]
then
    	echo "Error Code"
	exit
else
	echo "The Process is Completed"
	sudo rm -rf $cba_data
	echo "CBA WAS REMOVED"
fi

