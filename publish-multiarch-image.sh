export VERSION=$1 && docker buildx build --push  --platform linux/amd64,linux/arm64 --tag techwolfpl/grid-virtual-storage:latest --tag techwolfpl/grid-virtual-storage:$1 --build-arg VERSION=$1 .
