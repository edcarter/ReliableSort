libinssort.so:
	gcc -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -shared -fpic -o libinssort.so MyInsertionSort.c

clean:
	rm libinssort.so -f
