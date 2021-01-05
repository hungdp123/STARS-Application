Only works in command prompt, due to masking of password
Go to directory: 'PATH'\Assignment\bin

Paste the following for javax.mail:
set Classpath=%Classpath%;'PATH'\javax.mail.jar;
set Classpath=%Classpath%;'PATH'\activation-1.1.1.jar;

Then run java cx2002.MainApplication

Unhashed and unserialized usernames and passwords are stored in 'username.txt'
AccountCreator is not part of the assignment but can be used to create new accounts or update existing accounts into serialized file.

*'PATH' refers to corresponding path of where files are stored.