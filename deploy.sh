#!/bin/bash
set -euxo pipefail

mkdir -p tmp
cd tmp

git clone --depth=1 https://github.com/ElvisResearchGroup/L42.git
git clone --depth=1 https://github.com/ElvisResearchGroup/L42IDE.git

mkdir -p bin

find L42/src -name "*.java" > srcfiles.txt
find L42IDE/src -name "*.java" >> srcfiles.txt

../L42PortableLinux/L42Internals/jdk-16/bin/javac \
  @srcfiles.txt \
  --enable-preview \
  --release 16 \
  -cp "../L42PortableLinux/L42Internals/L42_lib/*" \
  -d bin

cp L42IDE/src/is/L42/repl/textArea.xhtml bin/is/L42/repl/textArea.xhtml

../L42PortableLinux/L42Internals/jdk-16/bin/jar --create --file L42.jar --manifest L42IDE/MANIFEST.MF -C bin/ .

cp L42.jar ../L42PortableLinux/L42Internals
cp L42.jar ../L42PortableWin/L42Internals
cp L42.jar ../L42PortableMac/L42Internals

cp L42IDE/src/is/L42/repl/textArea.xhtml ../L42PortableLinux/L42Internals/textArea.xhtml
cp L42IDE/src/is/L42/repl/js/mode-l42.js ../L42PortableLinux/L42Internals/js/mode-l42.js
cp L42IDE/src/is/L42/repl/js/editorStyle.js ../L42PortableLinux/L42Internals/js/editorStyle.js
cp L42IDE/src/is/L42/repl/js/utils.js ../L42PortableLinux/L42Internals/js/utils.js
cp L42IDE/src/is/L42/repl/js/theme-l42.js ../L42PortableLinux/L42Internals/js/theme-l42.js
cp L42IDE/src/is/L42/repl/js/theme-l42_eclipse.js ../L42PortableLinux/L42Internals/js/theme-l42_eclipse.js
cp L42IDE/src/is/L42/repl/js/ext-searchbox.js ../L42PortableLinux/L42Internals/js/ext-searchbox.js
cp L42IDE/src/is/L42/repl/js/ace.js ../L42PortableLinux/L42Internals/js/ace.js

cp L42IDE/src/is/L42/repl/textArea.xhtml ../L42PortableWin/L42Internals/textArea.xhtml
cp L42IDE/src/is/L42/repl/js/mode-l42.js ../L42PortableWin/L42Internals/js/mode-l42.js
cp L42IDE/src/is/L42/repl/js/editorStyle.js ../L42PortableWin/L42Internals/js/editorStyle.js
cp L42IDE/src/is/L42/repl/js/utils.js ../L42PortableWin/L42Internals/js/utils.js
cp L42IDE/src/is/L42/repl/js/theme-l42.js ../L42PortableWin/L42Internals/js/theme-l42.js
cp L42IDE/src/is/L42/repl/js/theme-l42_eclipse.js ../L42PortableWin/L42Internals/js/theme-l42_eclipse.js
cp L42IDE/src/is/L42/repl/js/ext-searchbox.js ../L42PortableWin/L42Internals/js/ext-searchbox.js
cp L42IDE/src/is/L42/repl/js/ace.js ../L42PortableWin/L42Internals/js/ace.js

cp L42IDE/src/is/L42/repl/textArea.xhtml ../L42PortableMac/L42Internals/textArea.xhtml
cp L42IDE/src/is/L42/repl/js/mode-l42.js ../L42PortableMac/L42Internals/js/mode-l42.js
cp L42IDE/src/is/L42/repl/js/editorStyle.js ../L42PortableMac/L42Internals/js/editorStyle.js
cp L42IDE/src/is/L42/repl/js/utils.js ../L42PortableMac/L42Internals/js/utils.js
cp L42IDE/src/is/L42/repl/js/theme-l42.js ../L42PortableMac/L42Internals/js/theme-l42.js
cp L42IDE/src/is/L42/repl/js/theme-l42_eclipse.js ../L42PortableMac/L42Internals/js/theme-l42_eclipse.js
cp L42IDE/src/is/L42/repl/js/ext-searchbox.js ../L42PortableMac/L42Internals/js/ext-searchbox.js
cp L42IDE/src/is/L42/repl/js/ace.js ../L42PortableMac/L42Internals/js/ace.js

cp L42IDE/src/is/L42/repl/js/mode-l42.js /vol/ecs/sites/l42/js/mode-l42.js
cp L42IDE/src/is/L42/repl/js/utils.js /vol/ecs/sites/l42/js/utils.js
cp L42IDE/src/is/L42/repl/js/theme-l42.js /vol/ecs/sites/l42/js/theme-l42.js
cp L42IDE/src/is/L42/repl/js/theme-l42_eclipse.js /vol/ecs/sites/l42/js/theme-l42_eclipse.js
cp L42IDE/src/is/L42/repl/js/ace.js /vol/ecs/sites/l42/js/ace.js
cp L42IDE/src/is/L42/repl/css/style.css /vol/ecs/sites/l42/css/style.css
cp L42IDE/src/is/L42/repl/css/logoBlueSmaller.png /vol/ecs/sites/l42/css/logoBlueSmaller.png

