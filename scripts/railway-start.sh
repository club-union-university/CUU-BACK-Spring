#!/usr/bin/env bash
set -euo pipefail

# Nixpacks defaults pass -Dserver.port after -jar (invalid for java(1)).
# Pick the Spring Boot fat jar explicitly (exclude *-plain.jar).

if [[ ! -d build/libs ]]; then
  echo "railway-start: build/libs missing — run ./gradlew bootJar (or full build) first." >&2
  exit 1
fi

JAR="$(find build/libs -maxdepth 1 -name '*.jar' ! -name '*-plain.jar' -print -quit || true)"
if [[ -z "${JAR}" ]]; then
  echo "railway-start: no executable boot jar under build/libs/" >&2
  ls -la build/libs >&2 || true
  exit 1
fi

exec java "-Dserver.port=${PORT:-8080}" ${JAVA_OPTS:-} -jar "${JAR}"
