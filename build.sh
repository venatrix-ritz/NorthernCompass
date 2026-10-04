#!/bin/bash
MOD_NAME="MC-NorthernCompass"
ssh gojoe "docker run -u 0 --rm -v ~/dev:/workspace -w /workspace eclipse-temurin:25-jdk rm -rf ${MOD_NAME} && mkdir -p ~/dev/${MOD_NAME}"
tar -czf - . | ssh gojoe "cd ~/dev/${MOD_NAME} && tar -xzf - && sed -i 's/\r$//' gradlew && docker run -u 0 --rm -v ~/.gradle-ez:/root/.gradle -v \"\$PWD\":/work -w /work eclipse-temurin:25-jdk ./gradlew build --no-daemon"
