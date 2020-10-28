find -name "*.java" > sources.txt
javac -d bin/ @sources.txt

PACKAGE_ROOT=$(echo "$dirs")

rm -f Manifest.mf
echo "Manifest-Version: 1.0" >> Manifest.mf
echo "Main-Class: com.hvadoda1.dht.chord.starter.ChordServiceStarter" > Manifest.mf

cd bin/
jar cmf ../Manifest.mf ../server.jar com/

cd ..
rm -f Manifest.mf
rm -f sources.txt
rm -rf bin

LIB_PATH=${2:-LIB_PATH=/home/cs557-inst/local/lib/libthrift-0.13.0.jar:/home/cs557-inst/local/lib/slf4j-api-1.7.30.jar:/home/cs557-inst/loca/lib/slf4j-log4j12-1.7.12.jar:/home/cs557-inst/local/lib/javax.annotation-api-1.3.2.jar:/home/cs557-inst/local/lib/slf4j-simple-1.7.30.jar}

java -jar server.jar port=$1 -classpath 