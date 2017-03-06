all: java libinssort.so

java:
	javac -cp junit-4.12.jar *.java

libinssort.so:
	gcc -I$$JAVA_HOME/include -I$$JAVA_HOME/include/linux -shared -fpic -o libinssort.so MyInsertionSort.c -std=gnu99

clean:
	rm libinssort.so *.class -f
