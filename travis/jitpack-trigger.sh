#!/usr/bin/env bash
if [[ $TRAVIS_PULL_REQUEST == 'false' && $TRAVIS_REPO_SLUG == 'AyoCrazy/EasyStage' && $TRAVIS_BRANCH == 'master' ]];
then
    echo 'Trigger JitPack build'
    curl -v --connect-timeout 10 https://jitpack.io/com/github/AyoCrazy/EasyStage/-SNAPSHOT/
fi