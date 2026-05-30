#!/bin/bash

cd target
java --module-path projekt-0.0.1-SNAPHOST.jar:libs \
     --add-reads projekt=ALL-UNNAMED \
     -m projekt/projekt.frontEnd.Game
