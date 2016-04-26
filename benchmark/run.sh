#!/usr/bin/env bash

cpanm App::RL

COMAMND_TIME="command time -v"
if [[ `uname` == 'Darwin' ]];
then
    COMAMND_TIME="command time -l"
fi

echo "==> jrunlist"
${COMAMND_TIME} java -jar ../target/jrunlist-*-jar-with-dependencies.jar \
    statop \
    chr.sizes sep-gene.yml paralog.yml  \
    --op intersect --all \
    -o stdout \
    > /dev/null

echo "==> App::RL"
${COMAMND_TIME} runlist \
    stat2 \
    -s chr.sizes sep-gene.yml paralog.yml  \
    --op intersect --all --mk \
    -o stdout \
    > /dev/null
