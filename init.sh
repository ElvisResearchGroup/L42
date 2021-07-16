#!/bin/bash
echo "Enter your git access token: "
read token
echo "Enter the full path to your git repository (no trailing slash): "
read repo

paths=("TestUnit" "TestVoxelMap" "TestSifoLib" "TestRawQuery" "TestQuery" "TestLoad" "TestJavaServer" "TestGuiBuilder" "TestFileSystem")
names=("Unit.L42" "VoxelMapTrait.L42" "Sifo.L42" "RawQuery.L42" "Query.L42" "MiniLib.L42" "JavaServer.L42" "GuiBuilder.L42" "FileSystem.L42")

secretstr="class method S #\$of()=S\"$token\""

function gen_repo_method() {
	echo "class method Url $1()=Url\"$2/$3\""
}

for index in {0..8} 
do
	echo ${paths[$index]}
	repostr=$(gen_repo_method "repo" "$repo" "${names[$index]}")
	path="tests/is/L42/tests/L42Source/${paths[$index]}/Secret.L42"
	rm -rf $path
	$(echo -e "$secretstr\n$repostr" >> $path)
done

echo "TestHttpRequest"
path="tests/is/L42/tests/L42Source/TestHttpRequest/Secret.L42"
rm -rf $path
$(echo -e "$secretstr\n$(gen_repo_method 'httpreqrepo' $repo 'HttpRequest.L42')\n$(gen_repo_method 'adamstowelrepo' $repo 'AdamsTowel.L42')\n$(gen_repo_method 'gitwriterrepo' $repo 'GitWriter.L42')\n$(gen_repo_method 'deployrepo' $repo 'Deploy.L42')\n$(gen_repo_method 'testjar' $repo 'ExampleJar.L42')" >> $path)