#!/bin/bash

# First Build the ImageUtil.jar
cd ImageUtil/src
javac -cp ../../lib/Filters.jar:../../lib/java-image-scaling-0.8.5.jar imageUtil/*.java && jar cf ../../lib/imageUtil.jar imageUtil/*.java && rm imageUtil/*.class
cd ../../

# Now Build Image.java
javac -cp lib/cfx.jar:lib/jmimemagic-0.1.0.jar:lib/imageUtil.jar:lib/Filters.jar:lib/java-image-scaling-0.8.5.jar Image.java

# Cleanup
rm ImageLoader.class ImageType.class