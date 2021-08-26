#!/bin/bash
echo "Enter your git access token: "
read token
echo "Enter the full path to your git repository (no trailing slash): "
read repo

tests=("TestUnit" "TestVoxelMap" "TestSifoLib" "TestRawQuery" "TestQuery" "TestLoad" "TestJavaServer" "TestGuiBuilder" "TestFileSystem" "TestHttpRequest")

repostr="class method S repo()=S\"$repo\""
secretstr="class method S #\$of()=S\"$token\""

function gen_repo_method() {
	echo "class method Url $1()=Url\"$2/$3\""
}

for test in ${tests[@]}
do
	echo $test
	path="tests/is/L42/tests/L42Source/$test/Secret.L42"
	rm -rf $path
	$(echo -e "$secretstr\n$repostr" >> $path)
done
