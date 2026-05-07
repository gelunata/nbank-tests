#!/bin/bash

# загружаем токен из .env
source .env

DOCKER_USER=gelunata
IMAGE_NAME=nbank-tests
TAG=latest

REMOTE_IMAGE="$DOCKER_USER/$IMAGE_NAME:$TAG"

echo "Logging in to Docker Hub..."
echo "$DOCKERHUB_TOKEN" | docker login \
  --username "$DOCKER_USER" \
  --password-stdin

echo "Tagging image..."
docker tag "$IMAGE_NAME:$TAG" "$REMOTE_IMAGE"

echo "Pushing image..."

docker push "$REMOTE_IMAGE"

echo ""
echo "Done!"
echo "Image published successfully."
echo "To pull it:"
echo "docker pull $REMOTE_IMAGE"