verNum=$(grep -w "public static String l42IsRepoVersion" L42/src/is/L42/main/Main.java | cut -c44-47)
mkdir -p "/vol/ecs/sites/l42/${verNum}" 
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/AdamsTowel.xhtml" --output /vol/ecs/sites/l42/${verNum}/AdamsTowel.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/AdamsTowel.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/Deploy.xhtml" --output /vol/ecs/sites/l42/${verNum}/Deploy.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/Deploy.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/FileSystem.xhtml" --output /vol/ecs/sites/l42/${verNum}/FileSystem.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/FileSystem.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/GitWriter.xhtml" --output /vol/ecs/sites/l42/${verNum}/GitWriter.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/GitWriter.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/GuiBuilder.xhtml" --output /vol/ecs/sites/l42/${verNum}/GuiBuilder.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/GuiBuilder.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/HttpRequest.xhtml" --output /vol/ecs/sites/l42/${verNum}/HttpRequest.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/HttpRequest.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/JavaServer.xhtml" --output /vol/ecs/sites/l42/${verNum}/JavaServer.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/JavaServer.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/Json.xhtml" --output /vol/ecs/sites/l42/${verNum}/Json.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/Json.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/Process.xhtml" --output /vol/ecs/sites/l42/${verNum}/Process.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/Process.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/Query.xhtml" --output /vol/ecs/sites/l42/${verNum}/Query.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/Query.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/RawQuery.xhtml" --output /vol/ecs/sites/l42/${verNum}/RawQuery.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/RawQuery.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/Time.xhtml" --output /vol/ecs/sites/l42/${verNum}/Time.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/Time.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/Unit.xhtml" --output /vol/ecs/sites/l42/${verNum}/Unit.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/Unit.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/VoxelMap.xhtml" --output /vol/ecs/sites/l42/${verNum}/VoxelMap.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/VoxelMap.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/${verNum}/WebIntegrated42Lib.xhtml" --output /vol/ecs/sites/l42/${verNum}/WebIntegrated42Lib.xhtml && chmod 644 /vol/ecs/sites/l42/${verNum}/WebIntegrated42Lib.xhtml


curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/index.xhtml" --output /vol/ecs/sites/l42/index.xhtml && chmod 644 /vol/ecs/sites/l42/index.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/indexLib.xhtml" --output /vol/ecs/sites/l42/indexLib.xhtml && chmod 644 /vol/ecs/sites/l42/indexLib.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial.xhtml" --output /vol/ecs/sites/l42/tutorial.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_01Basics.xhtml" --output /vol/ecs/sites/l42/tutorial_01Basics.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_01Basics.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_02ModifiersIntro.xhtml" --output /vol/ecs/sites/l42/tutorial_02ModifiersIntro.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_02ModifiersIntro.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_03BasicClasses.xhtml" --output /vol/ecs/sites/l42/tutorial_03BasicClasses.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_03BasicClasses.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_04ErrorsAndExceptions.xhtml" --output /vol/ecs/sites/l42/tutorial_04ErrorsAndExceptions.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_04ErrorsAndExceptions.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_05Caching.xhtml" --output /vol/ecs/sites/l42/tutorial_05Caching.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_05Caching.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_06CachingMut.xhtml" --output /vol/ecs/sites/l42/tutorial_06CachingMut.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_06CachingMut.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_07InterfacesAndMatching.xhtml" --output /vol/ecs/sites/l42/tutorial_07InterfacesAndMatching.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_07InterfacesAndMatching.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_08Sequences.xhtml" --output /vol/ecs/sites/l42/tutorial_08Sequences.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_08Sequences.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_09InputOutput.xhtml" --output /vol/ecs/sites/l42/tutorial_09InputOutput.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_09InputOutput.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_10Exercises.xhtml" --output /vol/ecs/sites/l42/tutorial_10Exercises.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_10Exercises.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_11MetaprogrammingIntro.xhtml" --output /vol/ecs/sites/l42/tutorial_11MetaprogrammingIntro.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_11MetaprogrammingIntro.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_12Refactoring.xhtml" --output /vol/ecs/sites/l42/tutorial_12Refactoring.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_12Refactoring.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_13MoreDecorators.xhtml" --output /vol/ecs/sites/l42/tutorial_13MoreDecorators.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_13MoreDecorators.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_14ExampleOfProgram.xhtml" --output /vol/ecs/sites/l42/tutorial_14ExampleOfProgram.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_14ExampleOfProgram.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/tutorial_15DeployCode.xhtml" --output /vol/ecs/sites/l42/tutorial_15DeployCode.xhtml && chmod 644 /vol/ecs/sites/l42/tutorial_15DeployCode.xhtml
curl --silent --show-error "https://raw.githubusercontent.com/Language42/Language42.github.io/main/download.xhtml" --output /vol/ecs/sites/l42/download.xhtml && chmod 644 /vol/ecs/sites/l42/download.xhtml


cd ..
zip -r /vol/ecs/sites/l42/L42PortableLinux.zip L42PortableLinux
zip -r /vol/ecs/sites/l42/L42PortableWin.zip L42PortableWin
zip -r /vol/ecs/sites/l42/L42PortableMac.zip L42PortableMac

rm -rf tmp
