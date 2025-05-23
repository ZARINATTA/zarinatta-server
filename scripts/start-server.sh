#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop zarinatta-domain || true
docker rm zarinatta-domain || true
docker pull 615299734340.dkr.ecr.ap-northeast-2.amazonaws.com/zarinatta-domain:latest
docker run -d --name zarinatta-domain -p 8080:8080 615299734340.dkr.ecr.ap-northeast-2.amazonaws.com/zarinatta-domain:latest
echo "--------------- 서버 배포 끝 -----------------"