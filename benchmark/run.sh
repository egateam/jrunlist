#!/usr/bin/env bash

cpanm App::RL

COMMAND_TIME="command time -v"
if [[ `uname` == 'Darwin' ]];
then
    COMMAND_TIME="command time -l"
fi

echo "==> jrunlist"
${COMMAND_TIME} java -jar ../target/jrunlist-*-jar-with-dependencies.jar \
    statop \
    chr.sizes sep-gene.yml paralog.yml  \
    --op intersect --all \
    -o stdout \
    > jstatop.csv.tmp

echo "==> App::RL"
${COMMAND_TIME} runlist \
    stat2 \
    -s chr.sizes sep-gene.yml paralog.yml  \
    --op intersect --all --mk \
    -o stdout \
    > pstatop.csv.tmp

