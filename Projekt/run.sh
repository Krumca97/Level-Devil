#!/bin/bash

# spustit backend pres classpath
java -cp "target/classes:target/libs/*" \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     projekt.backEnd.LevelDevilFrontEndApp &

sleep 5

# spustit frontend pres module path
java --add-reads projekt=ALL-UNNAMED \
     --module-path target/classes:target/libs \
     -m projekt/projekt.frontEnd.MainMenu