#!/bin/sh

readonly SCRIPT_DIR=`dirname $0`
readonly TARGET_DIR=$SCRIPT_DIR/../sample-web-admin/target/snippets/

cd $TARGET_DIR

for file in *.adoc; do
    docker run --rm -v $(pwd):/documents/ asciidoctor/docker-asciidoctor asciidoctor $file
    echo "${file}をHTMLに変換しました"
done

echo "convert success!"
